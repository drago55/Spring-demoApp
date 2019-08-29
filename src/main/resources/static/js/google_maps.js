

var map;
var marker;
var markers = [];
var uniqueId = 1;
/*-----------------------------------------*/
/*Marker object*/
var markerObject ={
  name:'',
  city:'',
  postalCode:'',
  stateProvince:'',
  streetAddress:'',
  lat:null,
  lng:null,
  description:'',
  type:'',
  image:''

};
var defaultLocation={
  lat: 44.866,
  lng: 13.886

};

var mapOptions = {
  center: defaultLocation,
  zoom: 12

};


function initMap() {


  readValues();
  
  httpRequest("GET","/profile/mapOptions");
  
  
  map = new google.maps.Map(document.getElementById('map'), mapOptions);
  
  
  
  if (markerObject.lat!=null && markerObject.lng!=null) {
    addUpdateMarker();
  }


  map.addListener('click', function(event) {

    getLatLong(event.latLng);
    getAddress(event.latLng);
    addMarker(event.latLng);


  });
  map.addListener('rightclick', function(event) {

    removeMarkers(marker.id);
  });
  
  map.addListener('zoom_changed', function() {
	  document.getElementById("zoom").value =  map.getZoom();
   });



}
//This will fire after HTTP request
function callback(data){
	  //parsing raw json string and creating json objects
	let object=JSON.parse(data);
	 console.log("Map options  " + object);
	if (typeof object.mapOptions !== "undefined" && object.mapOptions !==null) {
	    //map options
	    newMapOptions(object.mapOptions);
	  }
}

function addUpdateMarker() {
  //create marker in update view and center to it
  var markerLocation = new google.maps.LatLng(markerObject.lat,markerObject.lng);
  addMarker(markerLocation);
  map.setCenter(markerLocation);

}


/*Read values from editMarker document readValues from inputs to set update marker on map*/
function readValues() {

  if (document.getElementById('addMarkerContainer')) {

    markerObject.name = document.getElementById('name').value;
    markerObject.streetAddress=document.getElementById('streetAddress').value;
    markerObject.stateProvince=document.getElementById('stateProvince').value;
    markerObject.city = document.getElementById('city').value;
    markerObject.lat = document.getElementById('lat').value;
    markerObject.lng = document.getElementById('lon').value;
    markerObject.description=document.getElementById('description').value;
    var e = document.getElementById("type");
    markerObject.type=e.options[e.selectedIndex].value;
    markerObject.image =document.getElementById("marker_icon").src;

  }

}

/*
*Lat and longitude from map
*/
function getLatLong(location) {
  // get lat/lon of click
  var clickLat = location.lat();
  var clickLon = location.lng();

  // show in input box
  document.getElementById("lat").value = clickLat.toFixed(5);
  document.getElementById("lon").value = clickLon.toFixed(5);
}

/*
*Getting address from latittude and longitude
*/
function getAddress(location) {
  //passing lat and long to geocoder
  var latlng = new google.maps.LatLng(location.lat(),location.lng());
  // This is making the Geocode request
  var geocoder = new google.maps.Geocoder();
  geocoder.geocode({ 'latLng': latlng }, function (results, status) {
    if (status !== google.maps.GeocoderStatus.OK) {
      alert(status);
    }
    // This is checking to see if the Geoeode Status is OK before proceeding
    if (status == google.maps.GeocoderStatus.OK) {



      var streetNumb = (results[0].address_components[0].long_name);
      var address = streetNumb + " " + (results[0].address_components[1].long_name);
      var postalCode = (results[0].address_components[6].long_name);
      var stateProvince = (results[0].address_components[5].long_name);
      var city= (results[0].address_components[2].long_name);
      //https://developers.google.com/maps/documentation/geocoding/intro
        document.getElementById("streetAddress").value = address;
        document.getElementById("city").value = city;
        document.getElementById("postalCode").value = postalCode;
        document.getElementById("stateProvince").value = stateProvince;



    }
  });
}


function addMarker(location) {

  marker = new google.maps.Marker({
    position:location,
    map: map,

  });
  markers.push(marker);
  marker.id=uniqueId;
  uniqueId++;
}

function removeMarkers(id){
  for(var i=0; i<markers.length; i++){
    markers[i].setMap(null);
    if (markers[i].id == id) {
      //Remove the marker from Map
      //Remove the marker from array.
      markers.splice(i, 1);
      return;
    }

  }
}
