package com.ccdrive.peopledetail.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class AnimationTools {

	protected static int num = 0;

	public static Animation getMiniAnimation(int durationMillis) {
		Animation miniAnimation = new ScaleAnimation(1.15f, 1.0f, 1.15f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		miniAnimation.setDuration(durationMillis);
		miniAnimation.setFillAfter(true);
		return miniAnimation;
	}

	public static Animation getMaxAnimation(int durationMillis) {
		AnimationSet animationset = new AnimationSet(true);
		Animation maxAnimation = new ScaleAnimation(1.0f, 1.15f, 1.0f, 1.15f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animationset.addAnimation(maxAnimation);
		animationset.setDuration(durationMillis);
		animationset.setFillAfter(true);
		return animationset;
	}

	public static void setMenuAnimation(final ImageView imgView,
			final int imgID1, final int imgID2) {
		imgView.setImageResource(imgID1);
		AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.5f, 1.0f);
		localAlphaAnimation.setDuration(1500L); // 设置动画执行的持续时间
		localAlphaAnimation.setRepeatCount(-1); // -1 无限期的重复动画
		localAlphaAnimation
				.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						AnimationTools.num++;
						if (AnimationTools.num == 20) {
							AnimationTools.num = 0;
						}
						if (AnimationTools.num % 2 == 0) {
							imgView.setImageResource(imgID1);
							return;
						}
						imgView.setImageResource(imgID2);
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub

					}
				});
		imgView.startAnimation(localAlphaAnimation);
	}

	public static AnimationSet alphaAndTrans() {
		AnimationSet animationset = new AnimationSet(true);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(50);
		animationset.addAnimation(alphaAnimation);
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		translateAnimation.setDuration(100);
		animationset.addAnimation(translateAnimation);
		return animationset;
	}
}
