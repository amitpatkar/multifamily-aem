use(function () {    
    var pageInfo  = this.pageInfo;
    var globalMenu = this.globalMenu;
    var menuLinksRight = globalMenu.getSocialLinks();
    var isCombinedHeader = false;
    var logo = null;
    if (pageInfo.getCollectionConfigModel() !== null) {
        menuLinksRight = pageInfo.getCollectionConfigModel().getMenuLinksRight();
        logo = (pageInfo.getCollectionConfigModel().getLogo() !== null ? pageInfo.getCollectionConfigModel().getLogo() : null);
        isCombinedHeader = true;
    }
    else if (pageInfo.getPropertyConfigModel() !== null) {
        menuLinksRight = pageInfo.getPropertyConfigModel().getMenuLinksRight();
        logo = (pageInfo.getPropertyConfigModel().getLogo() !== null ? pageInfo.getPropertyConfigModel().getLogo() : null);
        isCombinedHeader = true;
    }
    return {
        isCombinedHeader : isCombinedHeader,
        menuLinksRight: menuLinksRight,
        logo : logo
    };

});