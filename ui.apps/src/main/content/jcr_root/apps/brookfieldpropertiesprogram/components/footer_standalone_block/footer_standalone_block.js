"use strict";
use(function () {     
    var url = this.url;
   
    if (url !== null && url.startsWith("/content") && !url.endsWith(".html") ) {
        url += ".html";
    }

    return {
        "url": url
    }
});