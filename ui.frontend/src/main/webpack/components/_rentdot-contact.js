import Choices from 'choices.js';

const RentdotContact = {
    selectors: {
        self: '[data-cmp-component="rentdotcontact"]',
        contactForm: '#rentdot_contact_form',
        firstName: "#first_name",
        lastName: "#last_name",
        emailAddress: '#email_address',
        phoneNumber: '#phone_number',
        building: '#building',
        message: '#message',
        messageLabelAsterisk: 'label[for="message"] sup',
        moveInDate: '#move_in_date',
        typeOfRequest: '#type_of_request',
        formErrorMessage: '#contact_form_error',
        postSubmissionContents: '.cmp-rentdotContact__postsubmission',
        confirmationName: '.cmp-rentdotContact__confirmation--name',
    },
    extraFormParams: [":cq_csrf_token", "building"],
    dataLayer: {
        formStarted: false,
        form: {
            formName: "Contact Us",
            formType: "Contact"
        },
        product: window.digitalData && window.digitalData.propertyInfo ? [{
            "productInfo": {
                "name": window.digitalData.propertyInfo.propertyName,
                "productID": window.digitalData.propertyInfo.onesiteID
            },
            "location": {
                "locationId": window.digitalData.propertyInfo.onesiteID,
                "locationName": window.digitalData.propertyInfo.propertyName
            }
        }] : window.digitalData && window.digitalData.collectionInfo ? [{
            "collection": {
                "collectionId": window.digitalData.collectionInfo.id,
                "collectionName": window.digitalData.collectionInfo.name
            },
            "location": {
                "locationId": window.digitalData.collectionInfo.id,
                "locationName": window.digitalData.collectionInfo.name
            }
        }] : []
    },
    validateForm: function (elements) {
        const results = {};
        let isFormValid = true;
        for (let i = 0; i < elements.length; i++) {
            results[elements[i].id] = !!(elements[i].value && elements[i].value.trim());
            isFormValid = isFormValid && results[elements[i].id]
        }
        results['isFormValid'] = isFormValid;
        return results;
    },
    registerTypeChangeEvent: function () {
        const contactForm = document.querySelector(this.selectors.contactForm);
        const typeOfRequest = contactForm.querySelector(this.selectors.typeOfRequest);
        const moveInDateContainer = contactForm.querySelector('.move-in-date');
        const buildingContainer = contactForm.querySelector('.building');
        const messageLabelAsterisk = contactForm.querySelector(this.selectors.messageLabelAsterisk);
        typeOfRequest.addEventListener('change', () => {
            switch (typeOfRequest.value) {
                case 'new-home':
                    moveInDateContainer.classList.remove('hidden');
                    buildingContainer.classList.remove('hidden');
                    if (!messageLabelAsterisk.classList.contains('hidden')) {
                        messageLabelAsterisk.classList.add('hidden');
                    }
                    break;
                case 'current-resident':
                    if (!moveInDateContainer.classList.contains('hidden')) {
                        moveInDateContainer.classList.add('hidden');
                    }
                    buildingContainer.classList.remove('hidden');
                    if (!messageLabelAsterisk.classList.contains('hidden')) {
                        messageLabelAsterisk.classList.add('hidden');
                    }
                    break;
                case 'general-inquiry':
                    if (!moveInDateContainer.classList.contains('hidden')) {
                        moveInDateContainer.classList.add('hidden');
                    }
                    if (!buildingContainer.classList.contains('hidden')) {
                        buildingContainer.classList.add('hidden');
                    }
                    building.classList.remove("invalid");
                    messageLabelAsterisk.classList.remove('hidden');
                    break;
            }
        });
    },
    initDataLayer: function () {
        const form = document.querySelector(this.selectors.self + ' ' + this.selectors.contactForm);
        //console.log('form: ', form);
        form.addEventListener("change", () => {
            if (!this.dataLayer.formStarted) {
                this.dataLayer.formStarted = true;
                const event = {
                    "event": "Form Started",
                    "form": this.dataLayer.form
                }
                if (this.dataLayer.product.length > 0) {
                    event.product = this.dataLayer.product
                }
                window.appEventData.push(event);
                window.appEventData.push({
                    "event": "Contact Us Started"
                });
            }
        })
    },
    onFormSubmitted: function() {
        window.appEventData.push({
            "event": "Contact Us Completed"
        });
    },
    onFormSubmissionSucceeded: function() {
        const event = {
            "event": "Form Submission Succeeded",
            "form": this.dataLayer.form
        }
        if (this.dataLayer.product.length > 0) {
            event.product = this.dataLayer.product
        }
        window.appEventData.push(event);
    },
    onFormSubmissionFailed: function(err) {
        const event = {
            "event": "Form Submission Failed",
            "form": {
                ...this.dataLayer.form,
                ...err,
            }
        }
        if (this.dataLayer.product.length > 0) {
            event.product = this.dataLayer.product
        }
        window.appEventData.push(event);
    },
    init: function () {
        this.registerTypeChangeEvent();
        const {selectors} = this;
        const component = document.querySelector(selectors.self);
        const contactForm = component.querySelector(selectors.contactForm);
        const typeOfRequest = component.querySelector(selectors.typeOfRequest);
        const building = component.querySelector(selectors.building);
        const formError = component.querySelector(selectors.formErrorMessage);
        const postSubmissionContents = component.querySelector(selectors.postSubmissionContents);
        const confirmationName = component.querySelector(selectors.confirmationName);
        const commonElementSelector = `${selectors.firstName}, ${selectors.lastName}, ${selectors.emailAddress}, ${selectors.phoneNumber}, ${selectors.message}`;
        const moreElementSelector = `${selectors.firstName}, ${selectors.lastName}, ${selectors.emailAddress}, ${selectors.phoneNumber}, ${selectors.building}`;
        const commonFormElements = Array.prototype.slice.call(component.querySelectorAll(commonElementSelector));
        const moreFormElements = Array.prototype.slice.call(component.querySelectorAll(moreElementSelector));
        const choiceConfig = {
            searchEnabled: false, 
            itemSelectText: '', 
            shouldSort: false, 
            classNames: {
                selectedState: 'is-selected',
                highlightedState: 'is-highlight'
              }
        };


        let rentdotSubmissionFailed = false;

        function formatDate(date) {
            return `${date.getDate()}-${date.getMonth() + 1}-${date.getFullYear()}`
        }

        function formatURL() {
            const parts = digitalData.pageInfo.currentPagePath.replace("/jcr:content", "").split("/");
            return `${parts[parts.length - 1]}.contactus.html`;
        }
        const typeOfRequestChoices = new Choices(typeOfRequest, choiceConfig);

        const buildingsMap = {};
        building.querySelectorAll("option").forEach(option => {
            if (option.value) {
                buildingsMap[option.value] = {
                    ["property_name"]: option.value,
                    ["property_email"]: option.getAttribute('data-email'),
                    ["property_address"]: option.getAttribute('data-address')
                }
            }
        });
        const buildingChoices = new Choices(building, choiceConfig);
        //Form submission
        contactForm.onsubmit = (e) => {
            e.preventDefault();
            let validationResults;
            if (typeOfRequest.value === "new-home" || typeOfRequest.value === "current-resident") {
                validationResults = this.validateForm(moreFormElements);
            } else if (typeOfRequest.value === "general-inquiry") {
                validationResults = this.validateForm(commonFormElements);
            }
            if (!validationResults.isFormValid) {
                formError.classList.remove("hidden");
                for (const [key, value] of Object.entries(validationResults)) {
                    const el = document.getElementById(key);
                    if (!value && el) {
                        el.classList.add("invalid");
                        el.nextElementSibling.classList.remove("hidden");
                    }
                }
                rentdotSubmissionFailed = true;
                this.onFormSubmissionFailed({formField: Object.keys(validationResults).filter(k => k !== 'isFormValid').map(k => ({formFieldError: k+" Required"}))})
                return false;
            } else {
                rentdotSubmissionFailed = false;
                const contactFormData = new FormData(contactForm);
                let data = {};
                for (const pair of contactFormData.entries()) {
                    data[pair[0]] = pair[1]
                }
                const type = data['type_of_request'];
                if (type === 'current-resident') {
                    delete data['move_in_date'];
                }
                if (type === 'general-inquiry') {
                    delete data['move_in_date'];
                    delete data['building']
                }

                const moveInDate = component.querySelector(selectors.moveInDate);
                if (moveInDate && moveInDate.value) {
                    data['move_in_date'] = formatDate(new Date(moveInDate.value));
                }


                if (data['building'] && buildingsMap[data['building']]) {
                    data = {...data, ...buildingsMap[data['building']]}
                }

                const token = data[":cq_csrf_token"];
                for (const key of this.extraFormParams) {
                    delete data[key];
                }

                this.onFormSubmitted();
                fetch(formatURL(), {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        "CSRF-Token": token,
                    },
                    body: JSON.stringify(data)
                }).then(()=>{
                    confirmationName.innerText = data['first_name']
                    contactForm.classList.add("hidden");
                    postSubmissionContents.classList.remove("hidden");
                }).then(() => this.onFormSubmissionSucceeded()).catch(err => {
                    console.error(err)
                    this.onFormSubmissionFailed({formError: err.message || String(err)})
                });
            }
        };
        // Remove invalid state of required field if a user corrects the field error.
        // Remove form error message if a user corrects all of the field errors.
        moreFormElements.forEach(el => {
            el.addEventListener('blur', () => {
                if (rentdotSubmissionFailed && el.value && el.value.trim()) {
                    el.classList.remove("invalid");
                    el.nextElementSibling.classList.add("hidden");
                    let allElementsValid = true;
                    moreFormElements.forEach(el => {
                        if (el.classList.contains("invalid")) {
                            allElementsValid = false;

                        }
                    });
                    if (allElementsValid) {
                        formError.classList.add("hidden");
                    }
                }
            });
        });

       this.initDataLayer();
    }

}

export default RentdotContact;
