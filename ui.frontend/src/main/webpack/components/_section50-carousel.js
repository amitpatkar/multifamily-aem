
import Glider from 'glider-js';

const Section50Carousel = {
    selectors: {
        self: '[data-cmp-component="section50carousel"]',
        gliderEl: '.glider',
        gliderPrev: 'button.carouselSection__btn--prev',
        gliderNext: 'button.carouselSection__btn--next',
        currentPage: '.carouselSection__status .current-page'
    },
    initiateCarousel: function(){
        const {selectors} = this;
        const components = document.querySelectorAll(selectors.self);
        components.forEach(component => {
            const gliderEl = component.querySelector(selectors.gliderEl);
            const currentPageEl = component.querySelector(selectors.currentPage)
            const prevArrow = component.querySelector(selectors.gliderPrev);
            const nextArrow = component.querySelector(selectors.gliderNext);
            new Glider(gliderEl, {
                slidesToShow: 1,
                slidesToScroll: 1,
                draggable: false,
                scrollLock: true,
                arrows: {
                    prev: prevArrow,
                    next: nextArrow
                },
                duration: 0.1
            });
            // gliderEl.addEventListener("glider-slide-visible", function(event){
            //     const glider = Glider(this);
            //     const index = glider.getCurrentSlide();
            //     currentPageEl.innerText = index + 1;
            // });
            gliderEl.addEventListener("scroll", function(event){
                const glider = Glider(this);
                currentPageEl.innerText = glider.getCurrentSlide() + 1;
            });
        });

    },
    init: function() {
        this.initiateCarousel();
    }
};

export default Section50Carousel;