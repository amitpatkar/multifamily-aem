import Tooltip from './_tooltip'
import {POIMapWrapper, POIMarker} from './_poi_map'
import AvailabilityFeed from "./_availability-feed";
import breakpoints from '../site/global/_breakpoints-export.scss';
import Choices from "choices.js";

export class LocationsMarker extends POIMarker {

    constructor() {
        super();
        this.style.display = 'block';

        if (this.oneSiteID) {
            const filterDate = new Date();
            this.units = fetch(AvailabilityFeed.formatAvailabilityURL(this.oneSiteID)).then(response => response.json())
                .then(units => AvailabilityFeed.updateRent(units, filterDate))
                .then(units => units.filter(unit => unit.availability.availableBit))
                .then(units => units.filter(unit => unit.rent && unit.unitDetails && unit.unitDetails.bedrooms));
            this.units.then(units => this.updateUnitsBasedData(units));
        }
    }

    collectAttributes() {
        super.collectAttributes();

        this.oneSiteID = this.getAttribute('one-site-id');
    }

    createInternalComponents() {
        super.createInternalComponents();

        const title = this.querySelector(".cmp-poi-map__category-title").innerHTML;
        const address = this.querySelector(".cmp-poi-map__category-text").innerHTML;
        const img = this.querySelector(".cmp-poi-map__image img").src;

        this.infowindow = new google.maps.InfoWindow({
            content: `<div class="cmp-poi-map__container--popup">
                            <div class="cmp-poi-map__popup-image">
                                <img src="${img}" alt="${title}">
                            </div>
                            <div class="cmp-poi-map__popup-title">
                                ${title}
                            </div>
                            <div class="cmp-poi-map__popup-text">
                                ${address}
                            </div>
                        </div>`
        });
    }

    attachEventListeners() {
        super.attachEventListeners();

        this.infowindow.addListener('closeclick', () => {
            this.onMarkerClick();
        });
    }

    updateUnitsBasedData(units) {
        let bedroomsString = "";
        let minRent = 0;
        if (units.length > 0) {
            const bedrooms = [
                ...new Set(units.map((u) => u.unitDetails.bedrooms)),
            ].sort();
            const smallestBedroom = bedrooms[0] === '0' ? 'Studio' : bedrooms[0];
            const largestBedroom = bedrooms[bedrooms.length - 1];

            bedroomsString = bedrooms.length > 1 ? `${smallestBedroom} - ${largestBedroom} Bed` : (bedrooms[0] === '0' ? 'Studio' : `${bedrooms[0]} Bed`);
            minRent = units.map(u => u.rent).sort()[0];
            this.querySelector('.cmp-poi-map__apartment-type span').innerHTML = bedroomsString;
            this.querySelector('.cmp-poi-map__apartment-price span').innerHTML = minRent;

            this.show();
        } else {
            this.hide();
        }
    }


    onClick() {
        // pass
    }

    handleMarkerClick(handler) {
        this.markerClickHandler = handler;
    }

    onMarkerClick(e) {
        if (this.markerClickHandler) {
            this.markerClickHandler(e);
        } else {
            super.onMarkerClick(e);
        }
    }

    select() {
        if (window.matchMedia(`(min-width: ${breakpoints.breakpointDesktop})`).matches) {
            this.infowindow.open({
                anchor: this.marker,
                map: this.marker.getMap(),
                shouldFocus: false
            });
        }
    }

    deselect() {
        this.infowindow.close();
    }

    applyFilter(filterFn) {
        return this.units
            .then(units => units.filter(filterFn))
            .then(units => {
                this.updateUnitsBasedData(units);
                return units.length > 0;
            });
    }

    show() {
        if (!this.parentNode && this.parentContainer) {
            this.parentContainer.append(this);
        }
    }

    hide() {
        if (this.parentNode) {
            this.deselect();
            this.removeMarkerFromMap();
            this.remove();
        }
    }

    connectedCallback() {
        super.connectedCallback();
        this.parentContainer = this.parentNode;
    }
}

