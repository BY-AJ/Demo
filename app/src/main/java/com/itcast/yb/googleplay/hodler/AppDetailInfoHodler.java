package com.itcast.yb.googleplay.hodler;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itcast.yb.googleplay.R;
import com.itcast.yb.googleplay.bean.AppInfo;
import com.itcast.yb.googleplay.http.HttpHelper;
import com.itcast.yb.googleplay.utils.BitmapHelper;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by yb on 2017/4/12.
 */

public class AppDetailInfoHodler extends BaseHodler<AppInfo>{

    private ImageView ivIcon;
    private TextView tvName;
    private RatingBar rbStar;
    private TextView tvDownNum;
    private TextView tvVersion;
    private TextView tvDate;
    private TextView tvSize;
    private BitmapUtils mBitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_appinfo);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        rbStar = (RatingBar) view.findViewById(R.id.rb_star);
        tvDownNum = (TextView) view.findViewById(R.id.tv_download_num);
        tvVersion = (TextView) view.findViewById(R.id.tv_version);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvSize = (TextView) view.findViewById(R.id.tv_size);

        mBitmapUtils = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        mBitmapUtils.display(ivIcon, HttpHelper.URL+"image?name="+data.iconUrl);
        tvName.setText(data.name);
        rbStar.setRating(data.stars);
        tvDownNum.setText("下载量:"+data.downloadNum);
        tvVersion.setText("版本:"+data.version);
        tvDate.setText(data.date);
        tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(),data.size));
    }
}
