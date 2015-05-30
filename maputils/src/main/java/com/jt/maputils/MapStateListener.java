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

	public MapStateListener(GoogleMap map, TouchableMapFragment touchableMapFragment,
							boolean trackCameraChanges)
	{
		mMap = map;

		mHandler = new Handler();

		if (trackCameraChanges)
		{
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
		}

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

			@Override
			public void onMove()
			{
				onMapMoved();
			}
		});
	}

	/**
	 * Called when the user touches the map.
	 */
	public abstract void onMapTouched();

	/**
	 * Called when the user releases the map.
	 */
	public abstract void onMapReleased();

	/**
	 * Called a maximum of once per touch if the user has moved the map beyond the predefined threshold.
	 */
	public abstract void onMapMoved();

	/**
	 * Called when the map has stared moving.
	 */
	public abstract void onMapUnsettled();

	/**
	 * Called when the map stops animating for a predefined period of time.
	 */
	public abstract void onMapSettled();

	public void unsettleMap()
	{
		if (mMapSettled)
		{
			mHandler.removeCallbacks(mSettleMapTask);
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

	/**
	 * Force map into settle state without firing listener.
	 */
	public void resetSettle()
	{
		mMapSettled = true;
	}

	private void runSettleTimer()
	{
		mHandler.removeCallbacks(mSettleMapTask);
		mLastPosition = mMap.getCameraPosition();
		mHandler.postDelayed(mSettleMapTask, SETTLE_TIME);
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
			mHandler.removeCallbacks(mSettleMapTask);
			mMapTouched = true;
			onMapTouched();
		}
	}

	private Runnable mSettleMapTask = new Runnable()
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
}
