package com.itcast.yb.googleplay.http.protocol;

import com.itcast.yb.googleplay.bean.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yb on 2017/4/6.
 */

public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>>{

    private ArrayList<String> picturesList;

    @Override
    public String getKey() {
        return "home";
    }

    @Override
    public String getParams() {
        return "";// 如果没有参数,就传空串,不要传null
    }

    //使用JsonObject解析方式: 如果遇到{},就是JsonObject;如果遇到[], 就是JsonArray
    @Override
    public ArrayList<AppInfo> parseData(String result) {
        try {
            JSONObject jo = new JSONObject(result);//获取一个jsonobject对象
            // 解析应用列表数据
            JSONArray ja = jo.getJSONArray("list");//获取jsonarray对象
            ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
            for (int i=0; i<ja.length(); i++) {
                AppInfo info=new AppInfo();
                JSONObject jo1 = ja.getJSONObject(i);
                info.des=jo1.getString("des");
                info.downloadUrl=jo1.getString("downloadUrl");
                info.iconUrl=jo1.getString("iconUrl");
                info.id=jo1.getString("id");
                info.name=jo1.getString("name");
                info.packageName=jo1.getString("packageName");
                info.size=jo1.getLong("size");
                info.stars= (float) jo1.getDouble("stars");
                appInfoList.add(info);
            }

            // 初始化轮播条的数据
            JSONArray ja1 = jo.getJSONArray("picture");
            picturesList = new ArrayList<String>();
            for (int i = 0; i < ja1.length(); i++) {
                String pic = ja1.getString(i);
                picturesList.add(pic);
            }
            return appInfoList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getPicturesList() {
        return picturesList;
    }
}
