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
