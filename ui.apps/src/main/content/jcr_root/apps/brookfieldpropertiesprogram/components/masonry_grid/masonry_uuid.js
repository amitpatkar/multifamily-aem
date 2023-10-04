use(function () {    
    var specials = this.specials;
    if (specials != null) {
        specials = specials.replace('cmp-apartmentDetail__promo-headline',"interactive-title");
        specials = specials.replace('cmp-apartmentDetail__promo-subhead',"small-title");
        specials = specials.replace('cmp-apartmentDetail__promo-highlight',"legal-text");
    }
    return {
        uuid: java.util.UUID.randomUUID().toString(),
        "specials":specials
    };

});