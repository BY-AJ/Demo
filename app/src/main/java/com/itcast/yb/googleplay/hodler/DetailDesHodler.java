package com.itcast.yb.googleplay.hodler;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itcast.yb.googleplay.R;
import com.itcast.yb.googleplay.bean.AppInfo;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by yb on 2017/4/14.
 */

public class DetailDesHodler extends BaseHodler<AppInfo>{

    private TextView tvDes;
    private RelativeLayout rlRoots;
    private TextView tvAuthor;
    private ImageView ivArrow;
    private LinearLayout.LayoutParams mParams;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_desinfo);
        tvDes = (TextView) view.findViewById(R.id.tv_detail_des);
        rlRoots = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);
        tvAuthor = (TextView) view.findViewById(R.id.tv_detail_author);
        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);

        rlRoots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    private boolean isOpen = false ;

    /**
     * 开关，切换状态
     */
    private void toggle() {
        int shortHeight = getShortHeight();
        int longHeight = getLongHeight();
        ValueAnimator animator = null ;

        if(isOpen) {
            isOpen = false;
            if(shortHeight < longHeight) {
                animator = ValueAnimator.ofInt(longHeight,shortHeight);
            }
        }else {
            isOpen = true;
            if(shortHeight < longHeight) {
                animator = ValueAnimator.ofInt(shortHeight,longHeight);
            }
        }

        if(animator != null) {
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Integer height = (Integer) valueAnimator.getAnimatedValue();
                    mParams.height = height;
                    tvDes.setLayoutParams(mParams);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    final ScrollView scrollView = getScrollView();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            // 滚动到底部
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    if (isOpen) {
                        ivArrow.setImageResource(R.drawable.arrow_up);
                    } else {
                        ivArrow.setImageResource(R.drawable.arrow_down);
                    }
                }
                @Override
                public void onAnimationCancel(Animator animator) {
                }
                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            animator.setDuration(200);
            animator.start();
        }
    }

    @Override
    public void refreshView(AppInfo data) {
        tvAuthor.setText(data.author);
        tvDes.setText(data.des);

        // 放在消息队列中运行, 解决当只有三行描述时也是7行高度的bug
        tvDes.post(new Runnable() {
            @Override
            public void run() {
                mParams = (LinearLayout.LayoutParams) tvDes.getLayoutParams();
                mParams.height = getShortHeight();
                tvDes.setLayoutParams(mParams);
            }
        });
    }

    /**
     * 获取一个TextView的最短高度
     */
    private int getShortHeight() {
        //获取真实的textview的宽度
        int width = tvDes.getMeasuredWidth();
        //模拟一个textview
        TextView view = new TextView(UIUtils.getContext());
        // 设置文字
        view.setText(getData().des);
        //大小要保持一致
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        //设置最大行数为7行
        view.setMaxLines(7);
        //获取模式+宽度真实值的一个值，宽度的模式是确定的,match_parent
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        //获取模式+高度真实值的一个值，size=2000,表示最大的高度值，高度的模式是AT_MOST,wrap_content
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.AT_MOST);
        view.measure(widthMeasureSpec,heightMeasureSpec);

        return view.getMeasuredHeight();
    }

    /**
     * 获取一个TextView的最长高度
     */
    private int getLongHeight() {
        //获取真实的textview的宽度
        int width = tvDes.getMeasuredWidth();
        //模拟一个textview
        TextView view = new TextView(UIUtils.getContext());
        // 设置文字
        view.setText(getData().des);
        //大小要保持一致
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        //获取模式+宽度真实值的一个值，宽度的模式是确定的,match_parent
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        //获取模式+高度真实值的一个值，size=2000,表示最大的高度值，高度的模式是AT_MOST,wrap_content
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.AT_MOST);
        view.measure(widthMeasureSpec,heightMeasureSpec);

        return view.getMeasuredHeight();
    }

    // 获取ScrollView, 一层一层往上找,
    // 知道找到ScrollView后才返回;注意:一定要保证父控件或祖宗控件有ScrollView,否则死循环
    private ScrollView getScrollView() {
        ViewParent parent = tvDes.getParent();
        while (!(parent instanceof ScrollView)) {
            parent = parent.getParent();
        }
        return (ScrollView)parent;
    }
}
