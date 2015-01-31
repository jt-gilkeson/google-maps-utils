package com.jt.maputils;

import android.os.Handler;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

public abstract class MapStateListener
{
	private static final int SETTLE_TIME = 250;

	private boolean mMapTouched = false;
	private boolean mMapSettled = false;

	private final Handler mHandler;

	private final GoogleMap      mMap;
	private       CameraPosition mLastPosition;

	private Runnable settleMapTask = new Runnable()
	{
		@Override
		public void run()
		{
			CameraPosition currentPosition = mMap.getCameraPosition();

			if (currentPosition.equals(mLastPosition))
			{
				settleMap();
			}
			else
			{
				runSettleTimer();
			}
		}
	};

	public MapStateListener(GoogleMap map, TouchableMapFragment touchableMapFragment)
	{
		mMap = map;

		mHandler = new Handler();

		map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener()
		{
			@Override
			public void onCameraChange(CameraPosition cameraPosition)
			{
				unsettleMap();
				if (!mMapTouched)
				{
					runSettleTimer();
				}
			}
		});

		touchableMapFragment.setTouchListener(new TouchableWrapper.OnTouchListener()
		{
			@Override
			public void onTouch()
			{
				touchMap();
				unsettleMap();
			}

			@Override
			public void onRelease()
			{
				releaseMap();
				runSettleTimer();
			}
		});
	}

	private void runSettleTimer()
	{
		mHandler.removeCallbacks(settleMapTask);
		mLastPosition = mMap.getCameraPosition();
		mHandler.postDelayed(settleMapTask, SETTLE_TIME);
	}

	private synchronized void releaseMap()
	{
		if (mMapTouched)
		{
			mMapTouched = false;
			onMapReleased();
		}
	}

	private void touchMap()
	{
		if (!mMapTouched)
		{
			mHandler.removeCallbacks(settleMapTask);
			mMapTouched = true;
			onMapTouched();
		}
	}

	public void unsettleMap()
	{
		if (mMapSettled)
		{
			mHandler.removeCallbacks(settleMapTask);
			mMapSettled = false;
			mLastPosition = null;
			onMapUnsettled();
		}
	}

	public void settleMap()
	{
		if (!mMapSettled)
		{
			mMapSettled = true;
			onMapSettled();
		}
	}

	public abstract void onMapTouched();

	public abstract void onMapReleased();

	public abstract void onMapUnsettled();

	public abstract void onMapSettled();
}
