/*
 * Copyright (C) 2015 J.T. Gilkeson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jt.maputils;

import android.os.Handler;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

/**
 * MapStateListener can be used to receive callbacks from the GoogleMap inside a TouchableMapFragment
 * for when the map settles or unsettles, is touched or released.
 *
 * Settling the map means that the map has finished scrolling, zooming or animating in any way and
 * is not currently being touched.
 *
 * Forked from: https://github.com/madsbf/MapStateListener
 */
public abstract class MapStateListener
{
	private static final int SETTLE_TIME = 500;

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
