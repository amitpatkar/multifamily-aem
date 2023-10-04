import {POIMapWrapper, POIMarker} from "./_poi_map";


export default {
    init: function() {
        customElements.define('poi-map', POIMapWrapper);
        customElements.define('poi-marker', POIMarker);
    }
}