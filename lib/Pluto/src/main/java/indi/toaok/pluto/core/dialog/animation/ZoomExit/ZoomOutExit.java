package indi.toaok.pluto.core.dialog.animation.ZoomExit;

import android.animation.ObjectAnimator;
import android.view.View;

import indi.toaok.pluto.core.dialog.animation.BaseAnimatorSet;

public class ZoomOutExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0, 0),//
				ObjectAnimator.ofFloat(view, "scaleX", 1, 0.3f, 0),//
				ObjectAnimator.ofFloat(view, "scaleY", 1, 0.3f, 0));//
	}
}
