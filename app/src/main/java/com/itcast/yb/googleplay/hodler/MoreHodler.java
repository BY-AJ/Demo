package com.itcast.yb.googleplay.hodler;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itcast.yb.googleplay.R;
import com.itcast.yb.googleplay.utils.UIUtils;

/**
 * Created by yb on 2017/4/5.
 */

public class MoreHodler extends BaseHodler<Integer>{

    private LinearLayout ll_root;
    private TextView tv_load_error;

    public static final int START_MORE_MORE = 1; //加载更多
    public static final int START_MORE_ERROR = 2; //加载失败
    public static final int START_MORE_NONE = 3; //没有更多数据了

    public MoreHodler(boolean hasmore) {
        // 如果有更多数据,状态为STATE_MORE_MORE,否则为STATE_MORE_NONE,将此状态传递给父类的data,
        // 父类会同时刷新界面
        setData(hasmore ? START_MORE_MORE : START_MORE_NONE);
    }

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_load_more);
        ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
        tv_load_error = (TextView) view.findViewById(R.id.tv_load_error);
        return view;
    }

    @Override
    public void refreshView(Integer data) {
        switch (data) {
            case START_MORE_MORE : //加载更多
                ll_root.setVisibility(View.VISIBLE);
                tv_load_error.setVisibility(View.GONE);
                break;
            case START_MORE_NONE : //没有更多的数据
                ll_root.setVisibility(View.GONE);
                tv_load_error.setVisibility(View.GONE);
                break;
            case START_MORE_ERROR : //加载失败
                ll_root.setVisibility(View.GONE);
                tv_load_error.setVisibility(View.VISIBLE);
                break;
        }
    }
}
