package com.jt.maputils;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchableWrapper extends FrameLayout
{
	public interface OnTouchListener
	{
		public void onTouch();
		public void onRelease();
	}

	public TouchableWrapper(Context context)
	{
		super(context);
	}

	public void setTouchListener(OnTouchListener onTouchListener)
	{
		this.onTouchListener = onTouchListener;
	}

	private OnTouchListener onTouchListener;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if (onTouchListener != null)
				{
					onTouchListener.onTouch();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (onTouchListener != null)
				{
					onTouchListener.onRelease();
				}
				break;
		}

		return super.dispatchTouchEvent(event);
	}
}
