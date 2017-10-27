package com.itcast.yb.googleplay.fragment;

import android.view.View;

import com.itcast.yb.googleplay.adapter.MyBaseAdapter;
import com.itcast.yb.googleplay.bean.AppInfo;
import com.itcast.yb.googleplay.hodler.AppHodler;
import com.itcast.yb.googleplay.hodler.BaseHodler;
import com.itcast.yb.googleplay.http.protocol.AppProtocol;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.itcast.yb.googleplay.view.LoadingPage;
import com.itcast.yb.googleplay.view.MyListView;

import java.util.ArrayList;

/**
 * Created by yb on 2017/3/31.
 */

public class AppFragment extends BaseFragment{
    private ArrayList<AppInfo> data;

    @Override
    public View createSuccessView() {
        MyListView mListView=new MyListView(UIUtils.getContext());
        mListView.setAdapter(new AppAdapter(data));
        return mListView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        AppProtocol protocol = new AppProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class AppAdapter extends MyBaseAdapter<AppInfo> {

        public AppAdapter(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public BaseHodler<AppInfo> getHodler(int position) {
            return new AppHodler();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
            AppProtocol protocol = new AppProtocol();
            ArrayList<AppInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }

    }
}
