package com.jt.maputils;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

/**
 * This class demonstrates how a map marker can perform an animation when it is selected.
 */
public abstract class MarkerSelectionAnimator
{
	private final Handler mAnimationHandler;
	private Marker mSelectedMarker = null;

	public MarkerSelectionAnimator()
	{
		mAnimationHandler = new Handler();
	}

	/**
	 * Get a MarkerClickListener that calls our custom selectMarker method
	 *
	 * @return OnMarkerClickListener that calls selectMarker
	 */
	public OnMarkerClickListener getAnimatedMarkerClickListener()
	{
		return new OnMarkerClickListener()
		{
			@Override
			public boolean onMarkerClick(Marker marker)
			{
				selectMarker(marker);
				return false;
			}
		};
	}

	/**
	 * Select the new marker and perform animation if necessary
	 *
	 * @param marker The marker being selected
	 */
	public void selectMarker(Marker marker)
	{
		// Only perform the animation if the marker is changing
		if (!matchesCurrentMarker(marker))
		{
			animateMarker(marker);
			mSelectedMarker = marker;
		}
	}

	/**
	 * Check whether the marker passed in matches the currently selected marker
	 *
	 * @param marker marker to compare
	 * @return whether passed in marker matches currently selected marker
	 */
	public boolean matchesCurrentMarker(Marker marker)
	{
		return (mSelectedMarker != null && marker.equals(mSelectedMarker));
	}

	/**
	 * Set up handler to perform an animation and start the handler
	 *
	 * @param marker Marker to animate
	 */
	protected void animateMarker(final Marker marker)
	{
		final long duration = 250;
		final long interval = 25;

		final long start = SystemClock.uptimeMillis();
		final Interpolator interpolator = new LinearInterpolator();

		mAnimationHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed / duration);

				if (marker == null)
				{
					// if we are refreshing the map, don't try to animate
					return;
				}

				if (t < 1.0)
				{
					// perform animation
					performAnimation(marker, t);

					// Perform next step after interval.
					mAnimationHandler.postDelayed(this, interval);
				}
				else
				{
					// put marker in final state
					animationFinished(marker);
				}
			}
		});
	}

	public abstract void performAnimation(Marker marker, float t);

	public abstract void animationFinished(Marker marker);
}
