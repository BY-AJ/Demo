package com.itcast.yb.googleplay.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.itcast.yb.googleplay.hodler.BaseHodler;
import com.itcast.yb.googleplay.hodler.MoreHodler;
import com.itcast.yb.googleplay.manage.ThreadManager;
import com.itcast.yb.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by yb on 2017/4/4.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter{
    private ArrayList<T> data;
    private static final int LOAD_MORE = 0; //加载更多布局
    private static final int LOAD_NORMAL = 1; //正常布局

    public MyBaseAdapter(ArrayList<T> data) {
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size() +1;//给加载更多预留一个位置
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //返回list有几个item布局,这里是普通布局+加载更多布局
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //返回当前位置是哪个类型的布局
    @Override
    public int getItemViewType(int position) {
        if(position == getCount()-1) {
            return LOAD_MORE;
        }else {
            return getInnerType(position);//默认是正常
        }
    }

    //目的，便于扩展，可以由用户自己决定
    public int getInnerType(int position) {
        return LOAD_NORMAL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHodler hodler;
        if(convertView == null) {
            //1.加载布局
            //2.找到控件
            //3.设置tag
            if(getItemViewType(position) == LOAD_MORE) {
                //判断是否为加载更多,如果是，显示加载更多布局
                hodler = new MoreHodler(hasmore());
            }else {
                //如果不是，显示普通布局
                hodler=getHodler(position);
            }
        }else {
            hodler= (BaseHodler) convertView.getTag();
        }

        if(getItemViewType(position) != LOAD_MORE) {
            //4.设置刷新数据
            hodler.setData(getItem(position));
        }else {
            //加载更多数据
            MoreHodler moreHodler = (MoreHodler) hodler;
            if(moreHodler.getData() == MoreHodler.START_MORE_MORE) {
                loadMore(moreHodler);
            }
        }
        return hodler.getRootView();
    }

    //默认有加载更多,子类可以根据情况重写
    public boolean hasmore() {
        return true;
    }

    // 标记是否正在加载更多
    private boolean isLoadMore = false;

    public void loadMore(final MoreHodler hodler) {
        if(!isLoadMore) {
            isLoadMore = true;
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<T> moreData = onLoadMore();
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if(moreData != null) {
                                // 每一页有20条数据, 如果返回的数据小于20条, 就认为到了最后一页了
                                if(moreData.size() < 20) {
                                    hodler.setData(MoreHodler.START_MORE_NONE);
                                    Toast.makeText(UIUtils.getContext(),"没有更多的数据了",Toast.LENGTH_SHORT).show();
                                }else {
                                    // 还有更多数据
                                    hodler.setData(MoreHodler.START_MORE_MORE);
                                }
                                // 将更多数据追加到当前集合中
                                data.addAll(moreData);
                                //更新数据
                                MyBaseAdapter.this.notifyDataSetChanged();
                            }else {
                                //加载失败
                                hodler.setData(MoreHodler.START_MORE_ERROR);
                            }
                            isLoadMore = false;
                        }
                    });
                }
            });
            /*new Thread(){
                @Override
                public void run() {
                    final ArrayList<T> moreData = onLoadMore();
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if(moreData != null) {
                                // 每一页有20条数据, 如果返回的数据小于20条, 就认为到了最后一页了
                                if(moreData.size() < 20) {
                                    hodler.setData(MoreHodler.START_MORE_NONE);
                                    Toast.makeText(UIUtils.getContext(),"没有更多的数据了",Toast.LENGTH_SHORT).show();
                                }else {
                                    // 还有更多数据
                                    hodler.setData(MoreHodler.START_MORE_MORE);
                                }
                                // 将更多数据追加到当前集合中
                                data.addAll(moreData);
                                //更新数据
                                MyBaseAdapter.this.notifyDataSetChanged();
                            }else {
                                //加载失败
                                hodler.setData(MoreHodler.START_MORE_ERROR);
                            }
                            isLoadMore = false;
                        }
                    });
                }
            }.start();*/
        }
    }

    // 返回当前页面的holder对象, 必须子类实现
    public abstract BaseHodler<T> getHodler(int position);

    // 加载更多数据, 必须由子类实现
    public abstract ArrayList<T> onLoadMore();

    //获取当前集合大小
    public int getListSize() {
        return data.size();
    }
}
