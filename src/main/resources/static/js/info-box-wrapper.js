
//This is info_box_template1
function info_box_template1() {
  return new InfoBox({
    content: document.getElementById("infobox"),
    disableAutoPan: false,

    maxWidth: 150,
    maxHeight: 150,
    pixelOffset: new google.maps.Size(0, 0),
    zIndex: null,

    closeBoxMargin: "12px 4px 2px 2px",
    closeBoxURL: "",
    infoBoxClearance: new google.maps.Size(1, 1)
  });
}
