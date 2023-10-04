const FormStyles = {
    selectors: {
        homeType: '.cmp-options.home-type',
        moveInDate: '.cmp-options.move-in-date',
        checkBox: 'input[type="checkbox"]',
        radioItem: '.radio-item',
        radioBtn: 'input[type="radio"]'
    },
    init: function () {
        const {selectors} = this;
        console.log("form-styles.js");
        const homeTypes = document.querySelectorAll(selectors.homeType);
        const moveInDates = document.querySelectorAll(selectors.moveInDate);
        homeTypes.forEach(section => {
            const checkBoxes = section.querySelectorAll(selectors.checkBox);
            checkBoxes.forEach(el => {
                el.addEventListener('change', () => {
                    const closestDiv = el.closest('.checkbox-item');
                    if (el.checked) {
                        closestDiv.classList.add('checked');
                        if(closestDiv.previousElementSibling && closestDiv.previousElementSibling.classList.contains("checked")) {
                            closestDiv.classList.add('border-left-removed');
                            closestDiv.previousElementSibling.classList.add('border-right-removed');
                        }
                        if(closestDiv.nextElementSibling && closestDiv.nextElementSibling.classList.contains("checked")) {
                            closestDiv.classList.add('border-right-removed');
                            closestDiv.nextElementSibling.classList.add('border-left-removed');
                        }
                    } else {
                        closestDiv.classList.remove('checked', 'border-left-removed', 'border-right-removed');
                        if (closestDiv.previousElementSibling) {
                            closestDiv.previousElementSibling.classList.remove('border-right-removed');
                        }
                        if (closestDiv.nextElementSibling) {
                            closestDiv.nextElementSibling.classList.remove('border-left-removed');
                        }
                    }

                });
            });
        });
        moveInDates.forEach(section=> {
            const radioItems =  Array.prototype.slice.call(section.querySelectorAll(selectors.radioItem));
            for(let i = 0; i <radioItems.length; i++) {
                const radioItem = radioItems[i];
                const radioBtn = radioItem.querySelector(selectors.radioBtn);
                radioBtn.addEventListener('change', () => {
                    if (radioBtn.checked) {
                        radioItem.classList.add('checked');
                        for(let k = 0; k < radioItems.length; k++ ) {
                            if (k != i) {
                                radioItems[k].classList.remove('checked');
                            }
                        }
                    } else {
                        radioItem.classList.remove('checked');
                    }
                });
            }

        });
    }
};
export default FormStyles;