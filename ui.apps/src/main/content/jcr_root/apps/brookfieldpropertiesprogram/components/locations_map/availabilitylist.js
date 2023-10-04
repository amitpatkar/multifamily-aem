"use strict";
use(function () {
    var rentStart = currentStyle["rentMin"] || 1000;
    var rentEnd = currentStyle["rentMax"] || 10000;
    var rentIncrementBy = currentStyle["rentIncrementBy"] || 1000;
    
    var rentOptList = new Array();
    for (var i=rentStart;i<=rentEnd;i+=rentIncrementBy) {
        rentOptList.push(i);
    }

    return {
        "rentOptList": rentOptList,
        "maxRent": rentEnd
    }
});