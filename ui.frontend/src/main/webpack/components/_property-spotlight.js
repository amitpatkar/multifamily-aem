import Glider from 'glider-js';

const PropertySpotlight = {
    selectors: {
        self: '[data-cmp-component="propertyspotlight"]',
        gliderEl: '.glider',
        gliderPrev: 'button.carouselSection__btn--prev',
        gliderNext: 'button.carouselSection__btn--next',
        currentSlide: '.carouselSection__status .current-page'
    },
    initiateSlider: function(component){
        const gliderEl = component.querySelector(selectors.gliderEl);
        const prevArrow = component.querySelector(selectors.gliderPrev);
        const nextArrow = component.querySelector(selectors.gliderNext);
        const currentSlideIndex = component.querySelector(selectors.currentSlide);    
    },
    init: function() {
        const {selectors} = this;
        const propertySportlightEls = document.querySelectorAll(selectors.self);
        propertySportlightEls.forEach(component => {
            const gliderEl = component.querySelector(selectors.gliderEl);
            const prevArrow = component.querySelector(selectors.gliderPrev);
            const nextArrow = component.querySelector(selectors.gliderNext);
            const currentSlideIndex = component.querySelector(selectors.currentSlide);
            const mQueryMobile= window.matchMedia('(max-width: 1024px)');
            const mobileSettings = {
                        slidesToShow: 1,
                        slidesToScroll: 1,
                        scrollLock: true,
                        arrows: {
                            prev: prevArrow,
                            next: nextArrow
                        },
                        duration: 0.3
                    };
            function updateStatus(gliderInstance) {
                currentSlideIndex.innerHTML = gliderInstance.getCurrentSlide() + 1;
            }
            if (mQueryMobile.matches) {
                new Glider(gliderEl, mobileSettings);
                // gliderEl.addEventListener("glider-slide-visible", function(){
                //     const glider = Glider(this);
                //     updateStatus(glider);
                // });
                gliderEl.addEventListener("scroll", function(){
                    const glider = Glider(this);
                    updateStatus(glider);
                });
            }  
            const mobileHandler = (e) => {
                if (e.matches) {
                    if(component.querySelector(".glider-track")) {
                        Glider(gliderEl).refresh(true);
                        currentSlideIndex.innerHTML = 1;
                    } else {
                        new Glider(gliderEl, mobileSettings);
                        // gliderEl.addEventListener("scroll", function(){
                        //     const glider = Glider(this);
                        //     updateStatus(glider);
                        // });
                        gliderEl.addEventListener("glider-slide-visible", function(){
                            const glider = Glider(this);
                            updateStatus(glider);
                        });
                    }                       
                } 
            }
            if (mQueryMobile.addEventListener) {
                mQueryMobile.addEventListener('change', mobileHandler);
            } else {
                mQueryMobile.addListener(mobileHandler);
            }

            // mQueryMobile.addEventListener('change', (e) => {
            //     if (e.matches) {
            //         if(component.querySelector(".glider-track")) {
            //             Glider(gliderEl).refresh(true);
            //             currentSlideIndex.innerHTML = 1;
            //         } else {
            //             new Glider(gliderEl, mobileSettings);
            //             // gliderEl.addEventListener("scroll", function(){
            //             //     const glider = Glider(this);
            //             //     updateStatus(glider);
            //             // });
            //             gliderEl.addEventListener("glider-slide-visible", function(){
            //                 const glider = Glider(this);
            //                 updateStatus(glider);
            //             });
            //         }                       
            //     } 

            // });


        });
        
    }
};

export default PropertySpotlight;