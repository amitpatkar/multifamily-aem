// Get the modal
var modal = document.getElementById('schedule-tour-modal');

// Get the button that opens the modal
var btnByClass = document.getElementsByClassName("schedule-tour-popup-button");

var openScheduleTourPopup = function () {
    var pageURL = document.location.href;
    if (pageURL.indexOf("#")) {
        pageURL = pageURL.substring(0, pageURL.indexOf("#"));
    }
    var strIframe = document.getElementById("schedule-tour-modal").querySelectorAll("iframe");
    var strIframeUrl = strIframe[0].src;
    var arrIframeUrl = strIframeUrl.split('scheduletourwidget');
    var SourceId = '';
    var results = new RegExp('[\?&]' + 'sourceid' + '=([^&#]*)').exec(pageURL);
    if (results != null) {
        SourceId = decodeURI(results[1]) || 0;
    }
    if ('' != SourceId) {
        strIframeUrl = arrIframeUrl[0] + 'scheduletourwidget' + '/?tracker=yes&sourceId=' + SourceId;
        strIframe[0].src = strIframeUrl;

        strIframe[0].style.display = "none";
        strIframe[0].onload = function () {
            strIframe[0].style.display = "block";
        }
    }
    strIframe[0].style.background = "#FFFFFF";
    modal.style.display = "block";
}

if (window.location.href.indexOf("#schedule-tour-popup-button") != -1) {
    openScheduleTourPopup();
}

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("schedule_popup_close")[0];

// When the user clicks the button, open the modal 
for (var i = 0, len = btnByClass.length | 0; i < len; i = i + 1 | 0) {
    btnByClass[i].onclick = openScheduleTourPopup;
}



// When the user clicks on <span> (x), close the modal
span.onclick = function () {
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
