package com.jt.maputils;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * BlankInfoWindowAdapter is used to bring a selected Marker to the front without showing an info window.
 */
public class BlankInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
{
	private final View mBlankInfoWindow;

	public BlankInfoWindowAdapter(LayoutInflater inflater)
	{
		mBlankInfoWindow = inflater.inflate(R.layout.no_info_window, null);
	}

	// Hack to prevent info window from displaying: use a 0dp/0dp frame
	@Override
	public View getInfoWindow(Marker marker)
	{
		return mBlankInfoWindow;
	}

	@Override
	public View getInfoContents(Marker marker)
	{
		return null;
	}
}
