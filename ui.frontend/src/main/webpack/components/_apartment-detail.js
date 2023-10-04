import * as availabilityFeed from './_availability-feed.js';
import * as bodyendscheduletour from './_body-end-schedule-tour.js';
const ApartmentDetail = {
    selectors: {
        self: '[data-cmp-component="apartmentdetail"]',
        accordionButton: '.cmp-apartmentDetail__accordion-button',
        accordionPanel: '.cmp-apartmentDetail__accordion-panel',
        promoMessage: '.cmp-apartmentDetail__promo',
        restrictByUnitType: '[data-cmp-component-restrict-by="unitType"]',
        restrictByUnit: '[data-cmp-component-restrict-by="unit"]',
        restrictByUnitOrUnitType: '[data-cmp-component-restrict-by="unit"],[data-cmp-component-restrict-by="unitType"]',
        availabeSection: '.cmp-apartmentDetail__available',
        unitList: '.cmp-apartmentDetail__unitList',
        unit: '.cmp-apartmentDetail__unitList .cmp-apartmentDetail__unit',
        apartmentDetialHeader: '.cmp-apartmentDetail__header',
        floorPlanImage: '.cmp-apartmentDetail__floorplan--large img'
    },
    cssClasses: {
        button: {
            expanded: "cmp-apartmentDetail__accordion-button--expanded"
        },
        panel: {
            hidden: "cmp-apartmentDetail__accordion-panel--hidden",
            expanded: "cmp-apartmentDetail__accordion-panel--expanded"
        }
    },
    registerDataLayerEvents: function () {
        document.addEventListener("click", e => {
            if (e.target.matches(`${this.selectors.unitList} a`)) {
                const pk = e.target.getAttribute("data-pk");
                const unit = digitalData.propertyInfo.group.units.find(u => u.pk === pk);
                appEventData.push({
                    "event": "Product Interaction Occurred",
                    "product": [
                        {
                            "floorPlan": {
                                "floorPlanId": unit.floorPlan.floorPlanID,
                                "floorPlanName": unit.floorPlan.floorPlanName
                            },
                            "productInfo": {
                                "name": unit.pk,
                                "productID": unit.address.unitID
                            }
                        }
                    ],
                    "productInteraction": {
                        "interactionDetail": "View Unit",
                        "interactionType": "View Unit Clicked"
                    }
                });
            } else if (e.target.matches(`${this.selectors.apartmentDetialHeader} a`)) {
                const unit = digitalData.propertyInfo.unit;
                appEventData.push({
                    "event": "Product Interaction Occurred",
                    "product": [
                        {
                            "floorPlan": {
                                "floorPlanId": unit.floorPlan.floorPlanID,
                                "floorPlanName": unit.floorPlan.floorPlanName
                            },
                            "productInfo": {
                                "name": unit.pk,
                                "productID": unit.address.unitID
                            }
                        }
                    ],
                    "productInteraction": {
                        "interactionDetail": "Apply Now",
                        "interactionType": "Apply Now Clicked"
                    }
                });
            } else if (e.target.matches(".schedule-tour-popup-button")) {
                const unit = digitalData.propertyInfo.unit || digitalData.propertyInfo.group.units[0];
                appEventData.push({
                    "event": "Product Interaction Occurred",
                    "product": [
                        {
                            "floorPlan": {
                                "floorPlanId": unit.floorPlan.floorPlanID,
                                "floorPlanName": unit.floorPlan.floorPlanName
                            },
                            "productInfo": {
                                "name": digitalData.propertyInfo.unit ? unit.pk : digitalData.propertyInfo.group.name,
                                "productID": digitalData.propertyInfo.unit ? unit.address.unitID : unit.floorPlan.floorPlanID,
                            }
                        }
                    ],
                    "productInteraction": {
                        "interactionDetail": "Schedule a Tour popup opened",
                        "interactionType": "Schedule a Tour clicked"
                    }
                });
            }
        });
    },
    registerButtonEvents: function () {
        const {selectors} = this;
        const {button, panel} = this.cssClasses;
        const sections = document.querySelectorAll(selectors.self);
        sections.forEach(section => {
            const accordionButton = section.querySelector(selectors.accordionButton);
            const accordionPanel = section.querySelector(selectors.accordionPanel);
            if (accordionButton) {
                accordionButton.addEventListener('click', () => {
                    if (accordionButton.classList.contains(button.expanded)) {
                        accordionButton.classList.remove(button.expanded);
                        accordionButton.setAttribute("aria-expanded", "false");
                        accordionPanel.classList.remove(panel.expanded);
                        accordionPanel.classList.add(panel.hidden);
                    } else {
                        accordionButton.classList.add(button.expanded);
                        accordionButton.setAttribute("aria-expanded", "true");
                        accordionPanel.classList.add(panel.expanded);
                        accordionPanel.classList.remove(panel.hidden);
                    }
                });
            }
        });
    },
    addPromoCssClass: function () {
        const {selectors} = this;
        const sections = document.querySelectorAll(selectors.self);
        sections.forEach(section => {
            const promoMessage = section.querySelector(selectors.promoMessage);
            if (promoMessage) {
                section.classList.add("hasPromo");
            } else {
                section.classList.remove("hasPromo");
            }
        });
    },
    createPaginationStatus: function(totalItems, itemsPerPage){
        return `<p class="cmp-apartmentDetail__unitPagination-status" 
                role="status" 
                aria-live="polite">1-${itemsPerPage} of ${totalItems} Results</p>`;
    },
    updatePaginationStatus: function(totalItems, itemsPerPage, pageNumber){
        const startIndex = itemsPerPage * (pageNumber -1) + 1;
        const endIndex = (itemsPerPage * pageNumber < totalItems) ? (itemsPerPage * pageNumber): totalItems ;
        return `${startIndex}-${endIndex} of ${totalItems} Results`;
    },
    createPaginationButtons: function(pageCount) {
        const prevButton = `<div class="cmp-apartmentDetail__unitPagination-buttons">
                             <button class="paginate-button previous" tabindex="-1">
                                <span class="visually-hidden">previous</span>
                                <svg width="7" height="10" viewBox="0 0 7 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <g>
                                    <path d="M6 9L2 5L6 1" stroke="#191919" stroke-width="2" stroke-linecap="round"/>
                                    </g>
                                </svg>                                
                            </button>
                            <button class="paginate-button current" data-page-index="1">
                                <span>1</span>
                            </button>`;
        const nextButton = `<button class="paginate-button next">
                                <span class="visually-hidden">next</span>
                                <svg width="7" height="10" viewBox="0 0 7 10" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <g>
                                    <path d="M1 9L5 5L1 1" stroke="#191919" stroke-width="2" stroke-linecap="round"/>
                                    </g>
                                </svg>                                
                            </button>
                            </div>`;
        let otherButtons='';
        for (let i = 2; i <= pageCount; i++) {
            otherButtons += `<button class="paginate-button" data-page-index="${i}">
                                <span>${i}</span>
                            </button>`;
        }
        return prevButton + otherButtons + nextButton;
    },
    renderPagination: function(unitList, units) {
        const totalUnits = units.length;
        const itemsPerPage = 5;
        const pageCount = Math.ceil(totalUnits / itemsPerPage);
        const paginator = document.createElement('div');
        paginator.className = "cmp-apartmentDetail__unitPagination";
        const pageStatusEl = this.createPaginationStatus(totalUnits, itemsPerPage);
        const paginationButtons = this.createPaginationButtons(pageCount);
        paginator.innerHTML=`${pageStatusEl} ${paginationButtons}`;
        unitList.parentNode.insertBefore(paginator, unitList.nextElementSibling);
        this.displayUnitPage(units, itemsPerPage, 1);
        this.registerPaginationEvents(units, document.querySelector(".cmp-apartmentDetail__unitPagination") ,itemsPerPage);
    },
    registerPaginationEvents: function(units, paginationElement, itemsPerPage){
        const totalItems = units.length;
        const pageCount = Math.ceil(units.length/itemsPerPage);
        const buttonArray = Array.prototype.slice.call(paginationElement.querySelectorAll("button.paginate-button"));
        const statusElement = paginationElement.querySelector(".cmp-apartmentDetail__unitPagination-status");
        buttonArray.forEach((el, index) => {
            el.addEventListener('click', ()=>{
                const currentPageIndex = parseInt(paginationElement.querySelector("button.paginate-button.current").getAttribute("data-page-index"));
                if(index === 0){
                    if(currentPageIndex !== 1) {
                        if (currentPageIndex === 2) {
                            buttonArray[0].setAttribute("tabindex", "-1");
                        }
                        buttonArray[pageCount + 1].removeAttribute("tabindex");
                        buttonArray[currentPageIndex].classList.remove("current");
                        buttonArray[currentPageIndex - 1].classList.add("current");
                        this.displayUnitPage(units, itemsPerPage, currentPageIndex - 1);
                        statusElement.innerHTML = this.updatePaginationStatus(totalItems, itemsPerPage, currentPageIndex - 1);
                    } 
                } else if (index === pageCount + 1) {
                    if(currentPageIndex !== pageCount) {
                        if (currentPageIndex === pageCount -1) {
                            buttonArray[pageCount + 1].setAttribute("tabindex", "-1");
                        }
                        buttonArray[0].removeAttribute("tabindex");
                        buttonArray[currentPageIndex].classList.remove("current");
                        buttonArray[currentPageIndex + 1].classList.add("current");
                        this.displayUnitPage(units, itemsPerPage, currentPageIndex + 1);
                        statusElement.innerHTML = this.updatePaginationStatus(totalItems, itemsPerPage, currentPageIndex + 1);
                    } 
                } else {
                    if (index !== currentPageIndex) {
                        buttonArray[currentPageIndex].classList.remove("current");
                        buttonArray[index].classList.add("current");
                        this.displayUnitPage(units, itemsPerPage, index);
                        if (index === 1) {
                            buttonArray[0].setAttribute("tabindex", "-1");
                            buttonArray[pageCount + 1].removeAttribute("tabindex");
                        } else if (index === pageCount) {
                            buttonArray[pageCount + 1].setAttribute("tabindex", "-1");
                            buttonArray[0].removeAttribute("tabindex");
                        } else {
                            buttonArray[0].removeAttribute("tabindex");
                            buttonArray[pageCount + 1].removeAttribute("tabindex");
                        }
                        statusElement.innerHTML = this.updatePaginationStatus(totalItems, itemsPerPage, index);
                    }
                }

            });
        });
    },
    displayUnitPage: function(units, itemsPerPage, pageNumber) {
        const minIndex = (pageNumber -1 ) * itemsPerPage;
        const maxIndex = pageNumber * itemsPerPage;
        for(let i = 0; i < units.length; i ++) {
            if (i >= minIndex && i < maxIndex) {
                units[i].classList.remove('hidden');
            } else {
                if (!units[i].classList.contains('hidden')) {
                    units[i].classList.add('hidden');
                }
            }
        }
        document.querySelector(this.selectors.availabeSection).scrollIntoView({ behavior: 'smooth', block: 'start'});
    },
    bindHandleBar: function () {
        const filters = availabilityFeed.default.retrieveFilterParamsFromQuery();
        filters.moveindate = filters.moveindate || availabilityFeed.default.defaultMoveInDate();
        digitalData.propertyInfo.units
                .then(units => availabilityFeed.default.updateRent(units, filters.moveindate))
                .then(units => units.filter(unit => unit.availability.availableBit))
                .then(units => units.filter(unit => unit.rent && unit.unitDetails && unit.unitDetails.bedrooms))
                .then(units => units.filter(availabilityFeed.default.createFiltersFunction(filters)))
                .then(units => availabilityFeed.default.updateOLLink(units, filters.moveindate))
                .then(availabilityFeed.default.groupUnits)
                //.then(availabilityFeed.default.sortItems(this.sortKey, this.sortOrder)) //the default sorting: rent, asc
                .then(groups => {
                    availabilityFeed.default.updateGroupsData(groups);
                    availabilityFeed.default.updateSortedGroupsData(groups);
                    var groupArr = groups.filter(group => group.name === digitalData.propertyInfo.unitType);
                    if (groupArr !== null && groupArr.length === 1) {
                        digitalData.propertyInfo.group = groupArr[0]; //debugging purpose
                        digitalData.propertyInfo.group.units.sort(availabilityFeed.default.compareFunction('rent', 'asc'));
                        digitalData.propertyInfo.group.floorPlan = {};
                        digitalData.propertyInfo.group.floorPlan.small = {src: "/content/dam/common/properties/" + digitalData.propertyInfo.propertyId + "/floorplans/" + digitalData.propertyInfo.group.name.replaceAll(/[\s]/g, "-") + "-streetmap.jpg"};
                        digitalData.propertyInfo.group.floorPlan.large = {src: "/content/dam/common/properties/" + digitalData.propertyInfo.propertyId + "/floorplans/" + digitalData.propertyInfo.group.name.replaceAll(/[\s]/g, "-") + ".jpg"};
                        //if pk 
                        if (digitalData.propertyInfo.pk) {
                            //set  unit specific data
                            var unitArr = digitalData.propertyInfo.group.units.filter(unit => unit.pk === digitalData.propertyInfo.pk);
                            digitalData.propertyInfo.unit = unitArr[0]; //debugging purpose
                            digitalData.propertyInfo.group.units = digitalData.propertyInfo.group.units.filter(unit => unit.pk !== digitalData.propertyInfo.pk).slice(0, 3);
                        }

                        digitalData.propertyInfo.el.innerHTML = digitalData.propertyInfo.template(digitalData.propertyInfo);
                        this.paginateUnitsList();
                        digitalData.propertyInfo.el.style.visibility = 'visible';
                        bodyendscheduletour.default.init();

                        document.querySelector(this.selectors.floorPlanImage).addEventListener("error", (e) => {
                            e.target.parentNode.classList.add('hidden')
                            e.target.parentNode.nextElementSibling.classList.remove('hidden')
                            appEventData.push({
                                "event": "Floor Plan Unavailable",
                                "product": digitalData.product
                            });
                        })
                    }
                });

    },
    handleBarTemplates:
            {
                template: document.body.querySelector('[data-cmp-component-restrict-by="unit"],[data-cmp-component-restrict-by="unitType"]') !== null ? Handlebars.compile(document.body.querySelector('[data-cmp-component-restrict-by="unit"],[data-cmp-component-restrict-by="unitType"]').innerHTML) : null,
                el: document.body.querySelector('[data-cmp-component-restrict-by="unit"],[data-cmp-component-restrict-by="unitType"]')                   
            },
    findQSParam: function (paramName) {
        const params = (new URL(document.location)).searchParams;
        const groupName = params.get(paramName); // is the string "Jonathan Smith".
        return groupName;
    },
    paginateUnitsList: function() {
        //add Pagination to the available units section
        const {selectors} = this;
        const self = document.querySelector(selectors.self);
        const units = self.querySelectorAll(selectors.unit);
        if(units.length > 5) {
            const unitList = self.querySelector(selectors.unitList);
            this.renderPagination(unitList, units);
        }
    },
    init: function () {
        const {selectors} = this;
        this.registerButtonEvents();
        this.registerDataLayerEvents();
        this.addPromoCssClass();
        //console.log(availabilityFeed);
        if (document.querySelector(selectors.restrictByUnitOrUnitType)) {
            if (this.findQSParam('unitType')) {
                digitalData.propertyInfo.unitType = this.findQSParam('unitType');
                if (this.findQSParam('pk')) {
                    digitalData.propertyInfo.pk = this.findQSParam('pk');
                }
                digitalData.propertyInfo.currentPagePath = digitalData.pageInfo.currentPagePath;
                digitalData.propertyInfo.template = this.handleBarTemplates.template;
                digitalData.propertyInfo.el = this.handleBarTemplates.el;
                availabilityFeed.default.fetchUnits(this.bindHandleBar.bind(this));
            }
        }
    }
};

export default ApartmentDetail;