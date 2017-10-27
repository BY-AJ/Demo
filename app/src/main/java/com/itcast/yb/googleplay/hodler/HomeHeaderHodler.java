package com.itcast.yb.googleplay.hodler;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.itcast.yb.googleplay.R;
import com.itcast.yb.googleplay.http.HttpHelper;
import com.itcast.yb.googleplay.utils.BitmapHelper;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by yb on 2017/4/10.
 */

public class HomeHeaderHodler extends BaseHodler<ArrayList<String>>{

    private ViewPager mViewPager;
    private ArrayList<String> data;
    private LinearLayout llRoot;
    private int mPreviousPos;

    @Override
    public View initView() {
        //创建根布局
        RelativeLayout rlRoot = new RelativeLayout(UIUtils.getContext());
        //设置根布局的宽和高
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,UIUtils.dip2px(180));
        rlRoot.setLayoutParams(params);

        //创建viewpager
        mViewPager = new ViewPager(UIUtils.getContext());
        //设置宽和高
        RelativeLayout.LayoutParams vpParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        //添加相对布局中
        rlRoot.addView(mViewPager,vpParams);

        //创建线性布局
        llRoot = new LinearLayout(UIUtils.getContext());
        //设置内边距
        int padding = UIUtils.dip2px(10);
        llRoot.setPadding(padding,padding,padding,padding);
        //设置布局水平方向
        llRoot.setOrientation(LinearLayout.HORIZONTAL);
        //设置宽和高
        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置布局在父控件的位置
        llParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        llParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        rlRoot.addView(llRoot,llParams);
        return rlRoot;
    }

    @Override
    public void refreshView(ArrayList<String> data) {
        this.data = data;
        mViewPager.setAdapter(new HomeHeaderAdapter());
        //设置当前图片的位置
        mViewPager.setCurrentItem(data.size()*10000);

        //加载小圆点
        for (int i=0; i<data.size(); i++) {
            ImageView point = new ImageView(UIUtils.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i == 0) {
                point.setImageResource(R.drawable.indicator_selected);
            }else {
                point.setImageResource(R.drawable.indicator_normal);
                params.leftMargin = UIUtils.dip2px(6);
                point.setLayoutParams(params);
            }
            llRoot.addView(point);
        }

        //设置页面监听事件
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                position = position % HomeHeaderHodler.this.data.size();
                //当前点被选中
                ImageView point = (ImageView) llRoot.getChildAt(position);
                point.setImageResource(R.drawable.indicator_selected);
                //上个点变为不选中
                ImageView prePoint = (ImageView) llRoot.getChildAt(mPreviousPos);
                prePoint.setImageResource(R.drawable.indicator_normal);

                mPreviousPos = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //实现头条的轮播
        RunnableTask task = new RunnableTask();
        task.start();
    }

    class RunnableTask implements Runnable {

        public void start() {
            //每次开始的时候，清除之前的消息，避免消息的重复
            UIUtils.getHandler().removeCallbacksAndMessages(null);
            UIUtils.getHandler().postDelayed(this,3000);
        }

        @Override
        public void run() {
            int currentItem = mViewPager.getCurrentItem();
            currentItem ++ ;
            mViewPager.setCurrentItem(currentItem);
            UIUtils.getHandler().postDelayed(this,3000);
        }
    }

    class HomeHeaderAdapter extends PagerAdapter {

        private BitmapUtils mBitmapUtils;

        public HomeHeaderAdapter() {
            mBitmapUtils = BitmapHelper.getBitmapUtils();
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % data.size();//位置需要改变，求余数

            String url = data.get(position);
            ImageView view = new ImageView(UIUtils.getContext());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            mBitmapUtils.display(view, HttpHelper.URL+"image?name=" + url);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
