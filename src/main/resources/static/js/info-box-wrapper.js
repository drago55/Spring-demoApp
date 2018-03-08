
//This is info_box_template1
function info_box_template1() {
  return new InfoBox({
    content: document.getElementById("infobox"),
    disableAutoPan: false,

    maxWidth: 250,
    maxHeight:150,
    pixelOffset: new google.maps.Size(25, -70),
    zIndex: null,

    closeBoxMargin: "12px 4px 2px 2px",
    closeBoxURL: "",
    infoBoxClearance: new google.maps.Size(0.1, 0.1)
  });
}
