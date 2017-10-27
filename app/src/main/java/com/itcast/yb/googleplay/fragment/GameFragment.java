package com.itcast.yb.googleplay.fragment;

import android.view.View;
import android.widget.TextView;

import com.itcast.yb.googleplay.utils.UIUtils;
import com.itcast.yb.googleplay.view.LoadingPage;

/**
 * Created by yb on 2017/3/31.
 */

public class GameFragment extends BaseFragment{

    @Override
    public View createSuccessView() {
        TextView view=new TextView(UIUtils.getContext());
        view.setText("GameFragment");
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        return LoadingPage.ResultState.STATE_SUCCESS;
    }
}
