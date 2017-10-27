package com.itcast.yb.googleplay.manage;

import android.content.Intent;
import android.net.Uri;

import com.itcast.yb.googleplay.bean.AppInfo;
import com.itcast.yb.googleplay.bean.DownloadInfo;
import com.itcast.yb.googleplay.http.HttpHelper;
import com.itcast.yb.googleplay.utils.IOUtils;
import com.itcast.yb.googleplay.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 下载管理器
 *
 * DownLoadManager就是被观察者，有责任通知所有观察者状态和进度发生变化
 * Created by yb on 2017/4/21.
 */

public class DownLoadManager {
    /**
     * 下载任务时的几种状态:
     * 未下载，等待下载，正在下载，暂停下载，下载失败，下载成功
     */
    public static final int STATE_UNDO = 1;
    public static final int STATE_WAITTING = 2;
    public static final int STATE_LOADING = 3;
    public static final int STATE_PAUSE= 4;
    public static final int STATE_ERROR= 5;
    public static final int STATE_SUCCESS= 6;

    // 下载对象的集合,使用线程安全的HashMap使用线程安全的HashMap
    private ConcurrentHashMap<String,DownloadInfo> mDownloadInfoMap = new ConcurrentHashMap<String,DownloadInfo>();
    // 下载任务的集合
    private ConcurrentHashMap<String,DownLoadManager.DownloadTask> mDownloadTaskMap = new ConcurrentHashMap<String,DownLoadManager.DownloadTask>();

    /*********管理器一般做单例模式，本次使用的饿汉模式*********/
    private static DownLoadManager mDM = new DownLoadManager();
    private DownLoadManager() {
    }
    public static DownLoadManager getInstance() {
        return mDM;
    }
    /*********************************************************/

    /************************************************************
     * 观察者模式设计步骤:
     * 1.定义一个观察者接口
     * 2.需要一个注册观察者的方法
     * 3.需要一个注销观察者的方法
     * 4.需要一个集合去维护观察者对象，进行观察者的注册或者注销
     * 5.需要被观察者去通知观察者，发生了那些变化的方法
     *************************************************************/
    /**观察者集合*/
    private ArrayList<DownloadObserver>  mObservers = new ArrayList<DownloadObserver>();

    /**定义观察者接口*/
    public interface DownloadObserver{
        /*** 下载状态发生改变*/
        public void onDownloadStateChanged(DownloadInfo info);
        /*** 下载进度条发生改变*/
        public void onDownloadProgressChanged(DownloadInfo info);
    }

