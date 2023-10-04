$(document).ready(function () {
    var colorCode = "lightblue";
    var fontColor = null;
    var isLocal = (window.location.href.indexOf("localhost") != -1);
    var isDev = (window.location.href.indexOf("https://author-p42404-e166196.adobeaemcloud.com/") != -1);
    var isQA = (window.location.href.indexOf("https://author-p42404-e166723.adobeaemcloud.com/") != -1);
    var isStage = (window.location.href.indexOf("https://author-p42404-e166724.adobeaemcloud.com/") != -1);
    var isProd = (window.location.href.indexOf("https://author-p42404-e166759.adobeaemcloud.com/") != -1);
    if (isDev) { //dev -- 
        colorCode = "lightblue";
    } else if (isQA) { //qa
        colorCode = "lightgreen";
    } else if (isStage) { //stage -- green
        colorCode = "lightyellow";
    } else if (isProd || isLocal) { //prod -- red 
        colorCode = "red";
        fontColor="white";
    }
    $("coral-actionbar").css("background-color", colorCode);
    $("coral-shell-header").css("background-color", colorCode)
    if (fontColor !== null) {
        $("coral-actionbar ._coral-Icon").css("color", fontColor)
        $("coral-actionbar ._coral-ActionButton-label").css("color", fontColor)
        $("coral-actionbar .editor-GlobalBar-pageTitle").css("color", fontColor)
    }
});

