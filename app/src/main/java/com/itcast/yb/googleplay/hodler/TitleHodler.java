package com.itcast.yb.googleplay.hodler;

import android.view.View;
import android.widget.TextView;

import com.itcast.yb.googleplay.R;
import com.itcast.yb.googleplay.bean.CategoryInfo;
import com.itcast.yb.googleplay.utils.UIUtils;

/**
 * Created by yb on 2017/4/9.
 */

public class TitleHodler extends BaseHodler<CategoryInfo>{

    private TextView tvName;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_title);
        tvName = (TextView) view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void refreshView(CategoryInfo data) {
        tvName.setText(data.title);
    }
}
