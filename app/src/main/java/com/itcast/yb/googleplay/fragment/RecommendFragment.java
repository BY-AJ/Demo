package com.itcast.yb.googleplay.fragment;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itcast.yb.googleplay.http.protocol.RecommendProtocol;
import com.itcast.yb.googleplay.utils.UIUtils;
import com.itcast.yb.googleplay.view.LoadingPage;
import com.itcast.yb.googleplay.view.fly.ShakeListener;
import com.itcast.yb.googleplay.view.fly.StellarMap;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yb on 2017/3/31.
 */

public class RecommendFragment extends BaseFragment{

    private ArrayList<String> data;

    @Override
    public View createSuccessView() {
        final StellarMap stellar = new StellarMap(UIUtils.getContext());
        stellar.setAdapter(new RecommendAdapter());
        // 随机方式, 将控件划分为9行6列的的格子, 然后在格子中随机展示
        stellar.setRegularity(6, 9);

        // 设置内边距10dp
        int padding = UIUtils.dip2px(10);
        stellar.setInnerPadding(padding, padding, padding, padding);

        // 设置默认页面, 第一组数据
        stellar.setGroup(0, true);

        ShakeListener shake = new ShakeListener(UIUtils.getContext());
        shake.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                stellar.zoomIn();//调到下一页
            }
        });

        return stellar;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        RecommendProtocol protocol = new RecommendProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class RecommendAdapter implements StellarMap.Adapter {

        //获取组的个数
        @Override
        public int getGroupCount() {
            return 2;
        }

        //返回每个组里面多少个成员
        @Override
        public int getCount(int group) {
            int count = data.size() / getGroupCount();
            if(group == getGroupCount() - 1) {
                count += data.size() % getGroupCount();
            }
            return count;
        }

        //初始化布局
        @Override
        public View getView(int group, int position, View convertView) {
            // 因为position每组都会从0开始计数, 所以需要将前面几组数据的个数加起来,才能确定当前组获取数据的角标位置
            position += (group) * getCount(group -1);

            TextView view = new TextView(UIUtils.getContext());

            final String keyword = data.get(position);
            view.setText(keyword);

            Random rand = new Random();
            //随机大小;范围在16-25
            int size = 16 + rand.nextInt(10);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);

            //随机颜色,彩色(30-230)
            int r = 30 + rand.nextInt(200);
            int g = 30 + rand.nextInt(200);
            int b = 30 + rand.nextInt(200);
            view.setTextColor(Color.rgb(r,g,b));

            //设置文本点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(),keyword,Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        // 返回下一组的id
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            if (isZoomIn) {
                // 往下滑加载上一页
                if (group > 0) {
                    group--;
                } else {
                    // 跳到最后一页
                    group = getGroupCount() - 1;
                }
            } else {
                // 往上滑加载下一页
                if (group < getGroupCount() - 1) {
                    group++;
                } else {
                    // 跳到第一页
                    group = 0;
                }
            }
            return group;
        }
    }
}
