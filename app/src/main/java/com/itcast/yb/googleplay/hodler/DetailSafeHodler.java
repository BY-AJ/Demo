package com.itcast.yb.googleplay.hodler;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itcast.yb.googleplay.R;
import com.itcast.yb.googleplay.bean.AppInfo;
import com.itcast.yb.googleplay.http.HttpHelper;
import com.itcast.yb.googleplay.utils.BitmapHelper;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

/**
 * Created by yb on 2017/4/12.
 */

public class DetailSafeHodler extends BaseHodler<AppInfo>{

    private ImageView[] mImages;
    private ImageView[] mImagesDes;
    private TextView[] mTextDes;
    private LinearLayout[] mllDes;
    private BitmapUtils mBitmapUtils;
    private RelativeLayout rlRoots;
    private LinearLayout llRoots;
    private int mHeight;
    private LinearLayout.LayoutParams mParams;
    private ImageView ivArrow;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_safeinfo);
        mImages = new ImageView[4];
        mImages[0] = (ImageView) view.findViewById(R.id.iv_safe1);
        mImages[1] = (ImageView) view.findViewById(R.id.iv_safe2);
        mImages[2] = (ImageView) view.findViewById(R.id.iv_safe3);
        mImages[3] = (ImageView) view.findViewById(R.id.iv_safe4);

        mImagesDes = new ImageView[4];
        mImagesDes[0] = (ImageView) view.findViewById(R.id.iv_des1);
        mImagesDes[1] = (ImageView) view.findViewById(R.id.iv_des2);
        mImagesDes[2] = (ImageView) view.findViewById(R.id.iv_des3);
        mImagesDes[3] = (ImageView) view.findViewById(R.id.iv_des4);

        mTextDes = new TextView[4];
        mTextDes[0] = (TextView) view.findViewById(R.id.tv_des1);
        mTextDes[1] = (TextView) view.findViewById(R.id.tv_des2);
        mTextDes[2] = (TextView) view.findViewById(R.id.tv_des3);
        mTextDes[3] = (TextView) view.findViewById(R.id.tv_des4);

        mllDes = new LinearLayout[4];
        mllDes[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
        mllDes[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
        mllDes[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
        mllDes[3] = (LinearLayout) view.findViewById(R.id.ll_des4);

        mBitmapUtils = BitmapHelper.getBitmapUtils();

        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);

        rlRoots = (RelativeLayout) view.findViewById(R.id.rl_des_root);
        //设置条目的点击事件
        rlRoots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        llRoots = (LinearLayout) view.findViewById(R.id.ll_des_root);
        return view;
    }

    private boolean isOpen =false;// 标记安全描述开关状态,默认关

    // 打开或者关闭安全描述信息
    private void toggle() {
        ValueAnimator animator =null;
        if(isOpen) {
            //关闭
            isOpen = false;
            // 属性动画
            animator=ValueAnimator.ofInt(mHeight,0);// 从某个值变化到某个值
        }else {
            //打开
            isOpen = true;
            animator=ValueAnimator.ofInt(0,mHeight);
        }
        // 动画更新的监听
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            // 启动动画之后, 会不断回调此方法来获取最新的值
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // 获取最新的高度值
                Integer height = (Integer) valueAnimator.getAnimatedValue();
                // 重新修改布局高度
                mParams.height = height;
                llRoots.setLayoutParams(mParams);
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                // 动画结束的事件
                // 更新小箭头的方向
                if (isOpen) {
                    ivArrow.setImageResource(R.drawable.arrow_up);
                } else {
                    ivArrow.setImageResource(R.drawable.arrow_down);
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animator.setDuration(200);// 动画时间
        animator.start();// 启动动画
    }

    @Override
    public void refreshView(AppInfo data) {
        ArrayList<AppInfo.SafeInfo> safe = data.safe;

        for (int i=0; i<4 ; i++) {
            if(i < safe.size()) {
                AppInfo.SafeInfo safeInfo = safe.get(i);
                // 安全标识图片
                mBitmapUtils.display(mImages[i], HttpHelper.URL+"image?name="+safeInfo.safeUrl);
                // 安全描述文字
                mTextDes[i].setText(safeInfo.safeDes);
                // 安全描述图片
                mBitmapUtils.display(mImagesDes[i],HttpHelper.URL+"image?name="+safeInfo.safeDesUrl);
            }else {
                // 剩下不应该显示的图片
                mImages[i].setVisibility(View.GONE);
                // 隐藏多余的描述条目
                mllDes[i].setVisibility(View.GONE);
            }
        }
        // 获取安全描述的完整高度
        llRoots.measure(0,0);
        mHeight = llRoots.getMeasuredHeight();
        // 修改安全描述布局高度为0,达到隐藏效果
        mParams = (LinearLayout.LayoutParams) llRoots.getLayoutParams();
        mParams.height = 0;
        llRoots.setLayoutParams(mParams);
    }
}
