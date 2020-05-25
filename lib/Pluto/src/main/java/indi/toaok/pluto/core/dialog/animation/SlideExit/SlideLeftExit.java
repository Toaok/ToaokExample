package indi.toaok.pluto.core.dialog.animation.SlideExit;

import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.view.View;

import indi.toaok.pluto.core.dialog.animation.BaseAnimatorSet;

public class SlideLeftExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "translationX", 0, -250 * dm.density), //
				ObjectAnimator.ofFloat(view, "alpha", 1, 0));
	}
}