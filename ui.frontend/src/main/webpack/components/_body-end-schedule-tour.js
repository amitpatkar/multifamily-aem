
function toCamelCase(name) {
    const parts = name.split('_');
    return parts[0] + parts.slice(1).map(part => part.charAt(0).toUpperCase() + part.slice(1));
}
 window.openScheduleTourPopup = function () {
        var pageURL = document.location.href;
        if (pageURL.indexOf("#")) {
            pageURL = pageURL.substring(0, pageURL.indexOf("#"));
        }
        //var strIframe = document.getElementById("schedule-tour-modal").querySelectorAll("iframe");
        var embedHolder = document.getElementById('embedHolder');
        if (embedHolder && (!embedHolder.innerHTML || embedHolder.innerHTML.indexOf("iframe")  === -1)) { 
            embedHolder.innerHTML = "<iframe id='ifrm' src='" + scheduleATourUrl + "' style='border:0;' frameborder='0' />";        
        }
        var modal = document.getElementById('schedule-tour-modal');
        modal.style.display = "block";
    }
function getProductArr() {
    const productArr = [];
    if (digitalData.propertyInfo) {
        const siteData = {
            "productInfo": {
                "name": digitalData.propertyInfo.propertyName,
                "productID": digitalData.propertyInfo.onesiteID
            },
            "location": {
                "locationId": digitalData.propertyInfo.onesiteID,
                "locationName": digitalData.propertyInfo.propertyName
            }
        };
        if (digitalData.propertyInfo.unit || digitalData.propertyInfo.group) {
            const unit = digitalData.propertyInfo.unit || digitalData.propertyInfo.group.units[0];
            siteData.floorPlan = {
                "floorPlanId": unit.floorPlan.floorPlanID,
                "floorPlanName": unit.floorPlan.floorPlanName
            }
        }
        productArr.push(siteData)
    }
    if (digitalData.collectionInfo) {
        productArr.push({
            "collection": {
                "collectionId": digitalData.collectionInfo.id,
                "collectionName": digitalData.collectionInfo.name
            },
            "location": {
                "locationId": digitalData.collectionInfo.id,
                "locationName": digitalData.collectionInfo.name
            }
        })
    }

    return productArr
}

function collectDataLayerInfo(el, key) {
    const event = {
        event: el.getAttribute('data-layer-'+key)
    }
    const eventData = el.getAttributeNames()
        .filter(n => n.startsWith('data-layer') && n !== 'data-layer-'+key && !n.startsWith('data-layer-auto') && !n.startsWith('data-layer-section'))
        .reduce((obj, n) => {
            const parts = n.replace('data-layer-', '').split('-');
            let nested = obj;
            for (let i=0; i<parts.length - 1; i++) {
                const name = toCamelCase(parts[i]);
                if (!nested[name]) {
                    nested[name] = {};
                }
                nested = nested[name];
            }
            nested[toCamelCase(parts[parts.length - 1])] = el.getAttribute(n);
            return obj;
        }, event);
    const productArr = getProductArr();
    if (el.hasAttribute("data-layer-auto-product") && productArr.length > 0) {
        eventData.product = productArr;
    }

    return eventData;
}

