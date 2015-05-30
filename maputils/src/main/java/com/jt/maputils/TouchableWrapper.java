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

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Wrapper class that provides callbacks for Touch Events within a FrameLayout.
 * <p/>
 * Forked from: https://github.com/madsbf/MapStateListener
 */
public class TouchableWrapper extends FrameLayout
{
	public interface OnTouchListener
	{
		/**
		 * Called when the user touches the layout.
		 */
		void onTouch();

		/**
		 * Called when the user releases the layout.
		 */
		void onRelease();

		/**
		 * Called a maximum of once per touch if the user has touched and dragged their finger
		 * beyond the predefined threshold.
		 */
		void onMove();
	}

	// Movement trigger sensitivity
	private static final float MOVE_SLOP = 20;

	private boolean mMoveTriggered = false;
	private float   mDownX         = 0;
	private float   mDownY         = 0;

	private OnTouchListener mTouchListener;

	public TouchableWrapper(Context context)
	{
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		if (mTouchListener != null)
		{
			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					mMoveTriggered = false;
					mDownX = event.getX();
					mDownY = event.getY();
					mTouchListener.onTouch();
					break;

				case MotionEvent.ACTION_UP:
					mTouchListener.onRelease();
					break;

				case MotionEvent.ACTION_MOVE:
					// Once we've signalled a movement, don't send any more.
					if (!mMoveTriggered)
					{
						float currentX = event.getX();
						float currentY = event.getY();

						float deltaX = currentX - mDownX;
						float deltaY = currentY - mDownY;

						if ((Math.abs(deltaX) > MOVE_SLOP) || (Math.abs(deltaY) > MOVE_SLOP))
						{
							mTouchListener.onMove();
							mMoveTriggered = true;
						}
					}
					break;
			}
		}

		return super.dispatchTouchEvent(event);
	}

	public void setTouchListener(OnTouchListener onTouchListener)
	{
		this.mTouchListener = onTouchListener;
	}
}
