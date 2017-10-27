package com.itcast.yb.googleplay.bean;

import java.util.ArrayList;

/**
 * 首页应用信息封装
 * Created by yb on 2017/4/6.
 */

public class AppInfo {
    public String author;
    public String date;
    public String des;
    public String downloadNum;
    public String downloadUrl;
    public String iconUrl;
    public String id;
    public String name;
    public String packageName;
    public ArrayList<SafeInfo> safe;
    public ArrayList<String> screen;
    public long size;
    public float stars;
    public String version;

    public static class SafeInfo{
        public String safeDes;
        public String safeDesUrl;
        public String safeUrl;
    }

}
