package com.itcast.yb.googleplay.fragment;

import android.view.View;

import com.itcast.yb.googleplay.adapter.MyBaseAdapter;
import com.itcast.yb.googleplay.bean.SubjectInfo;
import com.itcast.yb.googleplay.hodler.BaseHodler;
import com.itcast.yb.googleplay.hodler.SubjectHodler;
import com.itcast.yb.googleplay.http.protocol.SubjectProtocol;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.itcast.yb.googleplay.view.LoadingPage;
import com.itcast.yb.googleplay.view.MyListView;

import java.util.ArrayList;

/**
 * Created by yb on 2017/3/31.
 */

public class SubjectFragment extends BaseFragment{

    private ArrayList<SubjectInfo> data;

    @Override
    public View createSuccessView() {
        MyListView listView=new MyListView(UIUtils.getContext());
        listView.setAdapter(new SubjectAdapter(data));
        return listView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        SubjectProtocol protocol=new SubjectProtocol();
        data=protocol.getData(0);
        return check(data);
    }

    class SubjectAdapter extends MyBaseAdapter {

        public SubjectAdapter(ArrayList data) {
            super(data);
        }

        @Override
        public BaseHodler getHodler(int position) {
            return new SubjectHodler();
        }

        @Override
        public ArrayList onLoadMore() {
            SubjectProtocol protocol=new SubjectProtocol();
            ArrayList<SubjectInfo> moreData = protocol.getData(getListSize());
            return moreData;
        }
    }
}
