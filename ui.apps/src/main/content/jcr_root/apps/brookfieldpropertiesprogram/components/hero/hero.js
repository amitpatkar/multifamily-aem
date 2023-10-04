"use strict";
use(function () {     
    var virtualTourType = properties["virtualTourType"];  
    var isVirtualTour =  ( virtualTourType == null || virtualTourType === "" ? false :true); //properties["isVirtualTour"];    
    var title = this.title;
    var tourId = null;    
    var bgImagePath = this.bgImagePath;
    var bgImageLabel = properties["bgImageLabel"];
    var peekURL = this.pageInfo.getPeekURL();
    
    var isPlayInPlace = (properties["isPlayInPlace"] === "true" ? true : false);

    
    var isVideo =  false;

//    log.info(bgImagePath);
    //check if its a video
    if (bgImagePath !== "" && new String(bgImagePath).endsWith(".mp4")) {
        isVideo = true;
    }
    
    var data_cmp_class = null;
    if (isVirtualTour) {
        data_cmp_class = "herovirtualtour";
    }
    else if (isVideo) {
        data_cmp_class = "herovideo";
    }
                
    if ( this.pageInfo.getUnitTypeImagesModel() !== null ) {
        title = this.pageInfo.getUnitTypeImagesModel().getUnitTypeName();
        tourId = this.pageInfo.getUnitTypeTourId();
        bgImagePath = this.pageInfo.getUnitTypeImagesModel().getHeroImagePath() != null ? this.pageInfo.getUnitTypeImagesModel().getHeroImagePath() : bgImagePath;
        bgImageLabel = this.pageInfo.getUnitTypeImagesModel().getHeroImagePath() != null ? title : bgImageLabel;
        if (tourId === null && peekURL === null) isVirtualTour = false; 
    }    
    else if ( this.pageInfo.getUnitImagesModel() !== null ) {
        title = this.pageInfo.getUnitImagesModel().getUnitTypeName() + " " + this.pageInfo.getUnitImagesModel().getUnitName();
        tourId = this.pageInfo.getUnitTourId();
        bgImagePath = this.pageInfo.getUnitImagesModel().getHeroImagePath() != null ? this.pageInfo.getUnitImagesModel().getHeroImagePath() : bgImagePath;
        bgImageLabel = this.pageInfo.getUnitImagesModel().getHeroImagePath() != null ? title : bgImageLabel;
        if (tourId === null && peekURL === null) isVirtualTour = false; 
    }
    /*
    if (tourId === null && this.pageInfo.getPropertyConfigModel() !== null && this.pageInfo.getPropertyConfigModel().getTourIdForProperty() !== null) {
        tourId= this.pageInfo.getPropertyConfigModel().getTourIdForProperty();        
    }     
    */

    if (properties["virtualTourType"] == "tourId" && properties["tourId"] !== null && properties["tourId"] !== "") {
        tourId = properties["tourId"];
    }

    return {
        "showImage": (!isPlayInPlace && (bgImagePath !== "") && !isVideo),
        "showVideo": (!isPlayInPlace && (bgImagePath !== "") && isVideo),
        "title":title,
        "tourId": tourId,
        "isVirtualTour":isVirtualTour,
        "bgImagePath":bgImagePath,
        "bgImageLabel":bgImageLabel,
        "showVideoControls": isVideo ? true : false,
        "data_cmp_class" : data_cmp_class,
        "peekURL": peekURL
    }
});