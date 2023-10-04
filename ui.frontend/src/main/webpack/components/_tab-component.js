const TabComponent = {
    selectors: {
       tabComponent: '[data-cmp-component="tabcomponent"]',
       tabButtons: 'button[role="tab"]',
       tabPanels: '.tab-panel[role="tabpanel"]'
    },
    init: function(){
        const {selectors} = this;
        const tabSections = document.querySelectorAll(selectors.tabComponent);
        tabSections.forEach(tabSection => {
            const tabButtons = Array.prototype.slice.call(tabSection.querySelectorAll(selectors.tabButtons));
            const tabPanels = Array.prototype.slice.call(tabSection.querySelectorAll(selectors.tabPanels));
            if (tabButtons && tabButtons.length > 0){
                tabButtons.forEach(tabButton => {
                    tabButton.addEventListener('click', (event) => {
                        console.log('tab clicked!');
                        const clickedIndex = event.target.getAttribute("data-tab-index");
                        for (let i = 0; i <tabButtons.length; i++) {
                            tabButtons[i].setAttribute("aria-selected", "false");
                            tabButtons[i].setAttribute("tabindex", "-1");
                            tabPanels[i].classList.remove("current");
                            if (!tabPanels[i].classList.contains("hidden")){
                                tabPanels[i].classList.add("hidden");
                            }
                        }
                        tabButtons[clickedIndex].setAttribute("tabindex", "0");
                        tabButtons[clickedIndex].setAttribute("aria-selected", "true");
                        tabPanels[clickedIndex].classList.remove("hidden");
                        tabPanels[clickedIndex].classList.add("current");
                        tabPanels[clickedIndex].scrollIntoView();
                    });
                });
            }
        });
    }

};
export default TabComponent;