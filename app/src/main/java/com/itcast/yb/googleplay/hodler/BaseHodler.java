package com.itcast.yb.googleplay.hodler;

import android.view.View;

/**
 * Created by yb on 2017/4/4.
 */

public abstract class BaseHodler<T> {
    // 一个item的根布局
    private View mRootView;

    private T data;

    //当new这个对象时, 就会加载布局, 初始化控件,设置tag
    public BaseHodler() {
        mRootView = initView();
        //3.设置tag
        mRootView.setTag(this);
    }

    // 返回item的布局对象
    public View getRootView() {
        return mRootView;
    }

    //设置当前item的数据
    public void setData(T data) {
        this.data=data;
        refreshView(data);
    }

    //获取当前item的数据
    public T getData() {
        return data;
    }

    /**
     * 1.创建布局,2.找到控件
     */
    public abstract View initView();

    // 4. 根据数据来刷新界面
    public abstract void refreshView(T data);
}
