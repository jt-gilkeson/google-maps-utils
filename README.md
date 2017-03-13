# google-maps-utils
Library of helpers for Google Maps on Android

### Installation
Add the following dependency to your build.gradle file
```xml
	repositories {
		maven { url "https://jitpack.io" }
	}
	
	dependencies {
		compile 'com.github.jt-gilkeson:google-maps-utils:1.0'
	}
```

## BlankInfoWindowAdapter

The BlankInfoWindowAdapter can be used to bring GoogleMap pins to the front (change z-index) when selected, without showing the info window above the pin.

### How to use it
You can set the GoogleMap to use BlankInfoWindowAdapter as the InfoWindowAdapter.  Once that is in place, you can simply call showInfoWindow on markers to bring them to the front.  

```java
// Set info window adapter for markers to the blank window adapter
mMap.setInfoWindowAdapter(new BlankInfoWindowAdapter(getLayoutInflater()));
		
...
		
// Select marker and bring to front
marker.showInfoWindow();
```

## MapStateListener

The MapStateListener can be used to receive callbacks from GoogleMaps on Android, when the map settles or unsettles, is touched or released. Settling the maps means that the map has finished scrolling, zooming or animating in any way and is not currently being touched.

### How to use it
You can use the MapStateListener by replacing your MapFragment with the included TouchableMapFragment, which allows for receiving touch events from the Map.
With a reference to your TouchableMapFragment and the GoogleMap-object in your Activity, you can simply use the MapStateListener like this:

```java
new MapStateListener(mMap, mMapFragment, this) 
{
  @Override
  public void onMapTouched() 
  {
    // Map touched
  }

  @Override
  public void onMapReleased() 
  {
    // Map released
  }

  @Override
  public void onMapUnsettled() 
  {
    // Map unsettled
  }

  @Override
  public void onMapSettled() 
  {
    // Map settled
  }
};
```

## MarkerSelectionAnimator

MarkerSelectionAnimator demonstrates how to animate a marker when it is selected despite there being no animation support in the Marker class.

### How to use it
You can use the MarkerSelectionAnimator by setting your GoogleMap MarkerClickListener to the one returned by getAnimatedMarkerClickListener().  After that your custom animation will run each time you click on a Marker.  This class is meant to demonstrate how to do animation, you can easily override any of the methods in the class to do more customized animation.

```java
MarkerSelectionAnimator mMarkerAnimator = new MarkerSelectionAnimator()
{
	@Override
	public void performAnimation(Marker marker, float t)
	{
	  // Do something with your marker based on the interpolated t value
	  // i.e. marker.setIcon, marker.setAlpha, marker.setPosition, etc.
	}

	@Override
	public void animationFinished(Marker marker)
	{
	  // Set the marker to the finished state
	}
};

mMap.setOnMarkerClickListener(mMarkerAnimator.getAnimatedMarkerClickListener())
```
