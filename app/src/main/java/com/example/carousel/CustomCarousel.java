package com.example.carousel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomCarousel extends ViewPager {

    //传递过来的图片数组，这个必须更换，真实项目中有可能是一个集合
    private int[] imageUrls;
    private ArrayList<String> list_title;
    private static final int NEXT=99;//切换下一张图片的标志
    private boolean isRunning=true;//是否自动轮播的标志，默认不自动轮播

    //    private BitmapUtils bitmapUtils;
    public CustomCarousel(Context context) {
        super(context);
    }

    public CustomCarousel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageUrls(int[] imageUrls,ArrayList<String> titleList) {
        this.imageUrls = imageUrls;
        this.list_title = titleList;
        setAdapter(new MyRollViePagerAdatper());
        startRoll();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case NEXT:
                    if(isRunning==true){
                        //设置当前item+1；相当于设置下一个item，然后余图片数量；
                        setCurrentItem(getCurrentItem()+1);
                        //然后发送空消息延时2秒
                        handler.sendEmptyMessageDelayed(NEXT,2000);
                    }
                    break;
            }
        }
    };

    //开始轮播
    public void startRoll(){
        //开启轮播
        if(isRunning) {
            //发送handler延时2秒
            handler.sendEmptyMessageDelayed(NEXT, 2000);
        }
    }

    class MyRollViePagerAdatper extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final View convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_banner, null);
            ImageView image = convertView.findViewById(R.id.image);
            TextView title = convertView.findViewById(R.id.title);
            image.setImageResource(imageUrls[position % 4]);
            title.setText(list_title.get(position % 4));
            container.addView(convertView);
            return convertView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }

    private int downTime=0;//按下时间
    //按下的XY坐标
    private int downX=0;
    private int downY=0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX= (int) ev.getX();
                downY= (int) ev.getY();
                downTime= (int) System.currentTimeMillis();
                //停止轮播
                isRunning=false;
                handler.removeMessages(NEXT);
                break;

            case MotionEvent.ACTION_UP:
                int upX= (int) ev.getX();
                int upY= (int) ev.getY();
                int disX= Math.abs(upX - downX);
                int disY= Math.abs(upY - downY);
                int upTime=(int) System.currentTimeMillis();
                if(upTime-downTime<500 && disX-disY<5 ){
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(getCurrentItem()%imageUrls.length);//当前位置就是显示的条目
                    }
                }
                isRunning=true;
                //开启轮播
                startRoll();
                break;

            case MotionEvent.ACTION_CANCEL:
                isRunning=true;
                startRoll();
                break;

            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    public boolean dispatchTouchEvent(MotionEvent ev){
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    //当控件挂载到页面上会调用此方法
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    //当控件从页面上移除的时候会调用此方法
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isRunning=false;
        handler.removeMessages(NEXT);
    }

    private CustomCarousel.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        public void onItemClick(int position);
    }

    public void setOnItemClickListener(CustomCarousel.OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

}