const LocationsMap = {
    selectors: {
        locationsMap: '[data-cmp-component="locationsmap"]',
        categoryButton: 'button.cmp-poi-map__category-button',
        categoryPanel: '.cmp-poi-map__category-panel',
        tooltipEl: '.tooltip-container',
        moveInDateFilter: 'duet-date-picker',
        maxRentFilter: '#availability-filter-max-rent',
        minRentFilter: '#availability-filter-min-rent',
        filters: '#availability-filters',
        locationsPoiMap: 'poi-map',
        locationMarkers: 'locations-marker',
        emptyResultContainer: '.cmp-poi-map--locations--empty-result',
        filtersReset: '.cmp-poi-map--locations--empty-result a',
        filtersResetButton: '#availability_filter_clear'

    },
    cssClasses: {
        button: {
            expanded: "cmp-poi-map__category-button--expanded"
        },
        panel: {
            hidden: "cmp-poi-map__category-panel--hidden",
            expanded: "cmp-poi-map__category-panel--expanded"
        }
    },
    collapseAccordion: function (accordionButton, accordionPanel) {
        const {button, panel} = this.cssClasses;
        accordionButton.classList.remove(button.expanded);
        accordionButton.setAttribute("aria-expanded", "false");
        accordionPanel.classList.remove(panel.expanded);
        if (!accordionPanel.classList.contains(panel.hidden)) {
            accordionPanel.classList.add(panel.hidden);
        }
    },
    expandAccordion: function (accordionButton, accordionPanel) {
        const {button, panel} = this.cssClasses;
        accordionButton.classList.add(button.expanded);
        accordionButton.setAttribute("aria-expanded", "true");
        accordionPanel.classList.remove(panel.hidden);
        if (!accordionPanel.classList.contains(panel.expanded)) {
            accordionPanel.classList.add(panel.expanded);
        }
    },
    checkOtherAccordions: function (accordionButtons, skipIndex) {
        for (let i = 0; i < accordionButtons.length; i++) {
            if (i != skipIndex) {
                const accordionButton = accordionButtons[i];
                const accordionPanel = accordionButton.parentElement.nextElementSibling;
                if (accordionButton.classList.contains(this.cssClasses.button.expanded)) {
                    this.collapseAccordion(accordionButton, accordionPanel);
                }
            }
        }
    },
    registerAccordionEvent: function () {
        const {selectors} = this;
        const {button} = this.cssClasses;
        const mapComponents = document.querySelectorAll(selectors.locationsMap);
        mapComponents.forEach(mapComponent => {
            const accordionButtons = Array.prototype.slice.call(mapComponent.querySelectorAll(selectors.categoryButton));
            for (let i = 0; i < accordionButtons.length; i++) {
                const accordionButton = accordionButtons[i];
                const markerElement = accordionButton.closest('locations-marker');
                const accordionPanel = accordionButton.parentElement.nextElementSibling;
                const handleClick = () => {
                    if (accordionButton.classList.contains(button.expanded)) {
                        this.collapseAccordion(accordionButton, accordionPanel);
                        markerElement.deselect();
                    } else {
                        this.expandAccordion(accordionButton, accordionPanel);
                        markerElement.highlight();
                    }
                    this.checkOtherAccordions(accordionButtons, i);
                };
                accordionButton.addEventListener('click', handleClick);
                markerElement.handleMarkerClick(handleClick);
            }
        });

    },
    collectFiltersData: function() {
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
    registerFilterEvents: function() {
        const filtersForm = document.querySelector(this.selectors.filters);
        const picker = filtersForm.querySelector(this.selectors.moveInDateFilter);
        const minRent = filtersForm.querySelector(this.selectors.minRentFilter);
        const maxRent = filtersForm.querySelector(this.selectors.maxRentFilter);
        const minRentOptions = Array.from(minRent.querySelectorAll('option'));
        const maxRentOptions = Array.from(maxRent.querySelectorAll('option'));
        const filtersReset = document.querySelector(this.selectors.filtersReset);
        const filtersResetButton = document.querySelector(this.selectors.filtersResetButton);

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
        if (picker) {
            const moveInDate = new Date();
            picker.setAttribute("value", moveInDate.toISOString().slice(0, 10));
            picker.setAttribute("min", new Date().toISOString().slice(0, 10));
            picker.addEventListener("duetChange", function(event) {
                if (event.detail.value) {
                    picker.closest("form").classList.remove("move-in-date-empty");
                }
            });
        }
        if (minRent && maxRent) {
            minRent.addEventListener('change', e => {
                const newValue = parseFloat(e.target.value);
                AvailabilityFeed.disableMaxRentBelow.bind(this)(newValue, maxRentChoices, maxRentOptions);
            });
            maxRent.addEventListener('change', e => {
                const newValue = parseFloat(e.target.value);
                AvailabilityFeed.disableMinRentAbove.bind(this)(newValue, minRentChoices, minRentOptions);
            });
        }

        if (filtersReset || filtersResetButton) {
            function triggerChangeEvent(element) {
                if ("createEvent" in document) {
                    var evt = document.createEvent("HTMLEvents");
                    evt.initEvent("change", false, true);
                    element.dispatchEvent(evt);
                }
                else
                    element.fireEvent("onchange");
            }
            const clickEventListener = (e) => {
                e.preventDefault();
                picker.setAttribute("value", new Date().toISOString().slice(0, 10));
                minRentChoices.setChoiceByValue('0');
                maxRentChoices.setChoiceByValue('Infinity');
                triggerChangeEvent(minRent);
                triggerChangeEvent(maxRent);
                filtersForm.querySelectorAll('input[name="hometype"]').forEach(el => {
                    el.checked = false;
                    el.closest('.checkbox-item').classList.remove("checked");
                });
                this.submitFilters({moveindate: new Date()});
            }
            filtersReset && filtersReset.addEventListener('click', clickEventListener);
            filtersResetButton && filtersResetButton.addEventListener('click', clickEventListener);
        }
        if (filtersResetButton) {
            filtersForm.addEventListener('change', () => {
                filtersResetButton.disabled = this.isDefaultFilters(this.collectFiltersData());
            });
            filtersForm.addEventListener('duetChange', () => {
                filtersResetButton.disabled = this.isDefaultFilters(this.collectFiltersData());
            });
            filtersResetButton.disabled = this.isDefaultFilters(this.collectFiltersData());
        }

        filtersForm.addEventListener('submit', e => {
            e.preventDefault();
            const isFormValid = AvailabilityFeed.validateMoveInDate.bind(this)(filtersForm);
            if (isFormValid) {
                const data = this.collectFiltersData();
                this.submitFilters(data);
            }
        });
    },
    submitFilters: function(filters) {
        const map = document.querySelector(this.selectors.locationsPoiMap);
        const emptyResults = document.querySelector(this.selectors.emptyResultContainer);
        const filtersResetButton = document.querySelector(this.selectors.filtersResetButton);

        filtersResetButton.disabled = this.isDefaultFilters(filters);
        const filterFn = AvailabilityFeed.createFiltersFunction(filters);
        Promise.all(this.markers.map(marker => marker.applyFilter(filterFn))).then(values => {
            const hasItems = values.includes(true);
            if (hasItems && map.classList.contains('hidden')) {
                map.classList.remove('hidden');
                emptyResults.classList.add('hidden');
                map.autoCenter();
            } else if (!hasItems && !map.classList.contains('hidden')) {
                map.classList.add('hidden');
                emptyResults.classList.remove('hidden');
            }
        })
    },
    createTooltips: function () {
        Array.from(document.querySelectorAll(this.selectors.tooltipEl)).forEach(element => new Tooltip(element));
    },
    init: function () {
        customElements.define('poi-map', POIMapWrapper);
        customElements.define('locations-marker', LocationsMarker);

        this.markers = Array.from(document.querySelectorAll(this.selectors.locationMarkers));
        this.registerAccordionEvent();
        this.registerFilterEvents();
        this.createTooltips();
    }
};

export default LocationsMap;