import Choices from 'choices.js';
const AvailabilityFeed = {
    selectors: {
        self: '[data-cmp-component="availabilityfeed"]',
        loader: '.cmp-availability__loader',
        filters: '#availability-filters',
        moveInDateFilter: 'duet-date-picker',
        maxRentFilter: '#availability-filter-max-rent',
        minRentFilter: '#availability-filter-min-rent',
        moveInDateInput: '#availability-filter-move-in-date',
        sortFieldList: '#availability-sort-field',
        resultWrapper: ".cmp-availability__result-wrapper",
        results: ".cmp-availability__results",
        resultTbody: '#availability-result-table tbody',
        resultThead: '#availability-result-table thead',
        resultList: '#availability-result-list',
        emptyResult: '.cmp-availability__emptyResults',
        tablePagination: '.cmp-availability__paginate--table',
        listPagination: '.cmp-availability__paginate--list',
        tabs: '.cmp-availability__tabs',
        tabControl: 'button[role="tab"]',
        tabPanel: 'div[role="tabpanel"]',
        clearFilters: '#availability_filter_clear',
        filterToggleBtn: '#availability-filter-toggle',
    },
    localStorageFiltersKey: "availability-feed-filters",
    groupsData: [],
    sortedGroupsData: [],
    sortKey: 'minRent',
    sortOrder: 'asc',
    dataLayer: {
        listType: "Apartment"
    },
    isClearingState: false,
    currentPositionOffset: 0,
    updateGroupsData: function(newGroups) {
        this.groupsData = newGroups;
    },
    updateSortedGroupsData: function(newSortedGroups) {
        this.sortedGroupsData = newSortedGroups;
    },
    groupUnits: function(units){
        const groups = units.reduce((groups, unit) => {
            //console.log(unit.oneSiteId);
            const uniqKey = unit.oneSiteId + unit.floorPlan.floorPlanName; //this is the change for collections, should work for single property too
            if (groups[uniqKey]) { //concatenate these two uniq identifiers
                const item = groups[uniqKey];
                item.units.push(unit);
                item.availability++;
                const unitRent = parseFloat(unit.rent);
                if (unitRent < item.minRent) {
                    item.minRent = unitRent;
                }
                if (unitRent > item.maxRent) {
                    item.maxRent = unitRent;
                }
            } else {
                groups[uniqKey] = {
                    name: unit.floorPlan.floorPlanName,
                    floorPlanId: unit.floorPlan.floorPlanID,
                    oneSiteId:unit.oneSiteId,
                    type: `${unit.unitDetails.bedrooms === '0'
                        ? `Studio | ${unit.unitDetails.bathrooms} Bath`
                        : `${unit.unitDetails.bedrooms} Bed | ${unit.unitDetails.bathrooms} Bath`}`,
                    bedrooms: parseInt(unit.unitDetails.bedrooms),
                    bathrooms: parseInt(unit.unitDetails.bathrooms),
                    area: parseFloat(unit.unitDetails.grossSqFtCount),
                    minRent: parseFloat(unit.rent),
                    maxRent: parseFloat(unit.rent),
                    availability: 1,
                    units: [unit]
                };
            }
            return groups;
        }, {});
        return Object.values(groups);
    },
    formatAvailabilityURL: function(oneSiteID) {
        if (typeof oneSiteID === "string") {
            return `${digitalData.pageInfo.currentPagePath}.availability.json?oneSiteID=${oneSiteID}`;
        }
        if (Array.isArray(oneSiteID)) {
            return `${digitalData.pageInfo.currentPagePath}.availability.json?${oneSiteID.map(id => `oneSiteID=${id}`).join('&')}`;
        }
    },
    formatDate: function(date) {
        return `${date.getMonth()+1}/${date.getDate()}/${date.getFullYear()}`;
    },
    parseDate: function(str) {
        const parts = str.split("/");
        return new Date(parseInt(parts[2]), parseInt(parts[0])-1, parseInt(parts[1]));
    },
    updateUnitRent: function(unit, moveInDate, property) {
        const formatedDate = this.formatDate(moveInDate || this.defaultMoveInDate());
        const unitProperty = property || unit.property;
        const building = (unitProperty && unitProperty.buildings ? unitProperty.buildings : []).find(
            (building) => building.oneSiteId === parseInt(unit.oneSiteId),
        );
        const isLroPricing =
            building && building.lroPricing !== null
                ? building.lroPricing
                : unitProperty && unitProperty.lroPricing !== null
                ? unitProperty.lroPricing
                : !!unit.leaseOptions;

        let leaseRent = null;
        if (!isLroPricing) {
            leaseRent = unit.baseRentAmount;
        } else if (unit.leaseOptions && unit.leaseOptions.length > 0) {
            const option = unit.leaseOptions.find((option) => option.neededByDate === formatedDate);
            if (option) {
                leaseRent = option.rent;
            }
        }
        return {
            ...unit,
            rent: leaseRent ? parseFloat(leaseRent).toFixed(0) : null,
        };
    },
    updateRent: function(units, moveInDate, property) {
        return units.map((unit) => this.updateUnitRent(unit, moveInDate, property));
    },
    formatOlLink: function(moveInDate, id, oneSiteID) {
        oneSiteID = oneSiteID || (digitalData.propertyInfo ? digitalData.propertyInfo.onesiteID : "");
        const oneSiteIDPart = oneSiteID ? `&oneSiteID=${oneSiteID.split(',')[0].trim().split(":")[0].trim()}` : "";
        return `/lease-online.html?MoveInDate=${this.formatDate(moveInDate)}&UnitId=${id}&SearchUrl=${encodeURIComponent(location.href)}${oneSiteIDPart}`;
    },
    updateOLLink: function(units, moveInDate) {
        return units.map(unit => ({...unit, olLink: this.formatOlLink(moveInDate, unit.address.unitID, unit.oneSiteId)}));
    },
    filterUnitMinRent: function(minRent, unit) {
        if (minRent > 0) {
            if (parseFloat(unit.rent) < minRent) {
                return false;
            }
        }
        return true;
    },
    filterUnitMaxRent: function(maxRent, unit) {
        if (maxRent > 0) {
            if (parseFloat(unit.rent) > maxRent) {
                return false;
            }
        }
        return true;
    },
    filterUnitMoveInDate: function(moveInDate, unit) {
        if (moveInDate) {
            if (!unit.availability.availableDate || !unit.availability.madeReadyDate) {
                return false;
            }
            return this.parseDate(unit.availability.availableDate).getTime() <= moveInDate.getTime()
                && this.parseDate(unit.availability.madeReadyDate).getTime() <= moveInDate.getTime();
        }
        return true;
    },
    filterUnitHomeType: function(homeType, unit) {
        if (homeType) {
            switch (homeType) {
                case "studio": {
                    return parseInt(unit.unitDetails.bedrooms) === 0;
                }
                case "1bedroom": {
                    return parseInt(unit.unitDetails.bedrooms) === 1;
                }
                case "2bedroom+": {
                    return parseInt(unit.unitDetails.bedrooms) >= 2;
                }
                case "pent": {
                    if (unit && unit.floorPlan) {
                        if (unit.floorPlan.floorPlanName && unit.floorPlan.floorPlanName.includes('Penthouse')) {
                            return true;
                        }
                        if (unit.floorPlan.floorPlanName && unit.floorPlan.floorPlanName.includes('PH')) {
                            return true;
                        }
                        if (unit.floorPlan.floorPlanGroupName && unit.floorPlan.floorPlanGroupName.includes('PH')) {
                            return true;
                        }
                    }
                    return !!(unit && unit.address && unit.address.unitNumber && unit.address.unitNumber.includes('PH'));

                }
                case "town": {
                    if (unit && unit.floorPlan) {
                        if (unit.floorPlan.floorPlanName && unit.floorPlan.floorPlanName.includes('TH')) {
                            return true;
                        }
                        if (unit.floorPlan.floorPlanGroupName && unit.floorPlan.floorPlanGroupName.includes('TH')) {
                            return true;
                        }
                    }
                    return false;
                }
                default:
                    return false;
            }
        }
        return true;
    },
    sortItems: function(field, order) {
        return items => items.sort(this.compareFunction(field, order))
    },
    setAvailableHomeTypes: function(units) {
        const filters = document.querySelector(this.selectors.filters);
        if (filters) {
            const allTypes = ['studio', '1bedroom', '2bedroom+', 'pent', 'town'];
            const activeTypes = units.reduce((types, unit) => {
                allTypes.filter(type => this.filterUnitHomeType(type, unit)).forEach(type => types.add(type))
                return types;
            }, new Set())
            allTypes
                .filter(type => !activeTypes.has(type))
                .map(type => (filters.querySelector(`input[name="hometype"][value="${type}"]`)))
                .forEach(el => {
                    el.disabled = true;
                })
        }
        return units;
    },
    filterUnits: function(filterFn, moveInDate){
        this.getDestinationObject().units
            .then(units => this.updateRent(units, moveInDate))
            .then(units => units.filter(unit => unit.availability.availableBit))
            .then(units => units.filter(unit => unit.rent && unit.unitDetails && unit.unitDetails.bedrooms))
            .then(units => units.filter(filterFn))
            .then(this.groupUnits)
            .then(this.sortItems(this.sortKey, this.sortOrder)) //the default sorting: rent, asc
            .then(groups => {
                this.updateGroupsData(groups);
                this.updateSortedGroupsData(groups);
                this.renderGroupsIntoPage(groups);
                this.renderPagination(groups);
                this.toggleLoader('hide');
            });
    },
    getDestinationObject: function() {
        return digitalData.propertyInfo || digitalData.collectionInfo;
    },
    fetchUnits: function(successCallback){
        if (digitalData) {
            let oneSiteID;
            const data = this.getDestinationObject();
            if (data && data.onesiteID) {
                oneSiteID = data.onesiteID.split(',').map(pair => pair.split(":")[0].trim());
            } else if (data && data.properties) {
                oneSiteID = data.properties.map(p => p.onesiteID).filter(id => !!id).flatMap(id => id.split(",").map(pair => pair.split(":")[0].trim()));
            }
            if (oneSiteID) {
                data.units = fetch(this.formatAvailabilityURL(oneSiteID))
                    .then(response => response.json())
                    .then(units => this.setAvailableHomeTypes(units));
            } else {
                data.units = Promise.reject("OneSiteID is not configured");
            }
            if (!successCallback) {
                const filters = this.retrieveFilterParamsFromQuery();
                filters.moveindate = filters.moveindate || this.defaultMoveInDate();
                this.applyFilters(filters);
            } else {
              data.units.then(successCallback);
            }
        } else {
            digitalData.propertyInfo.units = Promise.reject("digitalData not found");
        }
    },
    disableMaxRentBelow: function(min, maxRentChoices, maxRentOptions) {
        const maxRent = document.querySelector(this.selectors.maxRentFilter);
        maxRent.querySelectorAll(`option`).forEach(option => {
            option.disabled = parseFloat(option.value) <= min;
            option.style.display = option.disabled ? 'none' : 'block';
            if (option.disabled && option.selected) {
                option.selected = false;
            }
        });

        const maxRentChoiceArray = maxRentOptions.map(option => {
            const disabled = parseFloat(option.value) <= min;
            const selected = disabled ? false : (parseFloat(option.value) == min ? true : false);
            return {
                value: option.value,
                label: option.textContent,
                selected,
                disabled
            };
        });
        maxRentChoices.setChoices(maxRentChoiceArray, 'value', 'label', true);
    },
    disableMinRentAbove: function(max, minRentChoices, minRentOptions) {
        const minRent = document.querySelector(this.selectors.minRentFilter);
        minRent.querySelectorAll(`option`).forEach(option => {
            option.disabled = parseFloat(option.value) >= max;
            option.style.display = option.disabled ? 'none' : 'block';
            if (option.disabled && option.selected) {
                option.selected = false;
            }
        });
        const minRentChoiceArray = minRentOptions.map(option => {
            const disabled = parseFloat(option.value) >= max;
            const selected = disabled ? false : (parseFloat(option.value) == max ? true : false);
            return {
                value: option.value,
                label: option.textContent,
                selected,
                disabled
            };
        });

        minRentChoices.setChoices(minRentChoiceArray, 'value', 'label', true);
    },
    getFiltersListString: function() {
        const data = this.collectFiltersFormData();
        return Object.entries(data).map(([key, value]) => {
            if (key === 'moveindate') {
                value = document.querySelector(this.selectors.moveInDateInput).value
            }
            return `${key}~${value}`
        }).join("|");
    },
    onFilterChanged: function(event) {
        if (this.isClearingState) return;
        const filterList = this.getFiltersListString();
        appEventData.push({
            event: event,
            listingRefined: {
                filterList: filterList,
                listingType: this.dataLayer.listType
            }
        });
    },
    onFilterAdded: function() {
        this.onFilterChanged("Listing Filter Added");
    },
    onFilterRemoved: function() {
        this.onFilterChanged("Listing Filter Removed");
    },
    registerFilterEvents: function() {
        const filters = document.querySelector(this.selectors.filters);
        const picker = filters.querySelector(this.selectors.moveInDateFilter);
        const minRent = filters.querySelector(this.selectors.minRentFilter);
        const maxRent = filters.querySelector(this.selectors.maxRentFilter);
        const params = this.retrieveFilterParamsFromQuery();
        const minRentOptions = Array.from(minRent.querySelectorAll('option'));
        const maxRentOptions = Array.from(maxRent.querySelectorAll('option'));
        const mobileSortDropdown = document.querySelector(this.selectors.sortFieldList);
        const filtersReset = filters.querySelector(this.selectors.clearFilters);
        const choiceConfig = {
            searchEnabled: false,
            itemSelectText: '',
            shouldSort: false,
            classNames: {
                selectedState: 'is-selected',
                highlightedState: 'is-highlight'
              }
        };
        if(minRent) {
            var minRentChoices = new Choices(minRent, choiceConfig);
        }
        if(maxRent) {
           var maxRentChoices = new Choices(maxRent, choiceConfig);
        }
        if (mobileSortDropdown) {
            var mobileSortChoices = new Choices(mobileSortDropdown, choiceConfig);
        }
        if (picker) {
            const moveInDate = params.moveindate || this.defaultMoveInDate();
            picker.setAttribute("value", moveInDate.toISOString().slice(0, 10));
            picker.setAttribute("min", this.defaultMoveInDate().toISOString().slice(0, 10));
            picker.addEventListener("duetChange", (event) => {
                if (event.detail.value) {
                    picker.closest("form").classList.remove("move-in-date-empty");
                }
            });
        }
        if (minRent && maxRent) {
            minRent.addEventListener('change', e => {
                const newValue = parseFloat(e.target.value);
                this.disableMaxRentBelow(newValue, maxRentChoices, maxRentOptions);
            });
            maxRent.addEventListener('change', e => {
                const newValue = parseFloat(e.target.value);
                this.disableMinRentAbove(newValue, minRentChoices, minRentOptions);
            });
        }
        if (minRent && params.minrent) {
            minRentChoices.setValue([`${params.minrent}`])
            this.disableMaxRentBelow(params.minrent, maxRentChoices, maxRentOptions);
        }
        if (maxRent && params.maxrent && params.maxrent !== Infinity && (params.minrent ? params.minrent < params.maxrent : true)) {
            maxRentChoices.setValue([`${params.maxrent}`])
            this.disableMinRentAbove(params.maxrent, minRentChoices, minRentOptions);
        }
        if (params.hometype) {
            params.hometype
                .map(type => filters.querySelector(`input[name="hometype"][value="${type}"]`))
                .forEach(el => {
                    el.checked = true;
                    el.closest('.checkbox-item').classList.add("checked");
                })
        }
        if (filtersReset) {
            function triggerChangeEvent(element) {
                if ("createEvent" in document) {
                    var evt = document.createEvent("HTMLEvents");
                    evt.initEvent("change", false, true);
                    element.dispatchEvent(evt);
                }
                else
                    element.fireEvent("onchange");
            }
            filtersReset.addEventListener('click', (e) => {
                e.preventDefault();
                this.isClearingState = true;
                picker.setAttribute("value", this.defaultMoveInDate().toISOString().slice(0, 10));
                minRentChoices.setChoiceByValue('0');
                maxRentChoices.setChoiceByValue('Infinity');
                triggerChangeEvent(minRent);
                triggerChangeEvent(maxRent);
                filters.querySelectorAll('input[name="hometype"]').forEach(el => {
                    el.checked = false;
                    el.closest('.checkbox-item').classList.remove("checked");
                });
                this.applyFilters({moveindate: this.defaultMoveInDate()})
                this.isClearingState = false;
                this.onFilterRemoved();
            })
            filters.addEventListener('change', () => {
                filtersReset.disabled = this.isDefaultFilters(this.collectFiltersFormData());
            });
            filters.addEventListener('duetChange', () => {
                filtersReset.disabled = this.isDefaultFilters(this.collectFiltersFormData());
            });
        }
    },
    renderGroupsIntoPage: function(groups) {
        const {selectors} = this;
        const self = document.querySelector(selectors.self);
        const results = self.querySelector(selectors.results);
        const resultTbody = document.querySelector(selectors.resultTbody);
        const resultList = document.querySelector(selectors.resultList);
        const emptyResult = self.querySelector(selectors.emptyResult);
        if (groups.length) {
            this.renderGroupsIntoTable(groups, 1, resultTbody);
            this.renderGroupsIntoList(groups, 1, resultList);
            results.classList.remove("hidden");
            if (!emptyResult.classList.contains("hidden")){
                emptyResult.classList.add("hidden");
            }
        } else {
            if (!results.classList.contains("hidden")) {
                results.classList.add("hidden");
            }
            emptyResult.classList.remove("hidden");
        }
    },
    groupToDataLayerItem: function(group, i) {
        const data = {
            "floorPlan": {
                "floorPlanId": group.units[0].floorPlan.floorPlanID,
                "floorPlanName": group.units[0].floorPlan.floorPlanName
            },
            "isDisplayed": true,
            "itemPosition": i+1,
            "productInfo": {
                "name": group.units[0].floorPlan.floorPlanName,
                "productID": group.units[0].floorPlan.floorPlanID
            },
            "price": {
                "priceRangeLow": `${group.minRent}`,
                "priceRangeHigh": `${group.maxRent}`
            }
        };
        if (digitalData.propertyInfo) {
            data.location = {
                "locationId": digitalData.propertyInfo.onesiteID,
                "locationName": digitalData.propertyInfo.propertyName
            }
        }
        if (digitalData.collectionInfo) {
            data.collection = {
                "collectionId": digitalData.collectionInfo.id,
                "collectionName": digitalData.collectionInfo.name
            }
            if (!data.location) {
                const property = digitalData.collectionInfo.properties.find(prop => prop.onesiteID === group.oneSiteId);
                if (property) {
                    data.location = {
                        "locationId": property.onesiteID,
                        "locationName": property.propertyName
                    }
                }
            }
        }
        return data;
    },
    onListingDisplayed: function(groups, totalCount) {
        const listings = groups.map((g, i) => this.groupToDataLayerItem(g, i + this.currentPositionOffset));
        appEventData.push({
            "event": "Product Location Listing Displayed",
            "listingDisplayed": {
                "displayCount": groups.length,
                "filterList": this.getFiltersListString(),
                "listing": listings,
                "listingType": "floorplan",
                "resultsCount": totalCount
            }
        });
    },
    onListingClicked: function(group, i) {
        appEventData.push({
            "event": "Product Location Listing Item Clicked",
            "listingItemClicked": {
                "filterList": this.getFiltersListString(),
                "listing": [
                    this.groupToDataLayerItem(group, i + this.currentPositionOffset)
                ]
            }
        });
    },
    collectBuildingNamesByOneSiteId: function(oneSiteId) {
        return oneSiteId ? oneSiteId.split(",").map(pair => ({
            oneSiteId: pair.split(":")[0].trim(),
            label: pair.split(":").length > 1 ? pair.split(":")[1].trim() : ""
        })).reduce((acc, {oneSiteId, label}) => {
            acc[oneSiteId] = label
            return acc
        }, {}) : {}
    },
    renderGroupsIntoTable: function(groups, pageNumber, tbody) {
            tbody.innerHTML = '';
            var viewApartmentLink = tbody.dataset.viewApartmentLink !== null ? tbody.dataset.viewApartmentLink : './apartment-type.html';
            var propertyStandaloneBaseMapped = tbody.dataset.propertyStandaloneBaseMapped;
            const groupsToRender = groups.slice((pageNumber-1) * 10, pageNumber * 10);
            this.tableGroupsRendered = groupsToRender;
            if (window.getComputedStyle(tbody.parentNode.parentNode).display !== "none") {
                this.currentPositionOffset = (pageNumber-1) * 10;
                this.onListingDisplayed(groupsToRender, groups.length);
            }
            const labelByOneSiteId = digitalData.propertyInfo ? this.collectBuildingNamesByOneSiteId(digitalData.propertyInfo.onesiteID)
                : digitalData.collectionInfo ? digitalData.collectionInfo.properties.reduce((acc, prop) => {
                const buildingNames = this.collectBuildingNamesByOneSiteId(prop.onesiteID)
                if (Object.keys(buildingNames).length > 0) {
                    acc = {...acc, ...buildingNames}
                }
                if (!acc[prop.onesiteID.split(",")[0].split(":")[0].trim()]) {
                    acc[prop.onesiteID.split(",")[0].split(":")[0].trim()] = prop.propertyName
                }
                return acc
            }, {}) : {};
            const allNan = groups.every(group => Number.isNaN(group.area));
            const areaCol = tbody.parentNode.querySelector('[data-col="area"]');
            if (allNan && areaCol) {
                areaCol.classList.add('hidden');
            } else if (!allNan && areaCol) {
                areaCol.classList.remove('hidden');
            }
            groupsToRender.map(group => `<tr><td><span class="small-title">${group.name}</span>${labelByOneSiteId[group.oneSiteId] ? '<span class="label-text"><br>' + labelByOneSiteId[group.oneSiteId] + '</span>' : ''}</td>                    
                    <td><span class="label-text">${group.type}</span></td>
                    ${allNan ? '' : `<td><span class="label-text">${Number.isNaN(group.area) ? '' : `${group.area} sq. ft.`}</span></td>`}
                    <td><span class="label-text">Starting at $${group.minRent}/month*</span></td>
                    <td><span class="label-text">${group.availability > 5 ? 'Yes' : group.availability + " Available"}</span></td>                    
                    <td><a href="${digitalData.collectionInfo ? propertyStandaloneBaseMapped + '/'+digitalData.collectionInfo.properties.find(prop => prop.onesiteID.indexOf(group.oneSiteId) >= 0).propertyId + '/commoncontent.apartment-type.html':viewApartmentLink}/${encodeURIComponent(group.name)}/${group.floorPlanId}?unitType=${encodeURIComponent(group.name)}&oneSiteId=${group.oneSiteId}" class="btn btn-secondary btn-ghost">View Apartment</a></td>
                </tr>`)
                .forEach(item => tbody.innerHTML += item);
    },
    renderGroupsIntoList: function(groups, pageNumber, listContainer) {
            // clear previous data
            listContainer.innerHTML = '';
            var viewApartmentLink = listContainer.dataset.viewApartmentLink !== null ? listContainer.dataset.viewApartmentLink : './apartment-type.html';
            var propertyStandaloneBaseMapped = listContainer.dataset.propertyStandaloneBaseMapped;
            const groupsToRender = groups.slice((pageNumber-1) * 3, pageNumber * 3);
            this.listGroupsRendered = groupsToRender;
            if (window.getComputedStyle(listContainer.parentNode).display !== "none") {
                this.currentPositionOffset = (pageNumber-1) * 3;
                this.onListingDisplayed(groupsToRender, groups.length);
            }
            const labelByOneSiteId = digitalData.propertyInfo ? this.collectBuildingNamesByOneSiteId(digitalData.propertyInfo.onesiteID)
                : digitalData.collectionInfo ? digitalData.collectionInfo.properties.reduce((acc, prop) => {
                    const buildingNames = this.collectBuildingNamesByOneSiteId(prop.onesiteID)
                    if (Object.keys(buildingNames).length > 0) {
                        acc = {...acc, ...buildingNames}
                    }
                    if (!acc[prop.onesiteID.split(",")[0].split(":")[0].trim()]) {
                        acc[prop.onesiteID.split(",")[0].split(":")[0].trim()] = prop.propertyName
                    }
                    return acc
                }, {}) : {};
            const allNan = groups.every(group => Number.isNaN(group.area));
            groupsToRender.map(group => `<li class="cmp-availability__resultCard">
                    <div class="cmp-availability__resultCard--left">
                        <h3 class="small-title">${group.name}${labelByOneSiteId[group.oneSiteId] ? '<span class="label-text"><br>' + labelByOneSiteId[group.oneSiteId] + '</span>' : ''}</h3>
                        <p class="label-text">
                            <span class="apt-type">${group.type}</span>
                            <span class="apt-divider"> | </span>
                            ${allNan ? '' : `<span class="apt-area">${Number.isNaN(group.area) ? '' : `${group.area} sq. ft.`}</span>`}
                            <span class="apt-rent">Starting at $${group.minRent}/month*</span>
                            <span class="apt-availability">${group.availability > 5 ? 'Yes' : group.availability + " Available"}</span>
                        </p>
                    </div>
                    <div class="cmp-availability__resultCard--right">
                        <p><a href="${digitalData.collectionInfo ? propertyStandaloneBaseMapped + '/'+ digitalData.collectionInfo.properties.find(prop => prop.onesiteID.indexOf(group.oneSiteId) >= 0).propertyId + '/commoncontent.apartment-type.html':viewApartmentLink}/${encodeURIComponent(group.name)}/${group.floorPlanId}?unitType=${encodeURIComponent(group.name)}" class="btn btn-secondary btn-ghost">View Apartment</a></p>
                    </div>
                </li>`)
                .forEach(item => listContainer.innerHTML += item);
    },
    renderPagination: function(groups) {
        const listPagination = document.querySelector(this.selectors.listPagination);
        const tablePagination = document.querySelector(this.selectors.tablePagination);
        const listPageCount = Math.ceil(groups.length/3);
        const tablePageCount = Math.ceil(groups.length/10);
        listPagination.innerHTML = '';
        tablePagination.innerHTML = '';
        if (groups.length > 3) {
            listPagination.innerHTML = this.createPaginationStatus(groups.length, 3) + this.createPaginationButtons(listPageCount);
            this.registerPaginationEvents(groups, listPagination, 3);
        }
        if (groups.length > 10) {
            tablePagination.innerHTML = this.createPaginationStatus(groups.length, 10) + this.createPaginationButtons(tablePageCount);
            this.registerPaginationEvents(groups, tablePagination, 10);
        }
    },
    createPaginationStatus: function(totalItems, itemsPerPage){
        return `<p class="cmp-availability__paginate-status"
                role="status" 
                aria-live="polite">1-${itemsPerPage} of ${totalItems} Results</p>`;
    },
    updatePaginationStatus: function(totalItems, itemsPerPage, pageNumber){
        const startIndex = itemsPerPage * (pageNumber -1) + 1;
        const endIndex = (itemsPerPage * pageNumber < totalItems) ? (itemsPerPage * pageNumber): totalItems ;
        return `${startIndex}-${endIndex} of ${totalItems} Results`;
    },
    createPaginationButtons: function(pageCount) {
        const prevButton = `<div class="cmp-availability__paginate-buttons">
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
    registerPaginationEvents: function(groups, paginationElement, itemsPerPage){
        const totalItems = groups.length;
        const pageCount = Math.ceil(groups.length/itemsPerPage);
        const buttonArray = Array.prototype.slice.call(paginationElement.querySelectorAll("button.paginate-button"));
        const results = document.querySelector(this.selectors.results);
        const resultTbody = document.querySelector(this.selectors.resultTbody);
        const resultList = document.querySelector(this.selectors.resultList);
        const statusElement = paginationElement.querySelector(".cmp-availability__paginate-status");
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
                        if(itemsPerPage === 3) {
                            this.renderGroupsIntoList(groups, currentPageIndex - 1, resultList);
                        }
                        if(itemsPerPage === 10) {
                            this.renderGroupsIntoTable(groups, currentPageIndex - 1, resultTbody);
                        }
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
                        if(itemsPerPage === 3) {
                            this.renderGroupsIntoList(groups, currentPageIndex + 1, resultList);
                        }
                        if(itemsPerPage === 10) {
                            this.renderGroupsIntoTable(groups, currentPageIndex + 1, resultTbody);
                        }
                        statusElement.innerHTML = this.updatePaginationStatus(totalItems, itemsPerPage, currentPageIndex + 1);
                    }
                } else {
                    if (index !== currentPageIndex) {
                        buttonArray[currentPageIndex].classList.remove("current");
                        buttonArray[index].classList.add("current");
                        if(itemsPerPage === 3) {
                            this.renderGroupsIntoList(groups, index, resultList);
                        }
                        if(itemsPerPage === 10) {
                            this.renderGroupsIntoTable(groups, index, resultTbody);
                        }
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
                    results.scrollIntoView({ behavior: 'smooth', block: 'start'});
                }

            });
        });
    },
    registerDataLayerEvents: function() {
        const resultTbody = document.querySelector(this.selectors.resultTbody);
        const resultList = document.querySelector(this.selectors.resultList);
        resultTbody.addEventListener('click', e => {
            if (e.target.matches('a')) {
                const listItem = e.target.closest('tr,li');
                const index = [...listItem.parentNode.children].indexOf(listItem);
                const group = this.tableGroupsRendered[index];
                this.onListingClicked(group, index)
            }
        })
        resultList.addEventListener('click', e => {
            if (e.target.matches('a')) {
                const listItem = e.target.closest('tr,li');
                const index = [...listItem.parentNode.children].indexOf(listItem);
                const group = this.listGroupsRendered[index];
                this.onListingClicked(group, index)
            }
        })
    },
    registerSortingEvents: function() {
        const sortByFieldList = document.querySelector(this.selectors.sortFieldList);
        const resultThead = document.querySelector(this.selectors.resultThead);
        const sortersInTable = resultThead.querySelectorAll("th[data-sort-field]");
        this.updateSortedGroupsData(this.groupsData);
        if (sortByFieldList) {
            sortByFieldList.addEventListener('change', (event)=>{
                if (event.target.value) {
                    this.sortKey = event.target.value;
                    this.sortOrder = 'asc';
                    this.sortedGroupsData.sort(this.compareFunction(event.target.value, 'asc'));
                    this.renderGroupsIntoPage(this.sortedGroupsData);
                    this.renderPagination(this.sortedGroupsData);
                }
            });
        }
        if (sortersInTable) {
            sortersInTable.forEach(sorter=>{
                sorter.addEventListener('click', ()=>{
                    if (sorter.classList.contains("asc")) {
                        sorter.classList.remove("asc");
                        sorter.classList.add("desc");
                        sorter.setAttribute("aria-sort", "descending");
                        this.sortKey = sorter.getAttribute("data-sort-field");
                        this.sortOrder = 'desc';
                        this.sortedGroupsData.sort(this.compareFunction(sorter.getAttribute("data-sort-field"), 'desc'));
                    } else if (sorter.classList.contains("desc")){
                        sorter.classList.remove("desc");
                        sorter.classList.add("asc");
                        sorter.setAttribute("aria-sort", "ascending");
                        this.sortKey = sorter.getAttribute("data-sort-field");
                        this.sortOrder = 'asc';
                        this.sortedGroupsData.sort(this.compareFunction(sorter.getAttribute("data-sort-field"), 'asc'));
                    } else {
                        const currentSortedElement = resultThead.querySelector("th.asc") || resultThead.querySelector("th.desc");
                        currentSortedElement.classList.remove("asc", "desc");
                        currentSortedElement.removeAttribute("aria-sort");
                        sorter.classList.add("asc");
                        sorter.setAttribute("aria-sort", "ascending");
                        this.sortKey = sorter.getAttribute("data-sort-field");
                        this.sortOrder = 'asc';
                        this.sortedGroupsData.sort(this.compareFunction(sorter.getAttribute("data-sort-field"), 'asc'));
                    }
                    this.renderGroupsIntoPage(this.sortedGroupsData);
                    this.renderPagination(this.sortedGroupsData);
                });
            });
        }
    },
    compareFunction: function(sortByField, sortByOrder){
        return (a, b) => {
            if (!a.hasOwnProperty(sortByField) || !b.hasOwnProperty(sortByField)) {
                return 0;
            }
            let comparison = 0;
            if (sortByField === 'type') {
                if(a['bedrooms'] > b['bedrooms']) {
                    comparison = 1;
                } else if(a['bedrooms'] < b['bedrooms']){
                    comparison = -1;
                } else {
                    if (a['bathrooms'] > b['bathrooms']) {
                        comparison = 1;
                    } else if (a['bathrooms'] < b['bathrooms']) {
                        comparison = -1;
                    }
                }
            } else {
                const varA = (typeof a[sortByField] === 'string')
                ? a[sortByField].toUpperCase() : a[sortByField];
                const varB = (typeof b[sortByField] === 'string')
                ? b[sortByField].toUpperCase() : b[sortByField];
                if (varA > varB) {
                    comparison = 1;
                } else if (varA < varB) {
                    comparison = -1;
                }
            }

            return (
                (sortByOrder === 'desc') ? (comparison * -1) : comparison
            );
        };
    },
    monthAfter: function(num) {
        const d = new Date();
        d.setDate(1);
        d.setMonth(d.getMonth()+num);
        return d;
    },
    createFiltersFunction: function(filters) {
        return unit => {
            let result = this.filterUnitMoveInDate(filters.moveindate, unit);
            result = result && (filters.hometype ? filters.hometype.map(type => this.filterUnitHomeType(type, unit)).find(val => val) : true);
            result = result && this.filterUnitMaxRent(filters.maxrent, unit);
            result = result && this.filterUnitMinRent(filters.minrent, unit);
            return result;
        }
    },
    validateMoveInDate: function(filtersForm) {
        const moveInDate = filtersForm.querySelector(this.selectors.moveInDateFilter);
        if(moveInDate && moveInDate.value) {
            filtersForm.classList.remove("move-in-date-empty");
            return true;
        } else {
            filtersForm.classList.add("move-in-date-empty");
            return false;
        }
    },
    registerTabEvents: function() {
        const {self, tabControl, tabPanel} = this.selectors;
        const tabSection = document.querySelector(self);
        const tabControls = Array.prototype.slice.call(tabSection.querySelectorAll(tabControl));
        const tabPanels = Array.prototype.slice.call(tabSection.querySelectorAll(tabPanel));
        if (tabControls && tabControls.length > 0) {
            if (tabControls.length === 1) {
                tabSection.classList.add("one-tab");
            } else {
                tabSection.classList.remove("one-tab");
            }
            tabControls.forEach(tab => {
                tab.addEventListener('click', (event) => {
                    console.log('tab clicked!');
                    const clickedIndex = event.target.getAttribute("data-tab-index");
                    for (let i = 0; i <tabControls.length; i++) {
                        tabControls[i].setAttribute("aria-selected", "false");
                        tabControls[i].setAttribute("tabindex", "-1");
                        tabPanels[i].classList.remove("current");
                        tabPanels[i].classList.add("hidden");
                    }
                    tabControls[clickedIndex].setAttribute("tabindex", "0");
                    tabControls[clickedIndex].setAttribute("aria-selected", "true");
                    tabPanels[clickedIndex].classList.remove("hidden");
                    tabPanels[clickedIndex].classList.add("current");
                });
            });
        }
    },
    parseQuery: function(queryString) {
        const query = {};
        const pairs = (queryString[0] === '?' ? queryString.substr(1) : queryString).split('&');
        for (let i = 0; i < pairs.length; i++) {
            const pair = pairs[i].split('=');
            const key = decodeURIComponent(pair[0]);
            const value = decodeURIComponent(pair[1] || '');
            if (query[key]) {
                query[key].push(value);
            } else {
                query[key] = [value];
            }
        }
        return query;
    },
    filtersKey: function() {
        let key = "portfolio";
        if (digitalData.propertyInfo && digitalData.propertyInfo.onesiteID) {
            key = digitalData.propertyInfo.onesiteID;
        }
        return key;
    },
    saveFilters: function(filters) {
        const key = this.filtersKey();
        const savedFiltersStr = sessionStorage.getItem(this.localStorageFiltersKey);
        const data = savedFiltersStr ? JSON.parse(savedFiltersStr) : {};
        data[key] = filters;
        sessionStorage.setItem(this.localStorageFiltersKey, JSON.stringify(data));
    },
    restoreFilters: function() {
        const savedFiltersStr = sessionStorage.getItem(this.localStorageFiltersKey);
        const data = savedFiltersStr ? JSON.parse(savedFiltersStr) : {};
        return data[this.filtersKey()] || {};
    },
    today: function() {
        const today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        today.setMilliseconds(0);
        return today;
    },
    tomorrow: function() {
        const d = this.today();
        d.setDate(d.getDate() + 1);
        return d;
    },
    defaultMoveInDate() {
        const d = new Date();
        d.setDate(d.getDate() + 1);
        return d;
    },
    retrieveFilterParamsFromQuery: function() {
        const params = this.restoreFilters();
        const query = this.parseQuery(location.search);
        if (query.moveindate && query.moveindate[0]) {
            params.moveindate = query.moveindate[0];
        }
        if (query.minrent && query.minrent[0]) {
            params.minrent = parseFloat(query.minrent[0]);
        }
        if (query.maxrent && query.maxrent[0]) {
            params.maxrent = parseFloat(query.maxrent[0]);
        }
        if (query.hometype) {
            params.hometype = query.hometype;
        }

        const defaultDate = this.defaultMoveInDate();
        if (params.moveindate) {
            params.moveindate = new Date(params.moveindate);
            if (params.moveindate.getTime() < defaultDate.getTime()) {
                params.moveindate = defaultDate;
            }
        }
        console.log("saved filter params: ", params);
        return params;
    },
    collectFiltersFormData: function() {
        const filtersForm = document.querySelector(this.selectors.filters);
        const filters = new FormData(filtersForm);
        const data = {};
        for (var pair of filters.entries()) {
            switch (pair[0]) {
                case 'hometype': {
                    data['hometype'] = data['hometype'] || [];
                    data['hometype'].push(pair[1])
                    break;
                }
                case 'minrent': {
                    if (pair[1]) {
                        data['minrent'] = parseFloat(pair[1]);
                    }
                    break
                }case 'maxrent': {
                    if (pair[1]) {
                        data['maxrent'] = parseFloat(pair[1]);
                    }
                    break
                }
                case 'moveindate': {
                    data['moveindate'] = new Date(pair[1]);
                    break;
                }
            }
        }
        return data;
    },
    isDefaultFilters(filters) {
        if (filters.hometype && filters.hometype.length > 0) {
            return false;
        }
        if (filters.minrent && filters.minrent > 0) {
            return false;
        }
        if (filters.maxrent && filters.maxrent < Infinity) {
            return false;
        }
        if (filters.moveindate && (filters.moveindate.getTime() <= this.today().getTime() || filters.moveindate.getTime() >= this.tomorrow().getTime())) {
            return false;
        }
        return true;
    },
    applyFilters(filters) {
        const results = document.querySelector(this.selectors.results);
        const filtersReset = document.querySelector(this.selectors.clearFilters);
        this.saveFilters(filters)
        filtersReset.disabled = this.isDefaultFilters(filters);
        if (results) {
            this.filterUnits(this.createFiltersFunction(filters), filters.moveindate);
            return true;
        }
        return false;
    },
    registerFiltersToggleEvent: function(){
        const {selectors} = this;
        const filterToggleBtn = document.querySelector(selectors.filterToggleBtn);
        const filterPanel= document.querySelector(selectors.filters);
        if (filterToggleBtn) {
            filterToggleBtn.addEventListener('click', () => {
                filterPanel.classList.toggle('expanded');
                if (filterPanel.classList.contains('expanded')) {
                    filterToggleBtn.setAttribute('aria-expanded', 'true');
                } else {
                    filterToggleBtn.setAttribute('aria-expanded', 'false');
                }
            });
        }

    },
    toggleLoader: function(status){
        const resultWrapper = document.querySelector(this.selectors.resultWrapper);
        const loader = document.querySelector(this.selectors.loader);
        if (loader && resultWrapper){
            if (status == 'show'){
                resultWrapper.classList.add('hidden');
                loader.classList.remove('hidden');
            } else if (status == 'hide'){
                resultWrapper.classList.remove('hidden');
                if (!loader.classList.contains('hidden')){
                    loader.classList.add('hidden');
                }
            }
        } else {
            return;
        }

    },
    init: function(){
        const {selectors} = this;
        const filtersForm = document.querySelector(selectors.filters);
        const tabs = document.querySelector(selectors.tabs);
        const results = document.querySelector(selectors.results);
        
        if (filtersForm) {
            this.registerFilterEvents();
            if (results) {
                this.toggleLoader('show');
                this.fetchUnits();
            }
            filtersForm.addEventListener('submit', e => {
                const isFormValid = this.validateMoveInDate(filtersForm);
                if (!isFormValid) {
                    e.preventDefault();
                } else {
                    const data = this.collectFiltersFormData();
                    if (Object.keys(data).length < Object.keys(this.restoreFilters()).length) {
                        this.onFilterRemoved();
                    } else {
                        this.onFilterAdded();
                    }
                    if (this.applyFilters(data)) {
                        e.preventDefault();
                    }
                }
            });
            if (results) {
                this.registerSortingEvents();
                this.registerDataLayerEvents();
            }
        }

        if (tabs) {
            this.registerTabEvents();
        }
        this.registerFiltersToggleEvent();
    }
};

export default AvailabilityFeed;
