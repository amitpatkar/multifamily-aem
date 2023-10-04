import { IsDesktop } from "./_utils";
const Accordions = {
    selectors: {
        self: '[data-cmp-component="accordions"]',
        collapseAll: '.collapse-all',
        accordionItem: '.cmp-accordions__item',
        accordionHeader: '.cmp-accordions__header',
        accordionButton: '.cmp-accordions__button',
        accordionButtonExpanded: '.cmp-accordions__button--expanded',
        accordionPanel: '.cmp-accordions__panel'
    },
    cssClasses: {
        button: {
            expanded: "cmp-accordions__button--expanded"
        },
        panel: {
            hidden: "cmp-accordions__panel--hidden",
            expanded: "cmp-accordions__panel--expanded"
        }
    },
    expandItem: function(accordionButtons, accordionPanels, itemIndex){
        const {button, panel} = this.cssClasses;
        for(let i = 0; i < accordionButtons.length; i++) {
            if (i != itemIndex) {
                accordionButtons[i].classList.remove(button.expanded);
                accordionPanels[i].classList.remove(panel.expanded);
                accordionButtons[i].setAttribute("aria-expanded", "false");
                if (!accordionPanels[i].classList.contains(panel.hidden)) {
                    accordionPanels[i].classList.add(panel.hidden);
                }
            } else {
                accordionButtons[i].classList.add(button.expanded);
                accordionButtons[i].setAttribute("aria-expanded", "true");
                accordionPanels[i].classList.remove(panel.hidden);
                accordionPanels[i].classList.add(panel.expanded);
                if(!IsDesktop()) {
                    accordionPanels[i].parentElement.scrollIntoView({behavior: "smooth"});
                }
            }
        }
    },
    registerButtonEvents: function(){
        const {selectors} = this;
        const components = document.querySelectorAll(selectors.self);
        components.forEach(accordionGroup => {
            const collapseAll = accordionGroup.querySelector(selectors.collapseAll);
            console.log(collapseAll);
            const accordionButtons = Array.prototype.slice.call(accordionGroup.querySelectorAll(selectors.accordionButton));
            const accordionPanels = Array.prototype.slice.call(accordionGroup.querySelectorAll(selectors.accordionPanel));
            if (accordionButtons.length > 0 && accordionPanels.length > 0  &&  accordionButtons.length == accordionPanels.length) {
                if (!collapseAll) {
                    this.expandItem(accordionButtons, accordionPanels, 0);
                }
                for (let i = 0; i <accordionButtons.length; i++) {
                    accordionButtons[i].addEventListener('click', ()=>{
                        const {button, panel} = this.cssClasses;
                        if(accordionButtons[i].classList.contains(button.expanded)){
                            accordionButtons[i].classList.remove(button.expanded);
                            accordionButtons[i].setAttribute("aria-expanded", "false");
                            accordionPanels[i].classList.remove(panel.expanded);
                            accordionPanels[i].classList.add(panel.hidden);
                        } else {
                            this.expandItem(accordionButtons, accordionPanels, i);
                        }
                    });
                }
            }
            
        });
    },
    init: function() {
        this.registerButtonEvents();
    }
};

export default Accordions;