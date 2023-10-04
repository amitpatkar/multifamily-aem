"use strict";
use(function () {     
    var tourId = this.pageInfo.getPeekURL();
    //var tourId = null;   
    var tourProvider = null;   
    
    if (tourId !== null) {
        tourProvider = "peek";
    }
    
    if ( tourId === null && this.pageInfo.getUnitTypeImagesModel() !== null ) {
        tourId = this.pageInfo.getUnitTypeTourId();        
    }    
    
    if ( tourId === null && this.pageInfo.getUnitImagesModel() !== null ) {
        tourId = this.pageInfo.getUnitTourId();
    }
    
    if (tourId !== null && tourProvider === null && this.pageInfo.getPropertyConfigModel() !== null && this.pageInfo.getPropertyConfigModel().getTourProviderUnit() !== null) { //this is matterport or panoskin
        tourProvider = this.pageInfo.getPropertyConfigModel().getTourProviderUnit();
    }
    
    if (tourId === null && this.pageInfo.getPropertyConfigModel() !== null && this.pageInfo.getPropertyConfigModel().getMatterportID() !== null) {
        tourId= this.pageInfo.getPropertyConfigModel().getMatterportID();    
        tourProvider = "matterport";
    }
    
    if (tourId === null && this.pageInfo.getPropertyConfigModel() !== null && this.pageInfo.getPropertyConfigModel().getPanoskinTourID() !== null) {
        tourId= this.pageInfo.getPropertyConfigModel().getPanoskinTourID();   
        tourProvider = "panoskin";
    }

    if (tourId === null && this.pageInfo.getPropertyConfigModel() !== null && this.pageInfo.getPropertyConfigModel().getPeekPropertyTourURL() !== null) {
        tourId= this.pageInfo.getPropertyConfigModel().getPeekPropertyTourURL();
        tourProvider = "peek";
    }

    return {
        "tourId": tourId,
        "tourProvider": tourProvider
    }
});