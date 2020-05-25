package indi.toaok.pluto.core.dialog.animation.FlipExit;

import android.animation.ObjectAnimator;
import android.view.View;

import indi.toaok.pluto.core.dialog.animation.BaseAnimatorSet;

public class FlipVerticalExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(ObjectAnimator.ofFloat(view, "rotationX", 0, 90),//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0));
	}
}
