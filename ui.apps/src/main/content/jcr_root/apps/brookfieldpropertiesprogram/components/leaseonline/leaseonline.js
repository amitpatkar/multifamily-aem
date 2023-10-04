"use strict";
use(function () {

    let queryParam = this.urlInfo.getRequest().getParameter("oneSiteID");
    let propConfig = this.pageInfo.getPropertyConfigModel();
    let modelParam = null;
    if (propConfig !== null) {
        modelParam = propConfig.getOnesiteID().split(',')[0].trim().split(':')[0].trim();
    }
    return {
        "oneSiteID": queryParam || modelParam
    }
});