const BodyEndScheduleTour = {
    selectors: {
        self: '[data-cmp-component="apartmentdetail"]',
        accordionButton: '.cmp-apartmentDetail__accordion-button',
        accordionPanel: '.cmp-apartmentDetail__accordion-panel',
        promoMessage: '.cmp-apartmentDetail__promo',
        restrictByUnitType: '[data-cmp-component-restrict-by="unitType"]',
        restrictByUnit: '[data-cmp-component-restrict-by="unit"]',
        restrictByUnitOrUnitType: '[data-cmp-component-restrict-by="unit"],[data-cmp-component-restrict-by="unitType"]'
    },
    initialized: false,
    registerButtonEvents: function () {
        //cleanup required
    },
    openScheduleTourPopup: function () {
        var pageURL = document.location.href;
        if (pageURL.indexOf("#")) {
            pageURL = pageURL.substring(0, pageURL.indexOf("#"));
        }
        //var strIframe = document.getElementById("schedule-tour-modal").querySelectorAll("iframe");
        var embedHolder = document.getElementById('embedHolder');
        if (embedHolder && (!embedHolder.innerHTML || embedHolder.innerHTML.indexOf("iframe")  === -1)) { 
            embedHolder.innerHTML = "<iframe id='ifrm' src='" + scheduleATourUrl + "' style='border:0;' frameborder='0' />";        
        }
        var modal = document.getElementById('schedule-tour-modal');
        modal.style.display = "block";
    },
    init: function () {
        if (this.initialized) return;
        this.initialized = true;
        if(window.appEventData) {
            document.querySelectorAll("[data-layer-viewed]").forEach(el => {
                const eventData = collectDataLayerInfo(el, 'viewed');
                window.appEventData.push(eventData);
            })
            const searchTerm = new URLSearchParams(location.search).get("search_term")
            if (searchTerm) {
                const productArr = getProductArr()
                const searchArr = productArr.map(item => {
                    return Object.keys(item).map(key => item[key]).reduce((acc, item) => {
                        acc = Object.keys(item).reduce((acc, key) => {
                            if (key === "name") {
                                acc["searchProductName"] = item[key]
                            } else {
                                acc[`search${key.charAt(0).toUpperCase()}${key.slice(1)}`] = item[key]
                            }
                            return acc
                        }, acc)
                        return acc
                    }, {})
                })
                appEventData.push({
                    "event": "Onsite Search Succeeded",
                    "onsiteSearch": {
                        "keyword": {
                            "searchTerm": searchTerm,
                            "searchType": "locations"
                        }
                    },
                    "search": searchArr
                });
            }
            if (digitalData.pageInfo.currentPagePath.indexOf("portfolio/en/collections") >= 0) {
                appEventData.push({
                    "event": "Product Collection Viewed",
                    "product": digitalData.collectionInfo.properties.map(p => ({
                        productInfo: {
                            productID: p.onesiteID
                        }
                    })),
                    "productCollection": {
                        "name": digitalData.collectionInfo.name
                    }
                });
            }
            window.appEventData.push({
                "event": "Page Load Completed"
            });
            document.querySelectorAll("[data-layer-clicked]").forEach(el => {
                el.addEventListener("click", () => {
                    const eventData = collectDataLayerInfo(el, 'clicked');
                    window.appEventData.push(eventData);
                })
            })
            if (digitalData.pageInfo.currentPagePath.indexOf("portfolio/en/properties") >= 0 && digitalData.pageInfo.currentPagePath.indexOf("commoncontent") === -1) {
                const productArr = getProductArr();
                const event = {
                    "event": "Product Location Viewed"
                };
                if (productArr.length > 0) {
                    event.product = productArr
                }
                window.appEventData.push(event);
            }
            const header = document.querySelector('header');
            const footer = document.querySelector('footer');
            const excludedElements = ['.glider-prev-btn', '.glider-next-btn', '.glider-prev', '.glider-next',
                '.dialog-close', '.cmp-footer__navigation *', '.cmp-header__links--left *',
                '.cmp-header__mobileTrigger', '.cmp-header__mobileMenu--top *', '.cmp-header__mobileMenu--info *',
                '.paginate-button'];
            [...document.querySelectorAll('a,button')].forEach(el => {
                if (excludedElements.some(selector => el.matches(selector))) {
                    return;
                }
                el.addEventListener('click', () => {
                    const event = {
                        "event": "CTA Link Clicked",
                        "linkInfo": {
                            "linkId": el.innerText.replace(/\s/g, '-').toLowerCase(),
                            "linkPage": digitalData.pageInfo.pageName
                        }
                    };
                    if (header.contains(el)) {
                        event.linkInfo.lingRegion = 'header';
                    } else if (footer.contains(el)) {
                        event.linkInfo.lingRegion = 'footer';
                    } else {
                        const section = el.closest('[data-layer-section]')
                        if (section) {
                            event.linkInfo.lingRegion = section.getAttribute('data-layer-section') || (section.innerText
                                .split('\n')[0]
                                .split(/\s/).slice(0, 3).join('-')
                                .toLowerCase());
                        }
                    }
                    if (event.linkInfo.lingRegion) {
                        appEventData.push(event);
                    }
                })
            })
        }
        /*
        // Get the modal
        var modal = document.getElementById('schedule-tour-modal');
// Get the button that opens the modal
        var btnByClass = document.getElementsByClassName("schedule-tour-popup-button");
        if (window.location.href.indexOf("#schedule-tour-popup-button") !== -1) {
            this.openScheduleTourPopup();
        }
         var embedHolder = document.getElementById('embedHolder');
        if (embedHolder && (!embedHolder.innerHTML || embedHolder.innerHTML.indexOf("iframe")  === -1)){ 
            embedHolder.innerHTML = "<iframe loading='lazy' id='ifrm' src='" + scheduleATourUrl + "' style='border:0;' frameborder='0' />";        
        }
// Get the <span> element that closes the modal
        var span = document.getElementsByClassName("schedule_popup_close")[0];

// When the user clicks the button, open the modal 
        for (var i = 0, len = btnByClass.length | 0; i < len; i = i + 1 | 0) {
            btnByClass[i].onclick = this.openScheduleTourPopup;
        }
// When the user clicks on <span> (x), close the modal
        if(span) {
            span.onclick = function () {
                modal.style.display = "none";
            }
        }

// When the user clicks anywhere outside of the modal, close it
        window.onclick = function (event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }         
         */
    }
};

export default BodyEndScheduleTour;