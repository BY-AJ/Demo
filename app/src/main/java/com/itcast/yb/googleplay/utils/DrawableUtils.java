package com.itcast.yb.googleplay.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by yb on 2017/4/8.
 */

public class DrawableUtils {

    //获取一个shape对象
    public static GradientDrawable getGradientDrawable(int color,int radius) {
        // xml中定义的shape标签 对应此类
        GradientDrawable shape = new GradientDrawable();

        shape.setShape(GradientDrawable.RECTANGLE);//设置矩形
        shape.setCornerRadius(radius);//设置圆角边距
        shape.setColor(color);//设置颜色
        return shape;
    }

    //获取一个选择器
    public static StateListDrawable getSelector(Drawable normal,Drawable press) {
        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed},press);//按下
        selector.addState(new int[]{},normal);//正常
        return selector;
    }

    //获取状态选择器
    public static StateListDrawable getSelector(int normal, int press, int radius) {
        GradientDrawable bgNormal = getGradientDrawable(normal, radius);
        GradientDrawable bgPress = getGradientDrawable(press, radius);
        StateListDrawable selector = getSelector(bgNormal, bgPress);
        return selector;
    }
}
