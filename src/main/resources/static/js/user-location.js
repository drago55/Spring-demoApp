/*
* Starting location listener
*/
var watchID;
//var geolocation;
var startPos;
var geoOptions = {
  enableHighAccuracy: true,
  maximumAge        : 0,
  timeout           : 10000
}
var x = document.getElementById("location_error");
function locationListener() {

  var geoSuccess = function(position) {
    // Do magic with location
    startPos = position;

    var geolocation =new google.maps.LatLng(startPos.coords.latitude,startPos.coords.longitude);

    var circle = new google.maps.Circle({
      center: geolocation,
      map:map,
      fillColor:'#99ff66',
      fillOpacity: '0.1',
      strokeColor: '#99ff66',
      strokeOpacity:'0.01',
      radius: position.coords.accuracy
    });

    bounds.extend(geolocation);
    setUserLocation(geolocation);

  };
  var geoError = function(error) {
    locationError=true;
    //console.log('Error occurred. Error code: ' + error.code);
    // error.code can be:
    switch(error.code) {
      case error.TIMEOUT:
      //   3: timed out
      x.innerHTML = "The request to get user location timed out.";
      //  console.log("An TIMEOUT error occurred.");
      break;
      case error.PERMISSION_DENIED:
      //   1: permission denied
      x.innerHTML = "Default location is set please enable your location.";
      //  console.log("User denied the request for Geolocation.");
      break;
      case error.POSITION_UNAVAILABLE:
      //   2: position unavailable (error response from location provider)
      x.innerHTML = "Location information is unavailable.";
      //  console.log("Location information is unavailable.");
      break;
      case error.UNKNOWN_ERROR:
      //   0: unknown error
      //alert('Error can\'t find your location default location will be set');
      x.innerHTML = "An unknown error occurred.";
      //  console.log("An unknown error occurred.");
      break;

    }
  };

  if( navigator.geolocation) {
    watchID=navigator.geolocation.watchPosition(geoSuccess, geoError,geoOptions);
  } else {
    geoError();
  }


}

function stopWatching() {
  if (watchID != null) {
    navigator.geolocation.clearWatch(watchID);
    watchID = null;
  }
}
