package com.itcast.yb.googleplay.fragment;

import java.util.HashMap;

/**
 * fragment工厂类
 * Created by yb on 2017/3/31.
 */

public class FragmentFactory {
    // 保存Fragment集合,方便复用
    private static HashMap<Integer,BaseFragment> mFragmentMap = new HashMap<Integer, BaseFragment>();
    // 根据指针位置,生产相应的Fragment
    public static BaseFragment createFragment(int pos) {
        BaseFragment fragment = mFragmentMap.get(pos) ;
        if(fragment == null) {
            switch (pos) {
                case 0:fragment = new HomeFragment();break;
                case 1:fragment = new AppFragment();break;
                case 2:fragment = new GameFragment();break;
                case 3:fragment = new SubjectFragment();break;
                case 4:fragment = new RecommendFragment();break;
                case 5:fragment = new CategoryFragment();break;
                case 6:fragment = new HotFragment();break;
                default:break;
            }
            mFragmentMap.put(pos,fragment);
        }
        return fragment;
    }
}
