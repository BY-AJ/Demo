package com.itcast.yb.googleplay.fragment;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itcast.yb.googleplay.http.protocol.HotProtocol;
import com.itcast.yb.googleplay.utils.DrawableUtils;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.itcast.yb.googleplay.view.FlowLayout;
import com.itcast.yb.googleplay.view.LoadingPage;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yb on 2017/3/31.
 */

public class HotFragment extends BaseFragment{

    private ArrayList<String> data;

    @Override
    public View createSuccessView() {
        ScrollView scrollView = new ScrollView(UIUtils.getContext());
        FlowLayout flow = new FlowLayout(UIUtils.getContext());

        //设置内边距
        int padding = UIUtils.dip2px(10);
        flow.setPadding(padding,padding,padding,padding);
        flow.setHorizontalSpacing(UIUtils.dip2px(6));//设置水平间距
        flow.setVerticalSpacing(UIUtils.dip2px(8));//设置垂直间距

        for (int i=0; i<data.size(); i++) {
            final String keyword = data.get(i);
            TextView view = new TextView(UIUtils.getContext());
            view.setText(keyword);

            view.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);//字体大小18sp
            view.setTextColor(Color.WHITE); //字体颜色为白色
            view.setPadding(padding,padding,padding,padding);//设置内边距
            view.setGravity(Gravity.CENTER);//设置居中

            int color = 0xffcecece;// 按下后偏白的背景色
            //随机生成颜色
            Random random = new Random();
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);
            StateListDrawable selector = DrawableUtils.getSelector(Color.rgb(r, g, b), color, UIUtils.dip2px(6));
            //设置选择器
            view.setBackgroundDrawable(selector);

            flow.addView(view);

            // 只有设置点击事件, 状态选择器才起作用
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(),keyword,Toast.LENGTH_SHORT).show();
                }
            });
        }
        scrollView.addView(flow);
        return scrollView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        HotProtocol protocol = new HotProtocol();
        data = protocol.getData(0);
        return check(data);
    }
}
