import ListboxCombobox from "./_listbox-combobox";
const RentdotSearch = {
    selectors: {
        searchComponent: '[data-cmp-component="rentdotsearch"]',
        comboboxId: 'autocomplete-combobox',
        searchInputId: 'autocomplete-input',
        suggestionsId: 'autocomplete-suggestions',
        formId: 'autocomplete-form',
    },
    init: function(){
        const form = document.getElementById(this.selectors.formId);
        const input = document.getElementById(this.selectors.searchInputId);
        const listbox = document.getElementById(this.selectors.suggestionsId);

        let properties = digitalData.searchInputFieldModel.properties.map(prop => prop.title);

        const cities = (digitalData.searchInputFieldModel.cities || []).map(city => ({...city, title: `${city.title}, ${city.stateCode}`}));
        //const locationItems = [].concat(cities, digitalData.searchInputFieldModel.neighborhoods || [], digitalData.searchInputFieldModel.states || [], digitalData.searchInputFieldModel.additionalSearchTerms || []);
        //remove state, add metro areas and additionalSearchTerms
        const locationItems = [].concat(cities, digitalData.searchInputFieldModel.collections || [],  digitalData.searchInputFieldModel.neighborhoods || [],  digitalData.searchInputFieldModel.additionalSearchTerms || []);
        let locations = locationItems.map(coll => coll.title);

        properties = properties.sort();
        locations = locations.sort();

        function searchFn(searchString) {
            const locationResults = [];
            const propertyResults = [];
            for (let i = 0; i < locations.length; i++) {
                const location = locations[i].toLowerCase();
                if (location.indexOf(searchString.toLowerCase()) >= 0) {
                    locationResults.push(locations[i]);
                }
            }
            for (let i = 0; i < properties.length; i++) {
                const property = properties[i].toLowerCase();
                if (property.indexOf(searchString.toLowerCase()) >= 0) {
                    propertyResults.push(properties[i]);
                }
            }
            return {locationResults, propertyResults};
        }

        function findItem(text) {
            return digitalData.searchInputFieldModel.properties.concat(locationItems).find(p => p.title === text);
        }

        function searchPerformed(value) {
            appEventData.push({
                "event": "Onsite Search Performed",
                "onsiteSearch": {
                    "keyword": {
                        "searchTerm": value,
                        "searchType": "locations"
                    }
                }
            });
            const item = findItem(value);
            if (item && item.href) {
                location.href =  portfolioBaseMapped + item.href+(item.href.includes("?") ? "&" : "?")+"search_term="+value;
            } else {
                appEventData.push({
                    "event": "Onsite Search Failed",
                    "onsiteSearch": {
                        "keyword": {
                            "searchTerm": value,
                            "searchType": "locations"
                        }
                    }
                });
            }
        }

        form.addEventListener("submit", e => {
            e.preventDefault();

            searchPerformed(input.value)
        })

        listbox.addEventListener("click", () => {
            setTimeout(() => {
                searchPerformed(input.value)
            }, 500)
        })

        const searchComponent = new ListboxCombobox(
            document.getElementById(this.selectors.comboboxId),
            document.getElementById(this.selectors.searchInputId),
            listbox,
            searchFn,
            false
        );
    },


}
export default RentdotSearch;