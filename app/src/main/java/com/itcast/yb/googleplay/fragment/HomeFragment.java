package com.itcast.yb.googleplay.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.itcast.yb.googleplay.HomeAppDetailActivity;
import com.itcast.yb.googleplay.adapter.MyBaseAdapter;
import com.itcast.yb.googleplay.bean.AppInfo;
import com.itcast.yb.googleplay.hodler.BaseHodler;
import com.itcast.yb.googleplay.hodler.HomeHeaderHodler;
import com.itcast.yb.googleplay.hodler.HomeHodler;
import com.itcast.yb.googleplay.http.protocol.HomeProtocol;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.itcast.yb.googleplay.view.LoadingPage;
import com.itcast.yb.googleplay.view.MyListView;

import java.util.ArrayList;

/**
 * Created by yb on 2017/3/31.
 */

public class HomeFragment extends BaseFragment{

    private ArrayList<AppInfo> data;
    private ArrayList<String> mPicturesList;

    // 如果加载数据成功, 就回调此方法, 在主线程运行
    @Override
    public View createSuccessView() {
        MyListView mListView=new MyListView(UIUtils.getContext());
        //给listview添加一个头布局
        HomeHeaderHodler hodler = new HomeHeaderHodler();
        mListView.addHeaderView(hodler.getRootView());

        mListView.setAdapter(new HomeAdapter(data));

        //判断头条数据是否为空
        if(mPicturesList != null) {
            hodler.setData(mPicturesList);
        }

        //设置条目的点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo info = data.get(position-1);
                String packageName = info.packageName;
                //开启activity
                Intent intent=new Intent(UIUtils.getContext(), HomeAppDetailActivity.class);
                intent.putExtra("packageName",packageName);
                startActivity(intent);
            }
        });

        return mListView;
    }
    // 运行在子线程,可以直接执行耗时网络操作
    @Override
    public LoadingPage.ResultState onLoad() {

        HomeProtocol protocol = new HomeProtocol();
        data = protocol.getData(0);// 加载第一页数据

        mPicturesList = protocol.getPicturesList();
        // 校验数据并返回
        return check(data);
    }

    class HomeAdapter extends MyBaseAdapter<AppInfo> {

        public HomeAdapter(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public BaseHodler<AppInfo> getHodler(int position) {
            return new HomeHodler();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
            HomeProtocol protocol=new HomeProtocol();
            ArrayList<AppInfo> moreData=protocol.getData(getListSize());
            return moreData;
        }
    }
}
