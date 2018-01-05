package com.nettactic.mall.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
/**
 * 渐变动画效果 工具类
 * @author shicai
 *
 */
public class AnimationUtil {
	
	private static AnimationUtil animationUtil;
	
	private AnimationUtil(){
		
	}
	
	public static AnimationUtil getIntence(){
		if (animationUtil==null) {
			animationUtil = new AnimationUtil();
		}
		return animationUtil;
		
	}
	
	
	public  AlphaAnimation animation;
	
	/**
	 * 淡入淡出开始动画
	 */
	public  void startAlphaAnimation(View view){
		if ( animation == null ) {
			// 创建一个AlphaAnimation对象  
			animation = new AlphaAnimation(0.01f, 1f);  
			// 设置动画执行的时间（单位：毫秒）  
			animation.setDuration(800);  
			// 设置重复次数 
//			animation.setRepeatCount(5);
		}
		// 把动画设置给控件
		view.setAnimation(animation);
		// 开始动画 
		animation.start();
	}
	
	/**
	 * 结束动画
	 */
	public  void cancelAlphaAnimation(){
		if ( animation!=null ) {
			animation.cancel();
		}
	}

}
