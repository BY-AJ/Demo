package com.itcast.yb.googleplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.itcast.yb.googleplay.R;
import com.itcast.yb.googleplay.manage.ThreadManager;
import com.itcast.yb.googleplay.utils.UIUtils;

/**
 * Created by yb on 2017/4/3.
 */

public abstract class LoadingPage extends FrameLayout{
    private static final int STATE_LOAD_UNDO = 1;// 未加载
    private static final int STATE_LOAD_LOADING = 2;// 正在加载
    private static final int STATE_LOAD_ERROR = 3;// 加载失败
    private static final int STATE_LOAD_EMPTY = 4;// 数据为空
    private static final int STATE_LOAD_SUCCESS = 5;// 加载成功

    private int mCurrentState = STATE_LOAD_UNDO;// 当前状态
    private View mLoadingPage;
    private View mLoadingError;
    private View mLoadingEmpty;
    private View mLoadingSuccess;

    public LoadingPage(Context context) {
        super(context);
        initView();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //加载中
        if(mLoadingPage == null) {
            mLoadingPage = UIUtils.inflate(R.layout.page_loading);
            addView(mLoadingPage);
        }
        //加载失败
        if(mLoadingError == null) {
            mLoadingError = UIUtils.inflate(R.layout.page_load_error);
            Button btn_error= (Button) mLoadingError.findViewById(R.id.btn_error);
            btn_error.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadData();
                }
            });
            addView(mLoadingError);
        }
        //加载为空
        if(mLoadingEmpty == null) {
            mLoadingEmpty = UIUtils.inflate(R.layout.page_load_empty);
            addView(mLoadingEmpty);
        }
        showPage();
    }

    /**
     * 显示加载那个页面
     */
    private void showPage() {
        mLoadingPage.setVisibility(mCurrentState == STATE_LOAD_UNDO ||
                mCurrentState==STATE_LOAD_LOADING ? View.VISIBLE : View.GONE);

        mLoadingError.setVisibility(mCurrentState == STATE_LOAD_ERROR ? View.VISIBLE : View.GONE);

        mLoadingEmpty.setVisibility(mCurrentState == STATE_LOAD_EMPTY ? View.VISIBLE : View.GONE);

        //当成功布局为空,并且当前状态为成功,才初始化成功的布局
        if(mLoadingSuccess ==null && mCurrentState == STATE_LOAD_SUCCESS) {
            mLoadingSuccess = createSuccessView();
            if(mLoadingSuccess != null) {
                addView(mLoadingSuccess);
            }
        }

        if(mLoadingSuccess != null) {
            mLoadingSuccess.setVisibility(mCurrentState==STATE_LOAD_SUCCESS ?View.VISIBLE:View.GONE);
        }
    }

    //开始加载数据
    public void loadData() {
        if(mCurrentState != STATE_LOAD_SUCCESS) { // 如果当前没有加载, 就开始加载数据
            mCurrentState = STATE_LOAD_SUCCESS;
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    final ResultState resultState= onLoad();
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if(resultState != null) {
                                // 网络加载结束后,更新网络状态
                                mCurrentState = resultState.getState();
                                // 根据最新的状态来刷新页面
                                showPage();
                            }
                        }
                    });
                }
            });
/*            new Thread(){
                @Override
                public void run() {
                    final ResultState resultState= onLoad();
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if(resultState != null) {
                                // 网络加载结束后,更新网络状态
                                mCurrentState = resultState.getState();
                                // 根据最新的状态来刷新页面
                                showPage();
                            }
                        }
                    });
                }
            }.start();*/
        }
    }

    //枚举(STATE_SUCCESS,STATE_ERROR,STATE_EMPTY相当于这个类ResultState的引用对象)
    public enum ResultState {
        STATE_SUCCESS(STATE_LOAD_SUCCESS),STATE_ERROR(STATE_LOAD_ERROR) ,STATE_EMPTY(STATE_LOAD_EMPTY);
        private int state;
        private ResultState(int state) {
            this.state=state;
        }
        public int getState() {
            return state;
        }
    }

    //创建成功布局，交给实现这个类的去实现
    public abstract View createSuccessView();
    // 加载网络数据, 返回值表示请求网络结束后的状态
    public abstract ResultState onLoad();
}
