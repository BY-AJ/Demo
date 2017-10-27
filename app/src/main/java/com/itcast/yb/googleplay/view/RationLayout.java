package com.itcast.yb.googleplay.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.itcast.yb.googleplay.R;

/**
 * 自定义比例控件
 * Created by yb on 2017/4/8.
 */

public class RationLayout extends FrameLayout{

    private float ratio;

    public RationLayout(Context context) {
        super(context);
    }

    public RationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //第一种方法获取属性:float ratio = attrs.getAttributeFloatValue();

        //第二种方法获取属性
        // 当自定义属性时, 系统会自动生成属性相关id, 此id通过R.styleable来引用
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RationLayout);
        // id = 属性名_具体属性字段名称 (此id系统自动生成)
        ratio = typedArray.getFloat(R.styleable.RationLayout_ratio, -1);
        // 回收typearray, 提高性能
        typedArray.recycle();
    }

    public RationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 测量的时候，根据宽高比修改控件的高度
     *          1. 获取宽度
                2. 根据宽度和比例ratio, 计算控件的高度
                3. 重新测量控件
     * widthMeasureSpec,heightMeasureSpec这两个不是真正的宽高值，它们是由模式和真正的值组合在一起的
     * 三种模式:
     *      MeasureSpec.AT_MOST; 所有模式
            MeasureSpec.EXACTLY; 确定模式 相当于 match_parent
            MeasureSpec.UNSPECIFIED; 不确定模式 相当于wrap_content
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);//获取宽度值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);//获取宽度模式

        int height = MeasureSpec.getSize(heightMeasureSpec);//获取高度值
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);//获取高度模式

        //必须宽度模式是确定，高度模式是不确定，且比例大于0,这样才能
        if(widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio > 0) {
            //图片宽度 = 控件宽度 - 左侧内边距 - 右侧内边距
            int ImageWidth = width - getPaddingLeft() - getPaddingRight();//预防用户设置padding值,而影响图片的实际宽度

            //图片高度 = 图片宽度/宽高比例
            int ImageHeight = (int) (ImageWidth / ratio + 0.5f);

            //控件高度 = 图片高度 + 上侧内边距 + 下侧内边距
            height = ImageHeight + getPaddingTop() + getPaddingBottom();

            // 根据最新的高度来重新生成heightMeasureSpec(高度模式是确定模式)
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
