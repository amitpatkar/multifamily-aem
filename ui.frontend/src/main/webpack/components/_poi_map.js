import breakpoints from "../site/global/_breakpoints-export.scss";
import {getColor} from "./_utils";

const ClusterRenderer = {

    render: ({count, position}) => {
        // change color if this cluster has more markers than the mean cluster
        const color = '#00FF00';

        // create svg url with fill color
        const svg = window.btoa(`
  <svg fill="${color}" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 240 240">
    <circle cx="120" cy="120" opacity="1" r="90" />
  </svg>`);

        // create marker using svg icon
        return new google.maps.Marker({
            position,
            icon: {
                url: `data:image/svg+xml;base64,${svg}`,
                scaledSize: new google.maps.Size(45, 45),
            },
            label: {
                text: String(count),
                color: "#000000",
                fontSize: "12px",
            },
            // adjust zIndex to be above other markers
            zIndex: Number(google.maps.Marker.MAX_ZINDEX) + count,
        });
    }
}


export class POIMapWrapper extends HTMLElement {
    constructor() {
        super();

        const mapID = this.getAttribute("map-id");
        const zoom = this.hasAttribute("zoom") ? parseInt(this.getAttribute("zoom")) : 15;
        const autocenter = this.hasAttribute("autocenter") && this.getAttribute("autocenter") !== 'false';
        const noCluster = this.hasAttribute("no-cluster");

        const mapElement = this.querySelector(".map");
        if (mapElement != null) {
            this.map = new google.maps.Map(mapElement, {
                mapId: mapID,
                zoom: zoom,
                mapTypeControl: false,
                streetViewControl: false,
                rotateControl: false
            });

            if (!noCluster) {
                this.clusterer = new markerClusterer.MarkerClusterer({
                    map: this.map,
                    markers: [],
                    renderer: ClusterRenderer,
                });
            } else {
                this.markers = [];
            }

            this.autocenter = autocenter;
        }

        this.addEventListener('poi-marker-added', (e) => {
            const marker = e.detail.marker;
            if (this.clusterer) {
                this.clusterer.addMarker(marker);
            } else {
                this.markers.push(marker);
                marker.setMap(this.map);
            }

            if (this.autocenter) {
                this.autoCenter();
            } else if (e.detail.center) {
                this.map.setCenter(marker.getPosition());
            }
        });

        this.addEventListener('poi-marker-removed', (e) => {
            const marker = e.detail.marker;
            if (this.clusterer) {
                this.clusterer.removeMarker(marker);
            } else if (this.markers.indexOf(marker) >= 0){
                this.markers.splice(this.markers.indexOf(marker), 1);
                marker.setMap(null);
            }
        });

        this.addEventListener('poi-marker-highlight', e => {
            if (this.highlighted) {
                this.highlighted.deselect();
            }
            this.highlighted = e.detail;
            this.highlighted.select();

            this.map.panTo(this.highlighted.marker.getPosition());
            if (this.highlighted.zoom && this.highlighted.zoom !== this.map.getZoom()) {
                this.map.setZoom(this.highlighted.zoom);
            }
        });
    }

    autoCenter() {
        const bounds = new google.maps.LatLngBounds();
        (this.clusterer ? this.clusterer.markers : this.markers).forEach(marker => bounds.extend(marker.position));
        this.map.setCenter(bounds.getCenter());
        this.map.fitBounds(bounds);
    }
}

export class POIMarker extends HTMLElement {
    constructor() {
        super();

        this.collectAttributes();
        this.createInternalComponents();
        this.attachEventListeners();
    }

    collectAttributes() {
        this.lat = this.getAttribute('lat');
        this.lng = this.getAttribute('lng');
        this.zoom = this.hasAttribute('zoom') ? parseInt(this.getAttribute('zoom')) : null;
        this.href = this.getAttribute('href');
        this.color = getColor(this.getAttribute('color'));
        this.activeColor = getColor(this.getAttribute("active-color")) || this.color;
        this.center = this.hasAttribute('center');
        this.initHidden = this.hasAttribute('init-hidden');
    }

    createIcon(color) {
        return {
            path: `M0 -30a10 10 0 0 0-10 10c0 9 10 20 10 20s10-11 10-20a10 10 0 0 0-10-10zm0 12a2 2 0 1 1 2-2 2 2 0 0 1-2 2z`,
            fillColor: color || '#aa0000',
            fillOpacity: 1,
            scale: 1.1,
        }
    }

    createInternalComponents() {
        this.marker = new google.maps.Marker({
            position: new google.maps.LatLng(this.lat, this.lng),
            icon: this.createIcon(this.color)
        });

        const infoWindowComponent = this.querySelector('info-window');
        if (infoWindowComponent && infoWindowComponent.children[0]) {
            this.infowindow = new google.maps.InfoWindow({
                content: infoWindowComponent.children[0]
            });
        }
    }

    attachEventListeners() {
        this.addEventListener('click', this.onClick.bind(this));
        this.marker.addListener('click', this.onMarkerClick.bind(this));

        if (this.infowindow) {
            this.infowindow.addListener('closeclick', () => {
                this.deselect();
            });
        }
    }

    onMarkerClick(e) {
        if (this.href) {
            window.open(this.href, '_blank').focus();
        } else {
            this.highlight();
        }
    }

