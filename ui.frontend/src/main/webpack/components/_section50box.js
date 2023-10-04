const Section50Box = {
    selectors: {
        self: '[data-cmp-component="section50box"]',
        titleEl: '.large-title'
    },
    init: function() {
        const {selectors} = this;
        const boxEls = document.querySelectorAll(selectors.self);
        boxEls.forEach(box => {
            const titleEl = box.querySelector(selectors.titleEl);
            if(titleEl.innerText && titleEl.innerText.indexOf('-') != -1) {
                const titleHtml = titleEl.innerText.replaceAll('-', '&#8288;-&#8288;');
                titleEl.innerHTML=titleHtml;
            }
        });
    }
};

export default Section50Box;