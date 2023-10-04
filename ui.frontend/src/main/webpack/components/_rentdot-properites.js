import _isEqual from 'lodash.isequal';
import Choices from 'choices.js';
import {Utils} from './_utils';

const RentdotProperties = {
    selectors: {
        propertiesComponent: '[data-cmp-component="rentdotproperties"]',
        filtersFormId: 'properties-filters',
        filterToggleBtnId: 'properties-filters-toggle',
        filterPanelId: 'properties-filters-panel',
        propertyFilterId: 'filter-property',
        stateFilterId: 'filter-state',
        cityFilterId: 'filter-city',
        neighborhoodFilterId: 'filter-neighborhood',
        backToTopBtnId: 'back-to-top',
        propertyListId: 'property-list',
        loadMoreBtnId: 'load-more',
        loadMoreContainerId: 'load-more-container',
        hiddenPropertyCard: '.cmp-rentdotProperties__propertyCard.hidden',
        listView: '.cmp-rentdotProperties__listView',
        mapTab: '#map-panel'
    },
    currentPropertyList: [],
    registerFiltersToggleEvent: function(){
        const {selectors} = this;
        const filterToggleBtn = document.getElementById(selectors.filterToggleBtnId);
        const filterPanel= document.getElementById(selectors.filterPanelId);
        filterToggleBtn.addEventListener('click', ()=>{
            filterPanel.classList.toggle('expanded');
            if (filterPanel.classList.contains('expanded')) {
                filterToggleBtn.setAttribute('aria-expanded', 'true');
            } else {
                filterToggleBtn.setAttribute('aria-expanded', 'false');
            }
        });
    },
    getFilterData: function(propertyList) {
      const hierarchyData = [];
      let properties = propertyList.map(property=>property.propertyName).filter(Boolean);
      let states = propertyList.map(property=>property.stateName).filter(Boolean);
      let cities = propertyList.map(property=>property.city).filter(Boolean);
      let neighborhoods = propertyList.flatMap(property=>{
        if (property.neighborhoods) {
          return property.neighborhoods.map(item=>item.title);
        }}).filter(Boolean);
      properties = [...new Set(properties)].sort();
      states= [...new Set(states)].sort();
      cities= [...new Set(cities)].sort();
      neighborhoods= [...new Set(neighborhoods)].sort();

      for(let i = 0; i < propertyList.length; i++) {
        const propertyItem = propertyList[i];
        const matchedItem = hierarchyData.find(item=>item.city == propertyItem.city && item.state == propertyItem.stateName);
        if (!matchedItem) {
          hierarchyData.push({
            city: propertyItem.city, 
            state: propertyItem.stateName, 
            neighborhood: propertyItem.neighborhoods ? propertyItem.neighborhoods.map(item=>item.title) :[],
            property: propertyItem.propertyName? [propertyItem.propertyName] : []
          });
        } else {
          if (propertyItem.neighborhoods) {
            const newNeighborhood = [...new Set(matchedItem.neighborhood.concat(propertyItem.neighborhoods.map(item=>item.title)))];
            matchedItem.neighborhood = newNeighborhood;
          }
          if (propertyItem.propertyName &&  matchedItem.property.indexOf(propertyItem.propertyName) == -1){
            matchedItem.property.push(propertyItem.propertyName);
          }
        } 
      }
      //console.log('new', hierarchyData);
      return {properties, states, cities, neighborhoods, hierarchyData};
    },
    createPropertyCards: function(propertyList) {
    
       const {selectors} = this;
        const propertyListEl = document.getElementById(selectors.propertyListId);
        propertyListEl.innerHTML = '';
        propertyListEl.innerHTML = propertyList.map(function (property, index) {
            return `
        <a class="${index < 9 ? 'cmp-rentdotProperties__propertyCard' : 'cmp-rentdotProperties__propertyCard hidden'}" href="${property.href || ''}">
            <div class="cmp-rentdotProperties__propertyCard-container--image">
                <div class="cmp-rentdotProperties__propertyCard-image">
                    <img src="${property.featuredImage || ''}" alt="${property.propertyName}">
                </div>` + 
                `${property.specialsTeaser ? `<div class="cmp-rentdotProperties__propertyCard-banner">
                    <p>${property.specialsTeaser}</p>
                </div>` : ''}` +
            `</div>
            <div class="cmp-rentdotProperties__propertyCard-container--text">
                <h3 class="cmp-rentdotProperties__propertyCard-name">
                  ${property.collections ? property.propertyName + ' | ' + property.collections[0].title : property.propertyName} 
                </h3>
                <p class="cmp-rentdotProperties__propertyCard-address">${property.city}, ${property.stateName}</p>
                <p class="cmp-rentdotProperties__propertyCard-address">${property.streetAddress || ''}</p>
            </div>
            <div class="cmp-rentdotProperties__propertyCard-container--cta">
                <p class="cmp-rentdotProperties__propertyCard-cta btn btn-secondary btn-ghost">View Property</p>
                <div class="cmp-rentdotProperties__propertyCard-spacer"></div>
            </div>
        </a>
        `;
        }).join('');
        if (propertyList && propertyList.length > 9) {
          document.getElementById(selectors.loadMoreContainerId).innerHTML=`<button id="load-more" class="btn btn-dark">Load More</button>`;
          const loadMoreBtn = document.getElementById(selectors.loadMoreBtnId);
          if (loadMoreBtn) {
              loadMoreBtn.addEventListener('click', ()=>{
                  if (document.querySelectorAll(selectors.hiddenPropertyCard) && document.querySelectorAll(selectors.hiddenPropertyCard).length <= 9){
                      loadMoreBtn.classList.add("hidden");
                  }
                  Array.from(document.querySelectorAll(selectors.hiddenPropertyCard)).slice(0, 9).forEach(card=>{
                      card.classList.remove('hidden');
                  });
              });
          }
          
        } else {
          document.getElementById(selectors.loadMoreContainerId).innerHTML='';
        }
    },
    handleFilterOptions: function(operationType, filterType, optionArray, choiceInstance, selectedValue) {
      let filterElementId;
      switch (filterType) {
        case "property":
          filterElementId = this.selectors.propertyFilterId;
          break;
        case "state":
          filterElementId = this.selectors.stateFilterId;
          break;
        case "city":
          filterElementId = this.selectors.cityFilterId;
          break;
        case "neighborhood" :
          filterElementId = this.selectors.neighborhoodFilterId;
          break;
      }
      const filterEl = document.getElementById(filterElementId);
      switch(operationType) {
        case "initiateOptions" :
          const optionsHtml = optionArray.map(option => `<option value="${option}">${option}</option>`).join('');
          filterEl.innerHTML = '';
          filterEl.innerHTML = `<option value="all">SELECT</option>` + optionsHtml;
          break;
        case "updateOptions":
          if(choiceInstance) {
            if (optionArray.length == 0) {
              choiceInstance.setChoices([{value: "all", label: "SELECT", selected: true, disabled: false}], 'value', 'label', true);
              choiceInstance.disable();
            } else {
              // let selected = selectedValue;
              // if (optionArray.length == 1) {
              //   selected=optionArray[0];
              // }
                choiceInstance.setChoices(this.createChoices(optionArray, selectedValue), 'value', 'label', true);
                choiceInstance.enable();
            }
          }
          break;
        case "updateSelection":
            choiceInstance.setChoiceByValue(selectedValue ? selectedValue : 'all');
            choiceInstance.enable();
          break;
        case "showAll":
          if(choiceInstance) {
            choiceInstance.setChoices(this.createChoices(optionArray), 'value', 'label', true);
            choiceInstance.enable();
          }
          break;
        case "disable":
          if(choiceInstance) {
            choiceInstance.setChoiceByValue('all');
            choiceInstance.disable();
          }
      }

    },
    createChoices: function(optionArray, selectedValue) {
      const choiceArray = [{
        value: "all",
        label: "SELECT",
        selected: optionArray.find(option => selectedValue && option == selectedValue && option != "all") ? false: true,
        disabled: false
      }];
      const choicesObj = optionArray.map(option => {
        return {
            value: option,
            label: option,
            selected: selectedValue && option == selectedValue ? true: false,
            disabled: false
        };
      });
      return choiceArray.concat(choicesObj);
    },
    filterProperties: function(propertyList, propertyFilter, neighborhoodFilter, cityFilter, stateFilter) {
      let result = propertyList;
      if (propertyFilter && propertyFilter != "all") {
        result = propertyList.filter(property => property.propertyName && property.propertyName == propertyFilter);
      }
      if (neighborhoodFilter && neighborhoodFilter != "all") {
        result = propertyList.filter(property => property.neighborhoods && property.neighborhoods.find(item=>item.title == neighborhoodFilter));
      }
      if (cityFilter && cityFilter != "all") {
        result = result.filter(property => property.city && property.city == cityFilter);
      }
      if (stateFilter && stateFilter != "all") {
        result = result.filter(property => property.stateName && property.stateName == stateFilter);
      }
      //console.log('filtered results', propertyFilter, neighborhoodFilter, cityFilter, stateFilter, result);
      return result.length > 0 ? result.sort((a, b)=>(a.propertyName.toLowerCase() > b.propertyName.toLowerCase() ? 1 : -1)): result;
    },
    updatePropertyList: function(propertyList) {
      const filtersForm = document.getElementById(this.selectors.filtersFormId);
      const filterData = new FormData(filtersForm);
      const filters = {};
      for (const pair of filterData.entries()) {
          filters[pair[0]] = pair[1];
          //console.log(pair[0], pair[1]);
      }
      const newPropertyList = this.filterProperties(propertyList, filters['property'], filters['neighborhood'], filters['city'], filters['state']);
      if (!_isEqual(this.currentPropertyList, newPropertyList)) {
        this.currentPropertyList = newPropertyList;
        this.createPropertyCards(newPropertyList);
      }
    },
    // registerScrollTopEvent: function() {
    //     const backToTopBtn = document.getElementById(this.selectors.backToTopBtnId);
    //     backToTopBtn.addEventListener('click', ()=>{
    //         document.body.scrollTop = 0; // For Safari
    //         document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
    //     });

    // },
    init: function(){
        const uniquePropertyIdSet = new Set();
        let propertyList = [];
        // const samplePropertyListData= [
        //   {
        //     "propertyId": "The Lofts at 1835 Arch",
        //     "propertyName": "The Lofts at 1835 Arch",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "1835 Arch Street",
        //     "city": "Philadelphia",
        //     "stateCode": "pa",
        //     "stateName": "Pennsylvania",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": "Enjoy the first month for free",
        //     "neighborhoods": [{id: "center-city", title: "Center City", href: ""},
        //      {id: "logan-square", title: "Logan Square", href: ""}]
        //   },
        //   {
        //     "propertyId": "The Lofts at 1835 Arch",
        //     "propertyName": "The Lofts at 1835 Arch",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "1835 Arch Street",
        //     "city": "Philadelphia",
        //     "stateCode": "pa",
        //     "stateName": "Pennsylvania",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": "Enjoy the first month for free",
        //     "neighborhoods": [{id: "center-city", title: "Center City", href: ""},
        //      {id: "logan-square", title: "Logan Square", href: ""}]
        //   },
        //   {
        //     "propertyId": "Waterside",
        //     "propertyName": "Waterside",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-02.png",
        //     "streetAddress": "25 Waterside Plaza",
        //     "city": "New York City",
        //     "stateCode": "ny",
        //     "stateName": "New York",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "neighborhoods": [{id: "", title: "Midtown", href: ""},
        //     {id: "", title: "Manhattan", href: ""},
        //     {id: "", title: "Murray Hill", href: ""},
        //     {id: "", title: "Kips Bay", href: ""}]
        //   },
        //   {
        //     "propertyId": "the-eugene",
        //     "propertyName": "The Eugene",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-03.png",
        //     "streetAddress": "435 West 31st Street",
        //     "city": "New York City",
        //     "stateCode": "ny",
        //     "stateName": "New York",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": "Enjoy the first month for free",
        //     "neighborhoods": [{id: "", title: "Midtown", href: ""},
        //     {id: "", title: "Hudson Yards", href: ""},
        //     {id: "", title: "Murray Hill", href: ""},
        //     {id: "", title: "Hell's Kitchen", href: ""},
        //     {id: "", title: "Chelsea", href: ""}]
        //   },
        //   {
        //     "propertyId": "the-eugene",
        //     "propertyName": "The Eugene",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-03.png",
        //     "streetAddress": "435 West 31st Street",
        //     "city": "New York City",
        //     "stateCode": "ny",
        //     "stateName": "New York",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": "Enjoy the first month for free",
        //     "neighborhoods": [{id: "", title: "Midtown", href: ""},
        //     {id: "", title: "Hudson Yards", href: ""},
        //     {id: "", title: "Murray Hill", href: ""},
        //     {id: "", title: "Hell's Kitchen", href: ""},
        //     {id: "", title: "Chelsea", href: ""}]
        //   },
        //   {
        //     "propertyId": "Stratford Crossings",
        //     "propertyName": "Stratford Crossings",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "600 Grant Allen Way",
        //     "city": "Wadsworth",
        //     "stateCode": "oh",
        //     "stateName": "Ohio",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //   },
        //   {
        //     "propertyId": "Metro 417",
        //     "propertyName": "Metro 417",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "417 South Hill Street",
        //     "city": "Los Angeles",
        //     "stateCode": "ca",
        //     "stateName": "California",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": "Enjoy the first month for free",
        //     "neighborhoods": [{id: "", title: "Downtown LA", href: ""}]
        //   },
        //   {
        //     "propertyId": "Cameron Court",
        //     "propertyName": "Cameron Court",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "2700 Williamsburg St",
        //     "city": "Alexandria",
        //     "stateCode": "va",
        //     "stateName": "Virginia",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //   },
        //   {
        //     "propertyId": "VYV",
        //     "propertyName": "VYV",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "474 Warren Street",
        //     "city": "Jersey City",
        //     "stateCode": "nj",
        //     "stateName": "New Jersey",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "neighborhoods": [{id: "", title: "Hudson Exchange", href: ""}]
        //   },
        //   {
        //     "propertyId": "Foundry Lofts",
        //     "propertyName": "Foundry Lofts",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-02.png",
        //     "streetAddress": "301 Tingey Street Southeast",
        //     "city": "Washington, DC",
        //     "stateCode": null,
        //     "stateName": "District of Columbia",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "collections": [{id: "", title: "The Yards Collection", href: ""}],
        //     "neighborhoods": [{id: "", title: "Navy Yard", href: ""}]
        //   },
        //   {
        //     "propertyId": "Twelve12",
        //     "propertyName": "Twelve12",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-02.png",
        //     "streetAddress": "1212 4th Street Southeast",
        //     "city": "Washington, DC",
        //     "stateCode": null,
        //     "stateName": "District of Columbia",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "collections": [{id: "", title: "The Yards Collection", href: ""}],
        //     "neighborhoods": [{id: "", title: "Navy Yard", href: ""}]
        //   },
        //   {
        //     "propertyId": "The Olivia",
        //     "propertyName": "The Olivia",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "315 West 33rd Street",
        //     "city": "New York City",
        //     "stateCode": "ny",
        //     "stateName": "New York",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "neighborhoods": [{id: "", title: "Midtown West", href: ""},
        //     {id: "", title: "Hudson Yards", href: ""},
        //     {id: "", title: "Chelsea", href: ""}]
        //   },
        //   {
        //     "propertyId": "Bayside Village",
        //     "propertyName": "Bayside Village",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "3 Bayside Village Place",
        //     "city": "San Francisco",
        //     "stateCode": "ca",
        //     "stateName": "California",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "collections": null,
        //     "neighborhoods": [{id: "", title: "South Beach", href: ""},
        //     {id: "", title: "SoMa", href: ""},
        //     {id: "", title: "Mission Bay", href: ""},
        //     {id: "", title: "Financial District (SF)", href: ""},
        //     {id: "", title: "Union Square", href: ""}]
        //   },
        //   {
        //     "propertyId": "3700M",
        //     "propertyName": "3700M",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "3700 McKinney Avenue",
        //     "city": "Dallas",
        //     "stateCode": "tx",
        //     "stateName": "Texas",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "neighborhoods": [{id: "", title: "Uptown Dallas", href: ""}]
        //   },
        //   {
        //     "propertyId": "The Gardens",
        //     "propertyName": "The Gardens",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "6871 Ames Road",
        //     "city": "Parma",
        //     "stateCode": "oh",
        //     "stateName": "Ohio",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //   },
        //   {
        //     "propertyId": "Origin Ballston",
        //     "propertyName": "Origin Ballston",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "700 North Randolph Street",
        //     "city": "Arlington",
        //     "stateCode": "va",
        //     "stateName": "Virginia",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //   },
        //   {
        //     "propertyId": "One Franklin Town",
        //     "propertyName": "One Franklin Town",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "1 Franklin Town Boulevard",
        //     "city": "Philadelphia",
        //     "stateCode": "pa",
        //     "stateName": "Pennsylvania",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "neighborhoods": [{id: "", title: "Center City", href: ""},
        //     {id: "", title: "Logan Square", href: ""},
        //     {id: "", title: "Museum District (Phila.)", href: ""},
        //     {id: "", title: "Arts District (Phila.)", href: ""},
        //     {id: "", title: "Fairmount", href: ""}]
        //   },
        //   {
        //     "propertyId": "1110 Key Federal Hill",
        //     "propertyName": "1110 Key Federal Hill",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "1110 Key Hwy.",
        //     "city": "Baltimore",
        //     "stateCode": "md",
        //     "stateName": "Maryland",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "neighborhoods": [{id: "", title: "Federal Hill", href: ""}]
        //   },
        //   {
        //     "propertyId": "The Grand",
        //     "propertyName": "The Grand",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "5801 Nicholson Lane",
        //     "city": "North Bethesda",
        //     "stateCode": "md",
        //     "stateName": "Maryland",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //   },
        //   {
        //     "propertyId": "Thayer and Spring",
        //     "propertyName": "Thayer and Spring",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "915 Silver Spring Avenue",
        //     "city": "Silver Spring",
        //     "stateCode": "md",
        //     "stateName": "Maryland",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //   },
        //   {
        //     "propertyId": "Mosso",
        //     "propertyName": "Mosso",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "900 Folsom Street",
        //     "city": "San Francisco",
        //     "stateCode": "ca",
        //     "stateName": "California",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "neighborhoods": [{id:"", title: "SoMa", href: ""}]
        //   },
        //   {
        //     "propertyId": "One Blue Slip | Greenpoint Landing",
        //     "propertyName": "One Blue Slip | Greenpoint Landing",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "1 Blue Slip",
        //     "city": "New York City",
        //     "stateCode": "ny",
        //     "stateName": "New York",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "neighborhoods": [{id:"", title: "Greenpoint Landing", href: ""}]
        //   },
        //   {
        //     "propertyId": "Two Blue Slip | Greenpoint Landing",
        //     "propertyName": "Two Blue Slip | Greenpoint Landing",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "2 Blue Slip",
        //     "city": "New York City",
        //     "stateCode": "ny",
        //     "stateName": "New York",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "neighborhoods": [{id: "", title: "Greenpoint Landing", href:""}]
        //   },
        //   {
        //     "propertyId":"Vantage Pointe",
        //     "propertyName": "Vantage Pointe",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "1281 9th Ave",
        //     "city": "San Diego",
        //     "stateCode": "ca",
        //     "stateName": "California",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null
        //   },
        //   {
        //     "propertyId": "Winchester Lofts",
        //     "propertyName": "Winchester Lofts",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "275 Winchester Avenue",
        //     "city": "New Haven",
        //     "stateCode": "ct",
        //     "stateName": "Connecticut",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "neighborhoods": [{id: "", title: "Prospect Hill", href: ""}]
        //   },
        //   {
        //     "propertyId": "The River Lofts at Ashton Mill",
        //     "propertyName": "The River Lofts at Ashton Mill",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "51 Front Street",
        //     "city": "Cumberland",
        //     "stateCode": "ri",
        //     "stateName": "Rhode Island",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //   },
        //   {
        //     "propertyId": "Radian",
        //     "propertyName": "Radian",
        //     "featuredImage": "/etc.clientlibs/brookfieldpropertiesprogram/clientlibs/clientlib-site/resources/images/propertycover-sample-01.png",
        //     "streetAddress": "120 Kingston Street",
        //     "city": "Boston",
        //     "stateCode": "ma",
        //     "stateName": "Massachusetts",
        //     "href": null,
        //     "onesiteID": null,
        //     "postalCode": null,
        //     "specialsTeaser": null,
        //     "neighborhoods": [{id: "", title: "The Greenway", href:""}]
        //   },
        // ];
        // propertyList = samplePropertyListData.filter(obj=>{
        //   const hasPropertyId = uniquePropertyIdSet.has(obj.propertyId);
        //   uniquePropertyIdSet.add(obj.propertyId);
        //   return !hasPropertyId;
        // }).sort((a, b)=>(a.propertyName.toLowerCase() > b.propertyName.toLowerCase() ? 1 : -1));

        if (window.digitalData && window.digitalData.propertyList && window.digitalData.propertyList.properties) {
          propertyList = digitalData.propertyList.properties.filter(obj=>{
            const hasPropertyId = uniquePropertyIdSet.has(obj.propertyId);
            uniquePropertyIdSet.add(obj.propertyId);
            return !hasPropertyId;
          }).sort((a, b)=>(a.propertyName.toLowerCase() > b.propertyName.toLowerCase() ? 1 : -1));
        }
        //console.log(propertyList);
        if (propertyList.length > 0) {
            document.querySelector(this.selectors.listView).classList.remove("hidden");
            const {selectors} = this;
            const propertyFilter = document.getElementById(selectors.propertyFilterId);
            const stateFilter = document.getElementById(selectors.stateFilterId);
            const cityFilter = document.getElementById(selectors.cityFilterId);
            const neighborhoodFilter = document.getElementById(selectors.neighborhoodFilterId);
            const filtersForm = document.getElementById(selectors.filtersFormId);
            this.currentPropertyList = propertyList;
            
            // this.registerScrollTopEvent();
            this.registerFiltersToggleEvent();
            this.createPropertyCards(propertyList);
            const {properties, states, cities, neighborhoods, hierarchyData} = this.getFilterData(propertyList);
            this.handleFilterOptions('initiateOptions', 'property', properties); 
            this.handleFilterOptions('initiateOptions', 'state', states); 
            this.handleFilterOptions('initiateOptions', 'city', cities);
            this.handleFilterOptions('initiateOptions', 'neighborhood', neighborhoods);
            const choiceConfig = {
              searchEnabled: true, 
              itemSelectText: '', 
              shouldSort: false, 
              searchFields: ['label'],
              fuseOptions: {
                threshold: 0.1,
                distance: 1000
              },
              classNames: {
                selectedState: 'is-selected',
                highlightedState: 'is-highlight'
              }
            }
            const propertyChoices = new Choices(propertyFilter, choiceConfig);
            const stateChoices = new Choices(stateFilter, choiceConfig);
            const cityChoices = new Choices(cityFilter, choiceConfig);
            const neighborhoodChoices = new Choices(neighborhoodFilter, choiceConfig);
            this.handleFilterOptions('disable', 'city', null, cityChoices);
            this.handleFilterOptions('disable', 'neighborhood', null, neighborhoodChoices);

            propertyFilter.addEventListener('change', (event) => {
              let selectedState = 'all';
              let selectedCity = 'all';
              let selectedNeighborhood = 'all';
              const selectedProperty = event.target.value;
              if (selectedProperty != "all") {
                const filteredProperties = propertyList.filter(el=> el.propertyName && el.propertyName == selectedProperty);

                //populate state if there is only one matching value
                const stateByProperty = filteredProperties.map(item => item.stateName);
                if (stateByProperty.length == 1) {
                  selectedState = stateByProperty[0];
                }
                this.handleFilterOptions('updateSelection', 'state', null, stateChoices, selectedState);

                //populate city if there is only one matching value
                const citybyProperty = filteredProperties.map(item => item.city);
                if (citybyProperty.length == 1) {
                  selectedCity = citybyProperty[0];
                }
                this.handleFilterOptions('updateSelection', 'city', null, cityChoices, selectedCity);
                // const filteredCities = hierarchyData.filter(el=> stateByProperty.indexOf(el.state) != -1 ).map(item => item.city);
                // this.handleFilterOptions('updateOptions', 'city', filteredCities, cityChoices, selectedCity);

                //populate neighborhood if there is only one matching value
                const neighborhoodbyProperty =  filteredProperties.flatMap(property=>{
                  if (property.neighborhoods) {
                    return property.neighborhoods.map(item=>item.title);
                  }});
                  //console.log('neighborhood by Property', neighborhoodbyProperty);
                if (neighborhoodbyProperty.length == 1) {
                  selectedNeighborhood = neighborhoodbyProperty[0];
                }
                this.handleFilterOptions('updateSelection', 'neighborhood', null, neighborhoodChoices, selectedNeighborhood);
                // const filteredNeighborhoods= hierarchyData.filter(el=> stateByProperty.indexOf(el.state) != -1 ).flatMap(item => item.neighborhood);
                // this.handleFilterOptions('updateOptions', 'neighborhood', filteredNeighborhoods, neighborhoodChoices, selectedNeighborhood);
              } 
              this.updatePropertyList(propertyList);
            });

            stateFilter.addEventListener('change', (event) => {
              const selectedState = event.target.value;
              if (selectedState != "all") {
                  const filteredProperties = hierarchyData.filter(el=>el.state == selectedState).flatMap(item => item.property);
                  this.handleFilterOptions('updateOptions', 'property', filteredProperties, propertyChoices);
                  const filteredCities = hierarchyData.filter(el=>el.state == selectedState).map(item => item.city);
                  this.handleFilterOptions('updateOptions', 'city', filteredCities, cityChoices);
                  const filteredNeighborhoods = hierarchyData.filter(el=>el.state == selectedState).flatMap(item => item.neighborhood);
                  this.handleFilterOptions('updateOptions', 'neighborhood', filteredNeighborhoods, neighborhoodChoices);
              } else {
                this.handleFilterOptions('showAll', 'property', properties, propertyChoices);
                this.handleFilterOptions('showAll', 'city', cities, cityChoices);
                this.handleFilterOptions('showAll', 'neighborhood', neighborhoods, neighborhoodChoices);
                this.handleFilterOptions('disable', 'city', null, cityChoices);
                this.handleFilterOptions('disable', 'neighborhood', null, neighborhoodChoices);
              }
              this.updatePropertyList(propertyList);
            });
            cityFilter.addEventListener('change', (event) => {
              const selectedCity = event.target.value;
              if (selectedCity != "all") {
                const filteredStates = hierarchyData.filter(el=> el.city == selectedCity).map(item => item.state);
                if (filteredStates.length > 1) {
                    this.handleFilterOptions('updateSelection', 'state', null, stateChoices, "all");
                } 

                const filteredNeighborhoods = hierarchyData.filter(el => { 
                  return stateFilter.value == "all" ? (el.city == selectedCity) : (el.state == stateFilter.value && el.city == selectedCity); 
                }).flatMap(item => item.neighborhood);

                this.handleFilterOptions('updateOptions', 'neighborhood', filteredNeighborhoods, neighborhoodChoices);

                const filteredProperties = hierarchyData.filter(el => { 
                  return stateFilter.value == "all" ? (el.city == selectedCity) : (el.state == stateFilter.value && el.city == selectedCity); 
                }).flatMap(item => item.property);
                this.handleFilterOptions('updateOptions', 'property', filteredProperties, propertyChoices);
              } 
              else {
                if (stateFilter.value != "all") {
                  const filteredNeighborhoods = hierarchyData.filter(el => el.state == stateFilter.value).flatMap(item => item.neighborhood);
                  this.handleFilterOptions('updateOptions', 'neighborhood', filteredNeighborhoods, neighborhoodChoices, 'all');

                  const filteredProperties = hierarchyData.filter(el => el.state == stateFilter.value).flatMap(item => item.property);
                  this.handleFilterOptions('updateOptions', 'property', filteredProperties, propertyChoices, 'all');
                } else {
                  this.handleFilterOptions('showAll', 'property', properties, propertyChoices);
                  this.handleFilterOptions('showAll', 'city', cities, cityChoices);
                  this.handleFilterOptions('showAll', 'neighborhood', neighborhoods, neighborhoodChoices);
                }
              }
              this.updatePropertyList(propertyList);
              
            });
            neighborhoodFilter.addEventListener('change', (event) => {
              const selectedNeighborhood = event.target.value;
              if (selectedNeighborhood != "all") {
                  const filteredStates = hierarchyData.filter(el=> el.neighborhood.indexOf(selectedNeighborhood) != -1 ).map(item => item.state);
                  if (filteredStates.length == 1) {
                    this.handleFilterOptions('updateSelection', 'state', null, stateChoices, filteredStates[0]);
                  } else if (filteredStates.length > 1){
                    this.handleFilterOptions('updateSelection', 'state', null, stateChoices, "all");
                  }
                  const filteredCities = hierarchyData.filter(el=> el.neighborhood.indexOf(selectedNeighborhood) != -1 ).map(item => item.city);
                  if (filteredCities.length == 1) {
                    this.handleFilterOptions('updateSelection', 'city', null, cityChoices, filteredCities[0]);
                  } else if (filteredCities.length > 1) {
                    this.handleFilterOptions('updateSelection', 'city', null, cityChoices, "all");
                  }
                  //console.log('selected neighborhood: ', selectedNeighborhood);
                  const filteredProperties = propertyList.filter(el=> el.neighborhoods && el.neighborhoods.map(n=>n.title).indexOf(selectedNeighborhood) != -1)
                  .map(item => item.propertyName);
                 // console.log('filteredProperties by neighborohood: ', filteredProperties)
                  this.handleFilterOptions('updateOptions', 'property', filteredProperties, propertyChoices, 'all');
              } else {
                let filteredResults = hierarchyData;
                if (cityFilter.value != 'all'){
                  filteredResults = filteredResults.filter(el => el.city == cityFilter.value);
                }
                if (stateFilter.value != 'all'){
                  filteredResults = filteredResults.filter(el => el.state == stateFilter.value);
                }
                const filteredProperties = filteredResults.flatMap(item => item.property);
                this.handleFilterOptions('updateOptions', 'property', filteredProperties, propertyChoices, 'all');
              }
              this.updatePropertyList(propertyList);
            });
            filtersForm.onsubmit = (event) => {
                event.preventDefault();
                stateChoices.setChoiceByValue("all");
                this.handleFilterOptions('showAll', 'property', properties, propertyChoices);
                this.handleFilterOptions('showAll', 'city', cities, cityChoices);
                this.handleFilterOptions('showAll', 'neighborhood', neighborhoods, neighborhoodChoices);
                this.handleFilterOptions('disable', 'city', null, cityChoices);
                this.handleFilterOptions('disable', 'neighborhood', null, neighborhoodChoices);
                this.createPropertyCards(propertyList);
            }

        } else {
          document.querySelector(this.selectors.listView).classList.add("hidden");
        }

        const mapTab = document.querySelector(this.selectors.mapTab);
        const observer = Utils.listenChanges(mapTab, {attributes: true, attributeFilter: ['class']}, () => {
          if (!Utils.hasClass(mapTab, 'hidden')) {
            mapTab.querySelector('poi-map').autoCenter();
            observer.disconnect();
          }
        });

    }

};
export default RentdotProperties;