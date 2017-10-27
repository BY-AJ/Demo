package com.itcast.yb.googleplay.http.protocol;

import com.itcast.yb.googleplay.bean.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 首页应用详情页
 * Created by yb on 2017/4/12.
 */

public class HomeDetailProtocol extends BaseProtocol<AppInfo>{
    private String packageName;

    public HomeDetailProtocol(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getKey() {
        return "detail";
    }

    @Override
    public String getParams() {
        return "&packageName="+packageName;
    }

    @Override
    public AppInfo parseData(String result) {
        try {
            JSONObject jo = new JSONObject(result);
            AppInfo appInfo = new AppInfo();
            appInfo.author = jo.getString("author");
            appInfo.date = jo.getString("date");
            appInfo.des = jo.getString("des");
            appInfo.downloadNum = jo.getString("downloadNum");
            appInfo.downloadUrl = jo.getString("downloadUrl");
            appInfo.iconUrl = jo.getString("iconUrl");
            appInfo.name = jo.getString("name");
            appInfo.packageName = jo.getString("packageName");

            //解析安全模块信息
            JSONArray ja1 = jo.getJSONArray("safe");
            ArrayList<AppInfo.SafeInfo> safeInfo= new ArrayList<>();
            for (int i=0; i<ja1.length(); i++) {
                JSONObject jo1 = ja1.getJSONObject(i);
                AppInfo.SafeInfo info = new AppInfo.SafeInfo();
                info.safeDes = jo1.getString("safeDes");
                info.safeDesUrl = jo1.getString("safeDesUrl");
                info.safeUrl = jo1.getString("safeUrl");

                safeInfo.add(info);
            }
            appInfo.safe = safeInfo;

            //解析截图信息
            JSONArray ja2 = jo.getJSONArray("screen");
            ArrayList<String> screen = new ArrayList<String>();
            for (int i = 0; i < ja2.length(); i++) {
                String pic = ja2.getString(i);
                screen.add(pic);
            }
            appInfo.screen = screen;

            appInfo.size = jo.getLong("size");
            appInfo.stars = (float) jo.getDouble("stars");
            appInfo.version = jo.getString("version");

            return appInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