    onClick(e) {
        this.highlight();
    }

    highlight() {
        this.dispatchEvent(new CustomEvent("poi-marker-highlight", {
            bubbles: true,
            detail: this
        }));
    }

    select() {
        this.classList.add("selected");
        this.openInfoWindow();
    }

    deselect() {
        this.classList.remove("selected");
        this.closeInfoWindow();
    }

    openInfoWindow() {
        if (window.matchMedia(`(min-width: ${breakpoints.breakpointDesktop})`).matches && this.infowindow) {
            this.infowindow.open({
                anchor: this.marker,
                map: this.marker.getMap(),
                shouldFocus: false
            });
            this.marker.setIcon(this.createIcon(this.activeColor));
            this._mapClickListener = this.marker.getMap().addListener("click", () => this.closeInfoWindow());
        }
    }

    closeInfoWindow() {
        if (this.infowindow){
            this.infowindow.close();
            this.marker.setIcon(this.createIcon(this.color));
            if (this._mapClickListener) {
                google.maps.event.removeListener(this._mapClickListener);
            }
        }
    }

    connectedCallback() {
        if (!this.initHidden){
            this.addMarkerToMap();
        }
    }

    addMarkerToMap() {
        this.dispatchEvent(new CustomEvent("poi-marker-added", {
            bubbles: true,
            detail: {
                zoom: this.zoom,
                center: this.center,
                marker: this.marker
            }
        }));
    }

    removeMarkerFromMap() {
        this.dispatchEvent(new CustomEvent("poi-marker-removed", {
            bubbles: true,
            detail: {
                marker: this.marker
            }
        }));
    }
}

const POIMap = {
    selectors: {
        poiMap: '[data-cmp-component="poimap"]',
        categoryButton: 'button.cmp-poi-map__category-button',
        categoryPanel: '.cmp-poi-map__category-panel',
        showMoreBtn: '.cmp-poi-map__showmore button'
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
    collapseAccordion: function(accordionButton, accordionPanel) {
        const {button, panel} = this.cssClasses;
        accordionButton.classList.remove(button.expanded);
        accordionButton.setAttribute("aria-expanded", "false");
        accordionPanel.classList.remove(panel.expanded);
        if (!accordionPanel.classList.contains(panel.hidden)) {
            accordionPanel.classList.add(panel.hidden);
        }
        [...accordionPanel.querySelectorAll('poi-marker')].forEach((marker) => {
            marker.removeMarkerFromMap();
        });
        const showMoreButton = accordionPanel.querySelector(this.selectors.showMoreBtn);
        if (showMoreButton) {
            const panelId = showMoreButton.getAttribute('data-panel-id');
            if (panelId && document.getElementById(panelId)) {
                document.getElementById(panelId).classList.add('show-less');
                showMoreButton.parentElement.classList.remove('hidden');
            }
        }
    },
    expandAccordion: function(accordionButton, accordionPanel) {
        const {button, panel} = this.cssClasses;
        accordionButton.classList.add(button.expanded);
        accordionButton.setAttribute("aria-expanded", "true");
        accordionPanel.classList.remove(panel.hidden);
        if (!accordionPanel.classList.contains(panel.expanded)) {
            accordionPanel.classList.add(panel.expanded);
        }
        [...accordionPanel.querySelectorAll('poi-marker')].forEach((marker) => {
            marker.addMarkerToMap();
        });
    },
    checkOtherAccordions: function(accordionButtons, skipIndex) {
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
    registerAccordionEvent: function() {
        const {selectors} = this;
        const {button} = this.cssClasses;
        const poiMapComponents = document.querySelectorAll(selectors.poiMap);
        poiMapComponents.forEach(poiMapComponent => {
            const accordionButtons = Array.prototype.slice.call(poiMapComponent.querySelectorAll(selectors.categoryButton));
            for (let i = 0; i < accordionButtons.length; i++) {
                const accordionButton = accordionButtons[i];
                const accordionPanel = accordionButton.parentElement.nextElementSibling;
                accordionButton.addEventListener('click', () => {
                    if (accordionButton.classList.contains(button.expanded)) {
                        this.collapseAccordion(accordionButton, accordionPanel);
                    } else {
                        this.expandAccordion(accordionButton, accordionPanel);
                        setImmediate(() => poiMapComponent.querySelector('poi-map').autoCenter());
                    }
                    this.checkOtherAccordions(accordionButtons, i);
                });
            }
        });

    },
    registerShowMoreEvent: function(){
        const {selectors} = this;
        const poiMapComponents = document.querySelectorAll(selectors.poiMap);
        poiMapComponents.forEach(poiMapComponent => {
            const showMoreBtns = poiMapComponent.querySelectorAll(selectors.showMoreBtn);
            showMoreBtns.forEach(button => {
                button.addEventListener('click', () => {
                    const panelId = button.getAttribute('data-panel-id');
                    if (panelId && document.getElementById(panelId)) {
                        document.getElementById(panelId).classList.remove('show-less');
                        button.parentElement.classList.add('hidden');
                    }
                });
            });
        });

    },
    init: function () {
        customElements.define('poi-map', POIMapWrapper);
        customElements.define('poi-marker', POIMarker);
        this.registerAccordionEvent();
        this.registerShowMoreEvent();
    }
};

export default POIMap;