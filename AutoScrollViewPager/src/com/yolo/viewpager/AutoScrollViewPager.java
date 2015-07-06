package com.yolo.viewpager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
/**
 * 自动滚动ViewPager.
 * 提供给外界如下方法：
 * 1.setOnPositionChange.设置监听，提供给外界当前页面求模运算后的索引值，注意不是position值
 * 2.setGoToLeft 向左滚动（默认方向，可以不设置）
 * 3.setGoToRight 向右滚动
 * 4.setInterval 设置滚动一次时间间隔，默认3000毫秒
 * 5.stopAutoScroll 停止自动滚动
 * 6.startAutoScroll 开始自动滚动
 * @author jiax-a
 *
 */
public class AutoScrollViewPager extends ViewPager{

	/**
	 * false 表示向右滚动
	 */
	private boolean isGoToRight = false;
	private boolean isAutoScroll = false;
	/**
	 * 判断是否一开始启用过自动滚动
	 */
	private boolean isAbel = false;
	private Context context;
	private int count;//默认pager数
	private ImageView[] mImageViews;
	private onPositionChange listener;
	private int currentPosition;
	private long delay = 4000;
	private ScheduledExecutorService scheduledExecutor;
	private List<String> paths = new ArrayList<String>();
	private List<String> p;//临时保存原路径数据
	public AutoScrollViewPager(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public AutoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	
	
	private void init() {
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
		options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)			// 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.ic_launcher)	// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.ic_launcher)		// 设置图片加载或解码过程中发生错误显示的图片	
			.bitmapConfig(Bitmap.Config.RGB_565)		// 默认是ARGB_8888， 使用RGB_565会比使用ARGB_8888少消耗2倍的内存
			.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
			.build();		
	}
	public void setData(List<String> paths){
		this.p = paths;
		if(paths.size()<=1){
			return;
		}else if(paths.size() == 2 || paths.size() == 3){
			this.paths.addAll(paths);
			this.paths.addAll(paths);
		}else{
			this.paths.addAll(paths);
		}
		count = this.paths.size();
		mImageViews = new ImageView[count];  
        for(int i=0; i<count; i++){  
            ImageView imageView = new ImageView(context); 
            imageView.setScaleType(ScaleType.FIT_XY);
            //可以修改默认的图片
//            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.banner));
            mImageViews[i] = imageView;  
        }  
        myPagerAdapter = new MyPagerAdapter();
        setAdapter(myPagerAdapter);
        /** 
         * 2147483647 / 2 = 1073741820 - 1  
         * 设置ViewPager的当前项为一个比较大的数，以便一开始就可以左右循环滑动 
         */  
        int n = Integer.MAX_VALUE / 2 % count;  
        currentPosition = Integer.MAX_VALUE / 2 - n;  
        setCurrentItem(currentPosition);
        
        setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	
	private class MyPagerAdapter extends PagerAdapter{
		int index = 0;
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager)container).removeView(mImageViews[position % count]); 
		
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			if(paths != null && paths.size()!=0){
				String url = paths.get(position%count);
				ImageLoader.getInstance().displayImage(url, mImageViews[position % count],options);
			}
			((ViewPager)container).addView(mImageViews[position % count]);  
			
			mImageViews[position % count].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (clicklistener !=null) {
						if(p.size() == 2 || p.size() == 3){
							index = position%(count/2);
						}else{
							index = position%count;
						}
						clicklistener.onItemClick(index);
					}
				}
			});
			
			return mImageViews[position % count];  
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
	}
	/**
	 * 页面索引值监听器
	 * @author jiax-a
	 *
	 */
	private class MyOnPageChangeListener implements OnPageChangeListener{
		int index = 0;
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int position) {
			currentPosition = position;
			if(p.size() == 2 || p.size() == 3){
				index = position%(count/2);
			}else{
				index = position%count;
			}
			listener.posChange(index);
		}
	}
	
    private OnItemClicklistener clicklistener;
	
	public void setOnItemClicklistener(OnItemClicklistener clicklistener) {
		this.clicklistener = clicklistener;
	}

	public interface OnItemClicklistener{
		public void onItemClick(int position);
	}
	
	
	
	/**
	 * 设置ViewPager索引的回调，在setData之前调用
	 * @param listener
	 */
	public void setOnPositionChange(onPositionChange listener){
		this.listener = listener;
	}
	
	
	public interface onPositionChange{
		/**
		 * @param index 索引值
		 */
		public void posChange(int index);
	}
	/**
	 * 向左自动滚动(默认向左滚动，可以不设置)
	 */
	public void setGoToLeft(){
		this.isGoToRight = true;
	}
	/**
	 * 向右自动滚动(默认向左滚动)
	 */
	public void setGoToRight(){
		this.isGoToRight = false;
	}
	/**
	 * 设置滚动的时间间隔,单位毫秒。默认是3000毫秒
	 * @param delay
	 */
	public void setInterval(long delay){
		this.delay = delay;
	}
	/**
	 * 开启
	 * @param delay
	 */
	public void startAutoScroll(){
		if(paths.size()<=1){
			return;
		}
		isAutoScroll = true;
		scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutor.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				if(isGoToRight){
					currentPosition++;
				}else{
					currentPosition--;
				}
				
				handler.sendEmptyMessage(0);
			}
		}, delay, delay, TimeUnit.MILLISECONDS);
	}
	public void stopAutoScroll(){
		isAbel = true;
		isAutoScroll = false;
		//停止切换
		if(scheduledExecutor!=null)
			scheduledExecutor.shutdown();
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			setCurrentItem(currentPosition);
		}
	};
	private MyPagerAdapter myPagerAdapter;
	private DisplayImageOptions options;
	
	public boolean dispatchTouchEvent(android.view.MotionEvent ev) {
		int action = MotionEventCompat.getActionMasked(ev);
		if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
			stopAutoScroll();
		} else if (ev.getAction() == MotionEvent.ACTION_UP) {
			if(isAbel){
				startAutoScroll();
			}
        }
		
		return super.dispatchTouchEvent(ev);
	};
	
}
