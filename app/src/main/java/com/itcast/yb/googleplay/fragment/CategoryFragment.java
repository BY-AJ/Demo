package com.itcast.yb.googleplay.fragment;

import android.view.View;

import com.itcast.yb.googleplay.adapter.MyBaseAdapter;
import com.itcast.yb.googleplay.bean.CategoryInfo;
import com.itcast.yb.googleplay.hodler.BaseHodler;
import com.itcast.yb.googleplay.hodler.CategoryHodler;
import com.itcast.yb.googleplay.hodler.TitleHodler;
import com.itcast.yb.googleplay.http.protocol.CategoryProtocol;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.itcast.yb.googleplay.view.LoadingPage;
import com.itcast.yb.googleplay.view.MyListView;

import java.util.ArrayList;

/**
 * Created by yb on 2017/3/31.
 */

public class CategoryFragment extends BaseFragment{

    private ArrayList<CategoryInfo> data;

    @Override
    public View createSuccessView() {
        MyListView view = new MyListView(UIUtils.getContext());
        view.setAdapter(new CategoryAdapter(data));
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        CategoryProtocol protocol = new CategoryProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class CategoryAdapter extends MyBaseAdapter<CategoryInfo> {

        public CategoryAdapter(ArrayList<CategoryInfo> data) {
            super(data);
        }

        @Override
        public BaseHodler<CategoryInfo> getHodler(int positon) {
            // 判断是标题类型还是普通分类类型, 来返回不同的holder
            CategoryInfo info = data.get(positon);
            if(info.isTitle) {
                return new TitleHodler();
            }else {
                return new CategoryHodler();
            }
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;//每次比父控件多一个类型
        }

        @Override
        public int getInnerType(int position) {
            // 判断是标题类型还是普通分类类型
            CategoryInfo info = data.get(position);
            if(info.isTitle) {
                //返回标题类型
                return super.getInnerType(position) + 1;
            }else {
                //返回普通类型
                return super.getInnerType(position);
            }
        }

        @Override
        public boolean hasmore() {
            return false;//表示没有更多的数据，不要加载,隐藏控件
        }

        @Override
        public ArrayList<CategoryInfo> onLoadMore() {
            return null;
        }
    }
}
