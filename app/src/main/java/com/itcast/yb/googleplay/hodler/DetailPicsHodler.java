package com.itcast.yb.googleplay.hodler;

import android.view.View;
import android.widget.ImageView;

import com.itcast.yb.googleplay.R;
import com.itcast.yb.googleplay.bean.AppInfo;
import com.itcast.yb.googleplay.http.HttpHelper;
import com.itcast.yb.googleplay.utils.BitmapHelper;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by yb on 2017/4/14.
 */

public class DetailPicsHodler extends BaseHodler<AppInfo>{
    private ImageView[] mPics;
    private BitmapUtils mBitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_picinfo);
        mPics = new ImageView[5];
        mPics[0] = (ImageView) view.findViewById(R.id.iv_pic1);
        mPics[1] = (ImageView) view.findViewById(R.id.iv_pic2);
        mPics[2] = (ImageView) view.findViewById(R.id.iv_pic3);
        mPics[3] = (ImageView) view.findViewById(R.id.iv_pic4);
        mPics[4] = (ImageView) view.findViewById(R.id.iv_pic5);

        mBitmapUtils = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        ArrayList<String> screen = data.screen;
        for (int i=0; i<5; i++) {
            if(i < screen.size()) {
                mBitmapUtils.display(mPics[i], HttpHelper.URL+"image?name="+screen.get(i));
            }else {
                mPics[i].setVisibility(View.GONE);
            }
        }
    }
}
