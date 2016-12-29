## Goal

Create an application to show user current location and places around current location. 
The address can be either the user’s current location or an address he searches for.

Extra: show place details on marker tooltip click.

## Libraries used:
* Google APIs for map, places and geocoding.
* Gson for JSON manipulations
* POJOs from Google Maps Services Java client
* Retrofit for networking
* Dagger2 for dependency injection
* OkHttp logging interceptor for logging

## Possible improvements (what I would do if I had more time)

* Prettify UI
* Add tests
* Add error processing
* Add screen rotation support
* Add location update instead of using last known location
* Fix map marker blinking on places update
* Show custom marker icons
* Add a way to select radius for place search (currently it is set to 1 km)
* Highlight matches in autocomplete drop-down
* Split logic in MainActivity to different components (map, autocomplete text view, place details)
* Learn how to use RxJava and use it here
