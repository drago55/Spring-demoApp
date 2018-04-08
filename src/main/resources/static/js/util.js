/*function for calling controler to retrive data from database
data is returned over callback function */
function httpRequest (method,controler) {
  var httpRequest = new XMLHttpRequest;
  httpRequest.onreadystatechange = function(){
    if (httpRequest.readyState === 4) {// request is done
      if (httpRequest.status === 200) {// successfully
        callback(httpRequest.responseText);// we're calling our method
      }
    }
  };
  httpRequest.open(method, url+controler);
  httpRequest.send();
}
/*
*set no-image
*/
function noImage() {
  document.getElementById("img-info-box").src=url+"/image/no-image.png";
}
/*
*Detect mobile browsers
*/
function _isMobile(){
  // if we want a more complete list use this: http://detectmobilebrowsers.com/
  var isMobile = (/iphone|ipod|android|ie|blackberry|fennec/).test
  (navigator.userAgent.toLowerCase());
  return isMobile;
}

/*
*add marker to map
*/
function addMarker(location,title, map, icon) {

  marker = new google.maps.Marker({
    position:location,
    title:title,
    map:map,
    icon:icon
  });
  return marker;

}
