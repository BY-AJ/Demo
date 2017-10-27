package com.itcast.yb.googleplay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.itcast.yb.googleplay.fragment.BaseFragment;
import com.itcast.yb.googleplay.fragment.FragmentFactory;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.itcast.yb.googleplay.view.PagerTab;

public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private PagerTab mPagerTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerTab = (PagerTab) findViewById(R.id.pagertab);

        MyAdapter mAdapter=new MyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        //将ViewPager与PagerTab绑定在一起
        mPagerTab.setViewPager(mViewPager);

        //设置页面监听事件
        mPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = FragmentFactory.createFragment(position);
                //开始加载数据
                fragment.loadData();
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    //如果Viewpager中要添加的是fragment，则定义的类继承FragmentPagerAdapter
    class MyAdapter extends FragmentPagerAdapter {
        private String[] mTitleTab;//存放标题数组
        public MyAdapter(FragmentManager fm) {
            super(fm);
            mTitleTab = UIUtils.getStringArray(R.array.tab_names);
        }
        //修改标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleTab[position];
        }
        //返回那个位置的fragment对象
        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }
        //返回fragment的数目
        @Override
        public int getCount() {
            return mTitleTab.length;
        }
    }
}
