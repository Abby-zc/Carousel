package com.example.carousel;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LinearLayout carousel;
    private LinearLayout pointLinearLayout;
    //上一个指示点
    private int lastPosition = 0;
    private int[] bannerList = {
            R.mipmap.banner1,
            R.mipmap.banner2,
            R.mipmap.banner3,
            R.mipmap.banner2,
    };
    private ArrayList<String> list_title = new ArrayList<>();
    //存放指示点的集合
    private List<TextView> pointList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        carousel = findViewById(R.id.carousel);
        pointLinearLayout = findViewById(R.id.dots_list);
        lastPosition = 0;
        //放标题的集合
        list_title.add("新品震撼上市");
        list_title.add("时尚外观");
        list_title.add("智能省电，强劲续航");
        list_title.add("三色可选");
        addPoints();

        CustomCarousel customPager  = new CustomCarousel(MainActivity.this);
        carousel.addView(customPager);
        customPager.setImageUrls(bannerList,list_title);
        //设置当前选中页面
        customPager.setCurrentItem(0);
        //指示点跟随页面切换
        customPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % list_title.size();
                //切换指示点
                pointList.get(lastPosition).setBackgroundResource(R.drawable.indicate_dot);
                pointList.get(position).setBackgroundResource(R.drawable.indicate_dot_sel);
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //整个方法直接复制就可以
    private void addPoints() {
        pointList.clear();
        pointLinearLayout.removeAllViews();
        for (int x = 0; x < 4; x++) {
            TextView imageView = new TextView(MainActivity.this);
            if (x == 0) {
                imageView.setBackgroundResource(R.drawable.indicate_dot_sel);
            } else {
                imageView.setBackgroundResource(R.drawable.indicate_dot);
            }
            //导报的时候指示点的父View是什么布局就导什么布局的params,这里导的是线性布局
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            //设置间隔
            params.leftMargin = 26;
            //添加到线性布局;params指定布局参数，不然点就按在一起了
            pointLinearLayout.addView(imageView, params);
            //把指示点存放到集合中
            pointList.add(imageView);
        }
    }
}

