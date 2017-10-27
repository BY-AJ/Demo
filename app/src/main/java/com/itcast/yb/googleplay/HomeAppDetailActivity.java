package com.itcast.yb.googleplay;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.itcast.yb.googleplay.bean.AppInfo;
import com.itcast.yb.googleplay.hodler.AppDetailInfoHodler;
import com.itcast.yb.googleplay.hodler.DetailDesHodler;
import com.itcast.yb.googleplay.hodler.DetailDownloadHolder;
import com.itcast.yb.googleplay.hodler.DetailPicsHodler;
import com.itcast.yb.googleplay.hodler.DetailSafeHodler;
import com.itcast.yb.googleplay.http.protocol.HomeDetailProtocol;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.itcast.yb.googleplay.view.LoadingPage;

/**
 * Created by yb on 2017/4/12.
 */

public class HomeAppDetailActivity extends BaseActivity{

    private LoadingPage mLoadingPage;
    private String mPackageName;
    private AppInfo data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取从HomeFragment传递过来的包名
        mPackageName = getIntent().getStringExtra("packageName");

        mLoadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public View createSuccessView() {
                return HomeAppDetailActivity.this.createSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return HomeAppDetailActivity.this.onLoad();
            }
        };
        // 直接将一个view对象设置给activity
        setContentView(mLoadingPage);
        // 开始加载网络数据
        mLoadingPage.loadData();
        // 初始化actionbar
        initActionbar();
    }

    private void initActionbar() {
        ActionBar actionbar = getSupportActionBar();
        // actionbar.setHomeButtonEnabled(true);// home处可以点击
        actionbar.setDisplayHomeAsUpEnabled(true);// 显示左上角返回键,当和侧边栏结合时展示三个杠图片
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 加载布局
     */
    public View createSuccessView() {
        View view = UIUtils.inflate(R.layout.page_home_detail);

        //应用详情信息模块
        FrameLayout flDetailAppInfo = (FrameLayout) view.findViewById(R.id.fl_detail_appinfo);
        AppDetailInfoHodler appDetailInfoHodler = new AppDetailInfoHodler();
        flDetailAppInfo.addView(appDetailInfoHodler.getRootView());
        appDetailInfoHodler.setData(data);

        //安全模块信息
        FrameLayout flDetailSafe = (FrameLayout) view.findViewById(R.id.fl_detail_safe);
        DetailSafeHodler safeHodler = new DetailSafeHodler();
        flDetailSafe.addView(safeHodler.getRootView());
        safeHodler.setData(data);

        //截图模块信息
        HorizontalScrollView hsvDetailPics = (HorizontalScrollView) view.findViewById(R.id.hsv_detail_pics);
        DetailPicsHodler picsHodler = new DetailPicsHodler();
        hsvDetailPics.addView(picsHodler.getRootView());
        picsHodler.setData(data);

        //介绍模块信息
        FrameLayout flDetailDes = (FrameLayout) view.findViewById(R.id.fl_detail_des);
        DetailDesHodler desHodler = new DetailDesHodler();
        flDetailDes.addView(desHodler.getRootView());
        desHodler.setData(data);

       //下载模块设计
        FrameLayout flDownLoad = (FrameLayout) view.findViewById(R.id.fl_detail_download);
        DetailDownloadHolder downloadHolder=new DetailDownloadHolder();
        flDownLoad.addView(downloadHolder.getRootView());
        downloadHolder.setData(data);
        return view;
    }

    /**
     * 加载数据
     */
    public LoadingPage.ResultState onLoad() {
        HomeDetailProtocol protocol = new HomeDetailProtocol(mPackageName);
        data = protocol.getData(0);
        if(data != null) {
            return LoadingPage.ResultState.STATE_SUCCESS;
        }else {
            return LoadingPage.ResultState.STATE_ERROR;
        }
    }
}
