package com.itcast.yb.googleplay.bean;

import android.os.Environment;

import com.itcast.yb.googleplay.manage.DownLoadManager;

import java.io.File;

/**
 * 下载对象封装
 * Created by yb on 2017/4/21.
 */

public class DownloadInfo {

    public String id;
    public String name;
    public String packageName;
    public String downloadUrl;
    public long size;
    public int currentState;//当前状态
    public long currentPos;//当前位置
    public String path;

    public static final String GOOGLE_MARKET ="GOOGLE_MARKET";
    public static final String DOWNLOAD ="download";

    /**
     * 获取当前的进度
     */
    public float getProgress() {
        if(size ==0) {
            return 0;
        }
        float progress = currentPos / (float) size;
        return progress;
    }

    // 拷贝对象, 从AppInfo中拷贝出一个DownloadInfo
    public static DownloadInfo copy(AppInfo info) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.id=info.id;
        downloadInfo.name=info.name;
        downloadInfo.packageName = info.packageName;
        downloadInfo.downloadUrl = info.downloadUrl;
        downloadInfo.size = info.size;
        downloadInfo.currentState = DownLoadManager.STATE_UNDO;//默认状态
        downloadInfo.currentPos = 0;//默认从0开始下载
        downloadInfo.path = downloadInfo.getFilePath();

        return downloadInfo;
    }

    // 获取文件下载路径
    public String getFilePath() {
        StringBuffer sb = new StringBuffer();
        String sdcard = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        sb.append(sdcard);
        sb.append(File.separator);
        sb.append(GOOGLE_MARKET);
        sb.append(File.separator);
        sb.append(DOWNLOAD);
        if (createDir(sb.toString())) {
            // 文件夹存在或者已经创建完成
            return sb.toString() + File.separator + name + ".apk";// 返回文件路径
        }
        return null;
    }

    private boolean createDir(String dir) {
        File dirFile = new File(dir);
        // 文件夹不存在或者不是一个文件夹
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return dirFile.mkdirs();
        }
        return true;// 文件夹存在
    }
}
