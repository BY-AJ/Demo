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
 * Created by yb on 2017/4/4.
 */

public class HomeHodler extends BaseHodler<AppInfo>{

    private BitmapUtils mBitmapUtils;

    private TextView tvName,tvSize,tvDes;
    private ImageView ivIcon;
    private RatingBar rbStats;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_home);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvSize = (TextView) view.findViewById(R.id.tv_size);
        tvDes = (TextView)view.findViewById(R.id.tv_des);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        rbStats = (RatingBar) view.findViewById(R.id.rb_stats);

        mBitmapUtils = BitmapHelper.getBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        tvName.setText(data.name);
        tvDes.setText(data.des);
        rbStats.setRating(data.stars);
        tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(),data.size));
        mBitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);
    }
}
