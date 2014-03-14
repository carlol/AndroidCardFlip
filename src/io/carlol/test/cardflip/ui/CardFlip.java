package io.carlol.test.cardflip.ui;

import io.carlol.test.cardflip.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

/**
 * 
 * @author c.luchessa
 *
 */
public class CardFlip extends RelativeLayout {

	private static final int DEFAULTA_ANIMATION_DURATION = 500;
	
	private int mFrontViewId;
	private int mBackViewId;
	private int mAnimationDuration;
	
	private ViewGroup mFrontView;
	private ViewGroup mBackView;
	
	private boolean isAnimPerforming = false;
	

	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if ( ! isAnimPerforming ) {
				flipCard();
			}
		}
	};
	
	private AnimationListener mAnimationListener = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
			isAnimPerforming = true;
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) { /* ignored */ }
		
		@Override
		public void onAnimationEnd(Animation animation) {
			isAnimPerforming = false;
		}
	};



	public CardFlip(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CardFlip);

		mFrontViewId = a.getResourceId(R.styleable.CardFlip_cf_front, 0);
		mBackViewId = a.getResourceId(R.styleable.CardFlip_cf_back, 0);
		mAnimationDuration = a.getInt(R.styleable.CardFlip_cf_animationDuration, DEFAULTA_ANIMATION_DURATION);

		RuntimeException e = null;
		if (mFrontViewId == 0) {
			e = new IllegalArgumentException(a.getPositionDescription() + 
					": The front attribute is required and must refer to a valid child.");
		}
		if (mBackViewId == 0) {
			e = new IllegalArgumentException(a.getPositionDescription() + 
					": The back attribute is required and must refer to a valid child.");
		}
		a.recycle();
		if (e != null) throw e;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mFrontView = (ViewGroup) this.findViewById(mFrontViewId);
		mBackView = (ViewGroup) this.findViewById(mBackViewId);

		mFrontView.setOnClickListener(mOnClickListener);
		mBackView.setOnClickListener(mOnClickListener);
		
		this.setBackgroundColor(getResources().getColor(android.R.color.transparent));
	}

	private void flipCard() {

		FlipAnimation flipAnimation = new FlipAnimation(mFrontView, mBackView, mAnimationDuration);
		
		flipAnimation.setAnimationListener(mAnimationListener);

		if (mFrontView.getVisibility() == View.GONE) {
			flipAnimation.reverse();
		}
		this.startAnimation(flipAnimation);
	}


	/**
	 *
	 * @link http://stackoverflow.com/questions/16030667/displaying-card-flip-animation-on-old-android
	 *
	 */
	public static class FlipAnimation extends Animation
	{
		private Camera camera;

		private View fromView;
		private View toView;

		private float centerX;
		private float centerY;

		private boolean forward = true;

		/**
		 * Creates a 3D flip animation between two views.
		 *
		 * @param fromView First view in the transition.
		 * @param toView Second view in the transition.
		 * @param mAnimationDuration 
		 */
		public FlipAnimation(View fromView, View toView, int mAnimationDuration)
		{
			this.fromView = fromView;
			this.toView = toView;

			setDuration(mAnimationDuration);
			setFillAfter(false);
			setInterpolator(new AccelerateDecelerateInterpolator());
		}

		public void reverse()
		{
			forward = false;
			View switchView = toView;
			toView = fromView;
			fromView = switchView;
		}

		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight)
		{
			super.initialize(width, height, parentWidth, parentHeight);
			centerX = width/2;
			centerY = height/2;
			camera = new Camera();
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t)
		{
			// Angle around the y-axis of the rotation at the given time
			// calculated both in radians and degrees.
			final double radians = Math.PI * interpolatedTime;
			float degrees = (float) (180.0 * radians / Math.PI);

			// Once we reach the midpoint in the animation, we need to hide the
			// source view and show the destination view. We also need to change
			// the angle by 180 degrees so that the destination does not come in
			// flipped around
			if (interpolatedTime >= 0.5f)
			{
				degrees -= 180.f;
				fromView.setVisibility(View.GONE);
				toView.setVisibility(View.VISIBLE);
			}

			if (forward)
				degrees = -degrees; //determines direction of rotation when flip begins

			final Matrix matrix = t.getMatrix();
			camera.save();
			camera.rotateY(degrees);
			camera.getMatrix(matrix);
			camera.restore();
			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);
		}
	}
	
}
