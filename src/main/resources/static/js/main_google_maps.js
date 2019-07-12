
//Global map variable
var map;
//Global marker
var marker;
//array of Markers
var markers=[];
//time to reset to center of map default 120sec
var time = 120000;
//default centar
var defaultCenter = {
  lat: 44.86971,
  lng: 13.84140
};
//map options
var mapOptions = {
  center: defaultCenter,
  zoom: 14,
};
var infobox;
var bounds;
var isInfoPoint=false;
var user_location=null;
var start_location=null;
var directionsService;
var directionsDisplay;
var locationError=false;
/*-------------------------------------------------------------------*/
/*Main function for initialize map*/
function initMap() {

  bounds= new google.maps.LatLngBounds();
  map = new google.maps.Map(document.getElementById('map2'),mapOptions);


  httpRequest("GET","/markers/get_json");
    initClickListeners();




  if (typeof info_point_uuid !== "undefined" && info_point_uuid!=='') {
    httpRequest("GET","/home/_getInfoPointOptions/" + info_point_uuid);
    isInfoPoint=true;
  }

  if (_isMobile() && navigator.geolocation && !isInfoPoint) {
    //if it is mobile and supports locations and is not info-point then get location from user
    locationListener();
  }
  //new direction object
  newDirections();
  directionsDisplay.addListener('directions_changed', function() {
    getDistance();
  });


  map.addListener('idle', function() {

    // 120 seconds after the center of the map has changed
    window.setTimeout(function() {

      map.fitBounds(bounds);
      map.panToBounds(bounds);
      if(directionsDisplay != null) {

        directionsDisplay.setMap(null);
        directionsDisplay.setPanel(null);

        document.getElementById("panel-wraper").style.display = 'none';
        newDirections();
      }
    }, time);
  });

}


/*
*Callback function to setup user location
*/
function setUserLocation(location) {
  user_location=location;
}
/*
* Get directions to the markers
*/
function getDirections() {
  var lat=document.getElementById('lat').value;
  var lng=document.getElementById('lng').value;
  var end_location = new google.maps.LatLng(lat,lng);

  if (locationError) {
    //clear error message
    x.innerHTML="";
    //if there was error locationListener call keep trying geting user locations
    locationListener();

  }
  //we can use infoPoint link on mobile phones
  if (!isInfoPoint && user_location!==null) {
    //get directions form user location
    document.getElementById("panel-wraper").style.display = 'block';
    displayRoute(user_location, end_location , directionsService, directionsDisplay);
  }else if (start_location!==null && isInfoPoint) {
    //get direction from infoPoint to marker
    document.getElementById("panel-wraper").style.display = 'block';
    displayRoute(start_location, end_location , directionsService, directionsDisplay);
  }else {
    //else display default directions
    document.getElementById("panel-wraper").style.display = 'block';
    displayRoute(defaultCenter, end_location , directionsService, directionsDisplay);
  }
}
/*
*Call back function to handle data
**/
function callback(data){
  //parsing raw json string and creating json objects

     object=JSON.parse(data);
     addMarkers(object);

  if (typeof object.markers !== "undefined" && object.markers!==null){
    //json object which holds all location of markers
     addMarkers(object.markers);


  }else if (typeof object.mapOptions !== "undefined" && object.mapOptions !==null) {
    //map options
    newMapOptions(object.mapOptions);
  }
}

function initClickListeners() {
  document.getElementById("right-panel").addEventListener("dblclick", clearAll);
}

function clearAll() {

  if(directionsDisplay != null ) {
    //clearing directions
    directionsDisplay.setMap(null);
    directionsDisplay.setPanel(null);
    document.getElementById("panel-wraper").style.display = 'none';
    //creating new directions object for next session
    newDirections();
    //stop watching user location
    stopWatching();

  }
}
//
function setInfoPoint(location) {
  marker = new google.maps.Marker({
    position:location,
    title: 'Your Location',
    map: map,
    icon:iconHere
  });
}

/*
*We are calling this function when we have set new map options.
*/
function newMapOptions(options) {

  start_location=new google.maps.LatLng(options[0].lat, options[0].lng);
  mapOptions.center= start_location;
  mapOptions.zoom= options[0].zoom;
  time=options[0].reset_time;

  setInfoPoint(start_location);
  bounds.extend(start_location);
  map.fitBounds(bounds);       // auto-zoom
  map.panToBounds(bounds);
}
//Add marker function object is json
function addMarkers(object) {

  var lat_lng;

  //loop true all json objects in array
  for (var i = 0;  i <object.length;  i++) {

  console.log("--------------Adding Markers----------------");

    console.log(url);
    var  data=object[i];
    console.log("--------------Data in add markers----------------------");
    console.log(data);
    console.log(data.lat);
    console.log(data.lon);

    //creating markers
    lat_lng=new google.maps.LatLng(data.lat, data.lon);

    marker=addMarker(lat_lng, data.markerName, map, icons[data.markerType]);
    //extending bounds of map we are making sure all markers fit in our map
     bounds.extend(lat_lng);

     console.log(marker==null);

     //creating info box

    infobox = info_box_template1();
    (function (marker ,data, infobox, lat_lng) {
      google.maps.event.addListener(marker, 'click', function( e) {

        //set animation
        marker.setAnimation(google.maps.Animation.BOUNCE);
        //set properties
        document.getElementById("lat").value=data.lat;
        document.getElementById("lng").value=data.lon;

         document.getElementById("name").innerHTML=data.markerName;
         document.getElementById("description").innerHTML=data.description;

        //document.getElementById("city").innerHTML=data.city;
        document.getElementById("address").innerHTML=data.city+" "+data.address+" "+data.postalCode+" "+data.state;
        //document.getElementById("state").innerHTML=data.state;
        //document.getElementById("postalCode").innerHTML=data.postalCode;
        document.getElementById("createdByUser").innerHTML=data.userFirstName;
        
        //set if not null
        if(typeof data.images !== "undefined" && data.images != null){
        	 document.getElementById("img-info-box").src=resource_url+data.images[0].imagePath;
             console.log("images "+resource_url+data.images[0].imagePath);
          
        }
        //on error load no image
        if(!document.getElementById("img-info-box").hasAttribute("src")) {
          noImage();
        };

        //setting infobox visible
        infobox.setVisible(true);
        //open infobox
         infobox.open(map, this);
      google.maps.event.addListener(map, 'click', function() {
                      //On map click close infobox null animation
                     infobox.setVisible(false);
                      marker.setAnimation(null);

                    });

      });
    })(marker, data, infobox, lat_lng);

  }

  if (!bounds.isEmpty()) {
    //there are marker defined bounds  zoom and pan to this bounds
    map.fitBounds(bounds);       // auto-zoom
    map.panToBounds(bounds);     // auto-center

  }

}


