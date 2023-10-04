const GetInTouch = {
    selectors: {
        self: '[data-cmp-component="getintouch"]',
        contactForm: 'form',
        firstName: "#first_name",
        lastName: "#last_name",
        emailAddress: '#email_address',
        phoneNumber: '#phone_number',
        moveInDate: 'duet-date-picker',
        formErrorMessage: '#get_in_touch_error',
        postSubmissionContents: '.cmp-get-in-touch__postsubmission',
        confirmationName: '.cmp-get-in-touch__confirmation--name'
    },
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
        }] : []
    },
    validateForm: function(firstName, lastName, emailAddress, phoneNumber, moveInDate) {
        let isFirstNameEmpty = true;
        let isLastNameEmpty = true;
        let isEmailEmpty = true;
        let isPhoneNumberEmpty = true;
        let isMoveInDateEmpty = true;
        let isFormInvalid = true;
        if (firstName.value && firstName.value.trim()) {
            isFirstNameEmpty = false;
        }
        if (lastName.value && lastName.value.trim()) {
            isLastNameEmpty = false;
        }
        if (emailAddress.value && emailAddress.value.trim()) {
            isEmailEmpty = false;
        }
        if (phoneNumber.value && phoneNumber.value.trim()) {
            isPhoneNumberEmpty = false;
        }
        console.log(moveInDate, moveInDate.value)
        if (moveInDate.value && moveInDate.value.trim()) {
            isMoveInDateEmpty = false;
        }
        isFormInvalid = isFirstNameEmpty || isFirstNameEmpty || isEmailEmpty || isPhoneNumberEmpty || isMoveInDateEmpty;
        return {
            isFirstNameEmpty,
            isLastNameEmpty,
            isEmailEmpty,
            isPhoneNumberEmpty,
            isMoveInDateEmpty,
            isFormInvalid
        };
    },
    isInNodeList: function(node, nodeList) {
        const nodeArray = Array.prototype.slice.call(nodeList);
        return nodeArray.indexOf(node) > -1;
    },
    initDataLayer: function () {
        const form = document.querySelector(this.selectors.self + ' ' + this.selectors.contactForm);
       // console.log('form: ', form);
        form.addEventListener("change", () => {
            if (!this.dataLayer.formStarted) {
                this.dataLayer.formStarted = true;
                window.appEventData.push({
                    "event": "Form Started",
                    "form": this.dataLayer.form,
                    "product": this.dataLayer.product
                });
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
        window.appEventData.push({
            "event": "Form Submission Succeeded",
            "form": this.dataLayer.form,
            "product": this.dataLayer.product
        });
    },
    onFormSubmissionFailed: function(err) {
        window.appEventData.push({
            "event": "Form Submission Failed",
            "form": {
                ...this.dataLayer.form,
                ...err,
            },
            "product": this.dataLayer.product
        });
    },
    init: function() {
        console.log('Get In Touch init!');
        const {selectors} = this;
        const getInTouchSection = document.querySelector(selectors.self);
        const contactForm = getInTouchSection.querySelector(selectors.contactForm);
        const firstName = document.querySelector(selectors.firstName);
        const lastName = document.querySelector(selectors.lastName);
        const emailAddress = document.querySelector(selectors.emailAddress);
        const phoneNumber = document.querySelector(selectors.phoneNumber);
        const formError = document.querySelector(selectors.formErrorMessage);
        const postSubmissionContents = document.querySelector(selectors.postSubmissionContents);
        const confirmationName = document.querySelector(selectors.confirmationName);
        let submissionFailed = false;

        function formatDate(date) {
            return `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;
        }

        function formatURL() {
            const parts = digitalData.pageInfo.currentPagePath.replace("/jcr:content", "").split("/");
            return `${parts[parts.length - 1]}.contactus.html`;
        }

        //Form submission
        contactForm.onsubmit = (e) => {
            e.preventDefault();
            const moveInDate = contactForm.querySelector(selectors.moveInDate);
            const result = this.validateForm(firstName, lastName, emailAddress, phoneNumber, moveInDate);
            if (result.isFormInvalid) {
                const formFieldErrors = [];
                if (result.isFirstNameEmpty) {
                    firstName.classList.add("invalid");
                    firstName.nextElementSibling.classList.remove("hidden");
                    formFieldErrors.push({formFieldError: "First Name required"})
                }
                if (result.isLastNameEmpty) {
                    lastName.classList.add("invalid");
                    lastName.nextElementSibling.classList.remove("hidden");
                    formFieldErrors.push({formFieldError: "Last Name required"})
                }
                if (result.isEmailEmpty) {
                    emailAddress.classList.add("invalid");
                    emailAddress.nextElementSibling.classList.remove("hidden");
                    formFieldErrors.push({formFieldError: "Email required"})
                }
                if (result.isPhoneNumberEmpty) {
                    phoneNumber.classList.add("invalid");
                    phoneNumber.nextElementSibling.classList.remove("hidden");
                    formFieldErrors.push({formFieldError: "Phone Number required"})
                }
                if (result.isMoveInDateEmpty) {
                    moveInDate.classList.add("invalid");
                    moveInDate.nextElementSibling.classList.remove("hidden");
                    formFieldErrors.push({formFieldError: "Move in date required"})
                }
                formError.classList.remove("hidden");
                submissionFailed = true;
                this.onFormSubmissionFailed({formField: formFieldErrors})
                return false;
            } else {
                submissionFailed = false;
                const contactFormData = new FormData(contactForm);
                const data = {};
                for (var pair of contactFormData.entries()) {
                    data[pair[0]] = pair[1];
                }
                if (moveInDate && moveInDate.value) {
                    data['moveindate'] = formatDate(new Date(moveInDate.value));
                }
                const homeTypeKeys = ['studio', 'onebedroom', 'twobedroom', 'penthouse', 'townhome'];
                data['bedrooms'] = homeTypeKeys.filter(type => data[type]).map(type => data[type]).join(', ');
                homeTypeKeys.forEach(type => delete data[type]);

                //console.log(data);
                confirmationName.innerText = data['first_name']
                contactForm.classList.add("hidden");
                postSubmissionContents.classList.remove("hidden");
                this.onFormSubmitted();
                fetch('/libs/granite/csrf/token.json')
                    .then(resp => resp.json())
                    .then(data => data.token)
                    .then(token => fetch(formatURL(), {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                "CSRF-Token": token,
                            },
                            body: JSON.stringify(data)
                        })
                    )
                    .then(() => this.onFormSubmissionSucceeded())
                    .catch(err => {
                        console.error(err)
                        this.onFormSubmissionFailed({formError: err.message || String(err)})
                    })
                //contactForm.submit();
            }
        };
        //Remove invalid state of name/email if a user corrects the field error.
        //Remove form error message if a user corrects all of the field errors.
        setImmediate(() => {
            const moveInDate = contactForm.querySelector(selectors.moveInDate);
            const elements = [
                {elem: firstName, events: ["blur"]},
                {elem: lastName, events: ["blur"]},
                {elem: emailAddress, events: ["blur"]},
                {elem: phoneNumber, events: ["blur"]},
                {elem: moveInDate, events: ["duetBlur", "duetClose"]}
            ];
            elements.forEach(e => {
                function cb() {
                    if (submissionFailed && e.elem.value && e.elem.value.trim()) {
                        e.elem.classList.remove("invalid");
                        e.elem.nextElementSibling.classList.add("hidden");
                        if (!elements.filter(el => el !== e).find(el => el.elem.classList.contains("invalid"))) {
                            formError.classList.add("hidden");
                        }
                    }
                }
                e.events.forEach(ev => {
                    e.elem.addEventListener(ev, cb)
                })
            })
        })

       this.initDataLayer();
    }

}

export default GetInTouch