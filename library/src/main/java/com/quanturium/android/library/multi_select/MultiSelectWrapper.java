package com.quanturium.android.library.multi_select;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.quanturium.android.library.bottomsheetpicker.R;

/**
 * Wrapper FrameLayout over any type of views to add the multiSelect feature.
 */
public class MultiSelectWrapper<V extends View> extends FrameLayout {

	public final static int DEFAULT_CHECK_MARK_SIZE = 20;
	private final static float DEFAULT_SCALE_SELECTED = 0.8f;
	private static final float TILE_SCALE_NOT_SELECTED = 1f;

	private final V internalView;
	private View selectionMarkView;

	private boolean isSelectable;
	private boolean isSelected;
	private Drawable checkedDrawable;
	private Drawable uncheckedDrawable;
	private int selectionDrawableSize = 0;
	private int leftOffsetInPixels = 0;
	private int topOffsetInPixels = 0;
	private float selectedScale;

	public static <V extends View> MultiSelectWrapper wrap(V view) {
		return new MultiSelectWrapper<>(view);
	}

	private MultiSelectWrapper(V view) {
		super(view.getContext());
		this.internalView = view;
		init();
	}

	private void init() {

		selectionMarkView = new View(getContext());
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		selectionMarkView.setLayoutParams(params);
		selectionMarkView.setBackground(getUncheckedDrawable());
		selectionMarkView.setAlpha(0f);

		addView(internalView);
		addView(selectionMarkView);

		final float scale = getResources().getDisplayMetrics().density;
		setDrawableSize((int) (scale * DEFAULT_CHECK_MARK_SIZE));
		setSelectedScale(DEFAULT_SCALE_SELECTED);

		ViewCompat.setElevation(selectionMarkView, 3f * scale);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

		final int count = getChildCount();

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			if (child == internalView) {
				super.onLayout(changed, left, top, right, bottom);
			} else if (child == selectionMarkView) {
				positionSelectionMarkView(child, internalView, left, top, right, bottom);
			} else {
				throw new IllegalStateException("MultiSelectWrapper can have only one child view to wrap");
			}
		}
	}

	private void positionSelectionMarkView(View selectionView, View internalView, int left, int top, int right, int bottom) {

		int internalViewWidth = internalView.getMeasuredWidth();
		int internalViewHeight = internalView.getMeasuredHeight();

		int internalViewSelectedWidth = (int) (internalViewWidth * selectedScale);
		int internalViewSelectedHeight = (int) (internalViewHeight * selectedScale);

		int selectionMarkViewCenterX = leftOffsetInPixels + (internalViewWidth - internalViewSelectedWidth) / 2;
		int selectionMarkViewCenterY = topOffsetInPixels + (internalViewHeight - internalViewSelectedHeight) / 2;

		int halfSelectionMarkViewSize = selectionDrawableSize / 2;

		selectionView.layout(selectionMarkViewCenterX - halfSelectionMarkViewSize, selectionMarkViewCenterY - halfSelectionMarkViewSize, selectionMarkViewCenterX + halfSelectionMarkViewSize, selectionMarkViewCenterY + halfSelectionMarkViewSize);
	}

	/**
	 * Set the offset from top and left corners for the check / uncheck view based on the scaled
	 * view after middle-centering it to the top left corner
	 *
	 * @param leftOffsetInPixels left offset in pixels
	 * @param topOffsetInPixels top offset in pixels
	 */
	public void setDrawableOffset(int leftOffsetInPixels, int topOffsetInPixels) {
		this.leftOffsetInPixels = leftOffsetInPixels;
		this.topOffsetInPixels = topOffsetInPixels;
		requestLayout();
		invalidate();
	}

	public void setSelectedScale(float scale) {
		this.selectedScale = scale;
		requestLayout();
		invalidate();
	}

	/**
	 * Set the offset for the check / uncheck view
	 *
	 * @param widthAndHeightInPixels value in pixels. Default is 20dp
	 */
	public void setDrawableSize(int widthAndHeightInPixels) {
		selectionDrawableSize = widthAndHeightInPixels;
		LayoutParams layoutParams = (LayoutParams) selectionMarkView.getLayoutParams();
		layoutParams.width = widthAndHeightInPixels;
		layoutParams.height = widthAndHeightInPixels;
		requestLayout();
		invalidate();
	}

	/**
	 * Set a custom checked drawable
	 *
	 * @param d a Drawable
	 */
	public void setCheckedDrawable(Drawable d) {
		checkedDrawable = d;
		invalidate();
	}

	/**
	 * Set a custom unchecked drawable
	 *
	 * @param d a Drawable
	 */
	public void setUncheckedDrawable(Drawable d) {
		uncheckedDrawable = d;
		invalidate();
	}

	/**
	 * Get the current checked drawable
	 *
	 * @return The current checked drawable
	 */
	public Drawable getCheckedDrawable() {
		if (checkedDrawable == null) {
			checkedDrawable = getDefaultCheckedDrawable();
		}

		return checkedDrawable;
	}

	/**
	 * Get the current unchecked drawable
	 *
	 * @return The current unchecked drawable
	 */
	public Drawable getUncheckedDrawable() {
		if (uncheckedDrawable == null) {
			uncheckedDrawable = getDefaultUncheckedDrawable();
		}

		return uncheckedDrawable;
	}

	private Drawable getDefaultCheckedDrawable() {

		// Get accent color
		TypedValue typedValue = new TypedValue();
		Resources.Theme theme = getContext().getTheme();
		theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
		int colorAccent = typedValue.data;

		LayerDrawable multiselectCheckedDrawable = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.ic_multiselect_checked);
		Drawable d = multiselectCheckedDrawable.findDrawableByLayerId(R.id.icon);
		DrawableCompat.setTint(d, colorAccent);
		return multiselectCheckedDrawable;
	}

	private Drawable getDefaultUncheckedDrawable() {
		return ContextCompat.getDrawable(getContext(), R.drawable.ic_multiselect_unchecked);
	}

	/**
	 * Set the view as selectable or not. An unchecked mark on the top left corner will appear if selectable
	 *
	 * @param isSelectable whether is it selectable
	 * @param animated animated or not
	 */
	public void setSelectable(boolean isSelectable, boolean animated) {
		if (isSelectable != getSelectable()) {
			setSelectableInternal(isSelectable);
			if (animated) {
				animateSelectableView(isSelectable);
				return;
			}
		}

		// Default behavior
		selectionMarkView.animate().cancel();
		selectionMarkView.setAlpha(isSelectable ? 1f : 0f);
	}

	private void setSelectableInternal(boolean isSelectable) {
		this.isSelectable = isSelectable;
	}

	private boolean getSelectable() {
		return isSelectable;
	}

	/**
	 * Set the view as selected or not. A checked mark on the top left corner will appear if selected.
	 *
	 * @param isSelected whether is it selected
	 * @param animated animated or not
	 */
	public void setSelected(boolean isSelected, boolean animated) {
		if (isSelected != getSelected()) {
			setSelectedInternal(isSelected);

			if (animated) {
				animateTileZoomAnimation(isSelected);
				animateMultiselectCheckedAnimation(isSelected);
				return;
			}
		}

		// Default behavior
		internalView.setScaleX(isSelected ? selectedScale : TILE_SCALE_NOT_SELECTED);
		internalView.setScaleY(isSelected ? selectedScale : TILE_SCALE_NOT_SELECTED);
		selectionMarkView.setBackground(isSelected ? getCheckedDrawable() : getUncheckedDrawable());
	}

	private void setSelectedInternal(boolean isSelected) {
		this.isSelected = isSelected;
	}

	private boolean getSelected() {
		return isSelected;
	}

	private void animateSelectableView(boolean isSelectable) {
		selectionMarkView.animate()
				.alpha(isSelectable ? 1f : 0f)
				.setInterpolator(new FastOutSlowInInterpolator())
				.setDuration(200)
				.start();
	}

	private void animateTileZoomAnimation(boolean isSelected) {
		this.internalView.animate()
				.scaleX(isSelected ? selectedScale : TILE_SCALE_NOT_SELECTED)
				.scaleY(isSelected ? selectedScale : TILE_SCALE_NOT_SELECTED)
				.setInterpolator(new FastOutSlowInInterpolator())
				.setDuration(200)
				.start();
	}

	private void animateMultiselectCheckedAnimation(boolean isSelected) {
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 0);
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1, 0);
		ObjectAnimator scaleAnimation = ObjectAnimator.ofPropertyValuesHolder(this.selectionMarkView, pvhX, pvhY);
		scaleAnimation.setDuration(100);
		scaleAnimation.setRepeatCount(1);
		scaleAnimation.setRepeatMode(ValueAnimator.REVERSE);
		scaleAnimation.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationRepeat(Animator animation) {
				MultiSelectWrapper.this.selectionMarkView.setBackground(isSelected ? getCheckedDrawable() : getUncheckedDrawable());
			}
		});
		scaleAnimation.start();
	}

	/**
	 * Returns the underlying view that was wrapper with this class
	 *
	 * @return The underlying view
	 */
	public V getInternalView() {
		return internalView;
	}
}
