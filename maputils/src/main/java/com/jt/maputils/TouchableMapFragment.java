package com.jt.maputils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

public class TouchableMapFragment extends MapFragment
{
	private View             mOriginalContentView;
	private TouchableWrapper mTouchView;

	public static TouchableMapFragment newInstance()
	{
		return new TouchableMapFragment();
	}

	public void setTouchListener(TouchableWrapper.OnTouchListener onTouchListener)
	{
		mTouchView.setTouchListener(onTouchListener);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
	{
		mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);

		mTouchView = new TouchableWrapper(getActivity());
		mTouchView.addView(mOriginalContentView);

		return mTouchView;
	}

	@Override
	public View getView()
	{
		return mOriginalContentView;
	}
}
