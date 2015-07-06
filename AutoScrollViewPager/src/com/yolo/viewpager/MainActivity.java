package com.yolo.viewpager;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.yolo.viewpager.AutoScrollViewPager.OnItemClicklistener;
import com.yolo.viewpager.AutoScrollViewPager.onPositionChange;

public class MainActivity extends Activity{

	private AutoScrollViewPager viewpager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		viewpager = (AutoScrollViewPager) findViewById(R.id.viewpager);
		viewpager.setGoToLeft();
		ArrayList<String> paths = new ArrayList<String>();
		paths.add("http://a.hiphotos.baidu.com/image/pic/item/aa18972bd40735fa569c4bb29c510fb30f240848.jpg");
		paths.add("http://b.hiphotos.baidu.com/image/pic/item/342ac65c10385343ca299d2a9113b07ecb8088d2.jpg");
		paths.add("http://c.hiphotos.baidu.com/image/pic/item/5bafa40f4bfbfbed69062c747af0f736afc31f59.jpg");
		viewpager.setData(paths);
		
		
		viewpager.setOnPositionChange(new onPositionChange() {
			
			@Override
			public void posChange(int index) {
				//TODO 可以根据index的回调处理指示器的显示
//				Toast.makeText(MainActivity.this, ""+index, Toast.LENGTH_SHORT).show();				
			}
		});
		viewpager.setOnItemClicklistener(new OnItemClicklistener() {
			
			@Override
			public void onItemClick(int position) {
				//TODO 处理每个Item的点击事件
				Toast.makeText(MainActivity.this, "点击"+position, Toast.LENGTH_SHORT).show();				
			}

		});
	}
	@Override
	protected void onResume() {
		viewpager.startAutoScroll();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		viewpager.stopAutoScroll();
		super.onPause();
	}
}
