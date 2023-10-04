"use strict";
use(function () {
    if (this.amenities === null)  return {};
    return {
        "test":this.amenities,
        "amenitiesList": this.amenities.trim().split(/\s*[\r\n]+\s*/g)
    }
});