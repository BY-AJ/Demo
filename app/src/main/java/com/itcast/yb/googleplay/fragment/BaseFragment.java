package com.itcast.yb.googleplay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcast.yb.googleplay.utils.UIUtils;
import com.itcast.yb.googleplay.view.LoadingPage;

import java.util.ArrayList;

/**
 * Created by yb on 2017/3/31.
 */

public abstract class BaseFragment extends Fragment{

    private LoadingPage mLoadingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLoadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public View createSuccessView() {
                return BaseFragment.this.createSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return BaseFragment.this.onLoad();
            }
        };
        return mLoadingPage;
    }

    //基类也不知道怎么处理，交给它的子类去实现
    public abstract View createSuccessView();
    // 加载网络数据, 必须由子类来实现
    public abstract LoadingPage.ResultState onLoad();

    // 开始加载数据
    public void loadData() {
        if (mLoadingPage != null) {
            mLoadingPage.loadData();
        }
    }

    //检查状态
    public LoadingPage.ResultState check(Object obj) {
        if(obj != null) {
            if(obj instanceof ArrayList) {
                ArrayList list = (ArrayList) obj;
                if(list.isEmpty()) {
                    return LoadingPage.ResultState.STATE_EMPTY;
                }else {
                    return LoadingPage.ResultState.STATE_SUCCESS;
                }
            }
        }
        return LoadingPage.ResultState.STATE_ERROR;
    }
}