    /**注册观察者*/
    public void registerObserver(DownloadObserver observer) {
        if(observer!= null && !mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }
    /**注销观察者*/
    public void unregisterObserver(DownloadObserver observer) {
        if(observer!=null && mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }
    /**通知下载状态发生改变*/
    public void notifyDownloadStateChanged(DownloadInfo info) {
        for (DownloadObserver observer : mObservers) {
            observer.onDownloadStateChanged(info);
        }
    }
    /**通知下载进度发生改变*/
    public void notifyDownloadProgressChanged(DownloadInfo info) {
        for (DownloadObserver observer : mObservers) {
            observer.onDownloadProgressChanged(info);
        }
    }
    /**************************************************************************/

   /**
     * 开始下载
     */
    public synchronized void download(AppInfo info) {
        // 如果对象是第一次下载, 需要创建一个新的DownloadInfo对象,从头下载
        // 如果之前下载过, 要接着下载,实现断点续传
        DownloadInfo downloadInfo = mDownloadInfoMap.get(info.id);
        if(downloadInfo == null) {
            // 生成一个下载的对象
            downloadInfo = DownloadInfo.copy(info);
        }
        //状态需要发生改变
        downloadInfo.currentState = STATE_WAITTING;
        //通知观察者状态发生改变
        notifyDownloadStateChanged(downloadInfo);
        // 将下载对象放入集合中
        mDownloadInfoMap.put(downloadInfo.id,downloadInfo);

        // 初始化下载任务, 并放入线程池中运行
        DownloadTask task = new DownloadTask(downloadInfo);
        ThreadManager.getThreadPool().execute(task);
        // 将下载任务放入集合中
        mDownloadTaskMap.put(downloadInfo.id,task);
    }

    //下载对象
    class DownloadTask implements Runnable {
        private DownloadInfo downloadInfo;
        public DownloadTask(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }
        @Override
        public void run() {
            System.out.println(downloadInfo.name + "开始下载啦");
            downloadInfo.currentState = STATE_LOADING;//状态切换为开始下载
            notifyDownloadStateChanged(downloadInfo);//通知大家状态发生改变

            File file = new File(downloadInfo.path);
            HttpHelper.HttpResult httpResult;
            //当文件名不存在，或者文件长度不等于要下载的大小，或者当前位置是0
            if(!file.exists() || file.length()!=downloadInfo.currentPos ||
                    downloadInfo.currentPos == 0) {
                file.delete();//删除无效文件
                downloadInfo.currentPos = 0;//从0开始下载
                //开始从头下载
                httpResult = HttpHelper.download(HttpHelper.URL +
                        "download?name=" + downloadInfo.downloadUrl);
            }else {
                //开始断点续传,range 表示请求服务器从文件的哪个位置开始返回数据
                httpResult=HttpHelper.download(HttpHelper.URL + "download?name="
                        + downloadInfo.downloadUrl+"range="+file.length());
            }

            if(httpResult !=null && httpResult.getInputStream() != null) {
                InputStream in = httpResult.getInputStream();
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file,true);//需要追加在文件
                    int len = 0;
                    byte[] buffer = new byte[1024];
                    while ((len=in.read(buffer))!= -1 && downloadInfo.currentState == STATE_LOADING) {
                        out.write(buffer,0,len);
                        // 把剩余数据刷入本地
                        out.flush();
                        // 更新下载进度
                        downloadInfo.currentPos+=len;
                        notifyDownloadProgressChanged(downloadInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.close(in);
                    IOUtils.close(out);
                }
                //文件下载结束
                if(file.length() == downloadInfo.size) {
                    //文件下载完整
                    downloadInfo.currentState = STATE_SUCCESS;
                    notifyDownloadStateChanged(downloadInfo);
                }else if (downloadInfo.currentState == STATE_PAUSE) {
                    //文件暂停下载
                    notifyDownloadStateChanged(downloadInfo);
                }else {
                    //文件下载失败
                    file.delete();
                    downloadInfo.currentState = STATE_ERROR;
                    downloadInfo.currentPos = 0;
                    notifyDownloadStateChanged(downloadInfo);
                }
            }else {
                //网络异常
                file.delete();
                downloadInfo.currentState = STATE_ERROR;
                downloadInfo.currentPos = 0;
                notifyDownloadStateChanged(downloadInfo);
            }

            // 从集合中移除下载任务
            mDownloadTaskMap.remove(downloadInfo.id);
        }
    }

    /**
     * 暂停下载
     */
    public synchronized void pause(AppInfo info) {
        // 取出下载对象
        DownloadInfo downloadInfo = mDownloadInfoMap.get(info.id);
        if(downloadInfo != null) {
            // 只有在正在下载和等待下载时才需要暂停
            if(downloadInfo.currentState == STATE_WAITTING || downloadInfo.currentState == STATE_LOADING) {
                downloadInfo.currentState = STATE_PAUSE;//状态需要切换为暂停;
                notifyDownloadStateChanged(downloadInfo);//通知观察者状态发生改变

                DownloadTask task = mDownloadTaskMap.get(downloadInfo.id);
                if(task != null) {
                    // 移除下载任务, 如果任务还没开始,正在等待, 可以通过此方法移除
                    // 如果任务已经开始运行, 需要在run方法里面进行中断
                    ThreadManager.getThreadPool().cancel(task);
                }
            }
        }
    }

    /**
     * 开始安装
     */
    public synchronized void install(AppInfo info) {
        DownloadInfo downloadInfo = mDownloadInfoMap.get(info.id);
        if (downloadInfo != null) {
            // 跳到系统的安装页面进行安装
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
                    "application/vnd.android.package-archive");
            UIUtils.getContext().startActivity(intent);
        }
    }

    // 根据应用信息返回下载对象
    public DownloadInfo getDownloadInfo(AppInfo info) {
        return mDownloadInfoMap.get(info.id);
    }

}
