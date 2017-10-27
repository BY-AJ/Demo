package com.itcast.yb.googleplay.hodler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itcast.yb.googleplay.R;
import com.itcast.yb.googleplay.bean.SubjectInfo;
import com.itcast.yb.googleplay.http.HttpHelper;
import com.itcast.yb.googleplay.utils.BitmapHelper;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by yb on 2017/4/8.
 */

public class SubjectHodler extends BaseHodler<SubjectInfo>{
    private ImageView ivPics;
    private TextView tvTitle;
    private BitmapUtils mBitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_subject);
        ivPics = (ImageView) view.findViewById(R.id.iv_pic);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        mBitmapUtils = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(SubjectInfo data) {
        tvTitle.setText(data.des);
        mBitmapUtils.display(ivPics, HttpHelper.URL+"image?name="+data.url);
    }
}
