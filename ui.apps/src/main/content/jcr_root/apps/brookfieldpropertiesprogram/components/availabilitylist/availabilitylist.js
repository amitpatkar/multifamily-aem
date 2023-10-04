"use strict";
use(function () {
    var rentStart = currentStyle["rentMin"];
    var rentEnd = currentStyle["rentMax"];
    var rentIncrementBy = currentStyle["rentIncrementBy"];
    var isCollection = false;
    if (this.pageInfo.getCollectionConfigModel() !== null) {
        isCollection = true;
    }    
    var rentOptList = new Array();
    for (var i=rentStart;i<=rentEnd;i+=rentIncrementBy) {
        rentOptList.push(i);
    }

    return {
        "rentOptList": rentOptList,
        "isCollection":isCollection
    }
});