import A11yDialog from 'a11y-dialog';
import Glider from 'glider-js';

const Gallery = {
    selectors: {
        self: '[data-cmp-component="gallery"]',
        desktopGlider: '.cmp-gallery__inner--desktop .glider',
        desktopGliderPrev: '.cmp-gallery__inner--desktop button.glider-prev-btn',
        desktopGliderNext: '.cmp-gallery__inner--desktop button.glider-next-btn',
        mobileGlider: '.cmp-gallery__inner--mobile .glider',
        dialogEl: '.dialog-container',
        slides: '.slide-contents'
    },
    initiateGallery: function(){
        const {selectors} = this;
        const gallerySections = document.querySelectorAll(selectors.self);
        gallerySections.forEach(gallery => {
            const desktopGlider = gallery.querySelector(selectors.desktopGlider);
            const dialogEl = gallery.querySelector(selectors.dialogEl);
            const dialog = new A11yDialog(dialogEl);
            const mobileGlider = gallery.querySelector(selectors.mobileGlider);
            const captionTextEl = gallery.querySelector(".cmp-gallery__caption--text");
            const captionIndexEl = gallery.querySelector(".cmp-gallery__caption--index .image-current");
            const mobileSlides = mobileGlider.querySelectorAll(selectors.slides);
            mobileSlides.forEach(slide => {
                slide.addEventListener('touchstart', ()=>{
                    slide.classList.add("touched");
                    console.log("touched!")
                });
                slide.addEventListener('touchend', ()=>{
                    slide.classList.remove("touched");
                    console.log("remove touched!")
                });
            });
            new Glider(desktopGlider, {
                slidesToShow: 1,
                slidesToScroll: 1,
                scrollLock: true,
                arrows: {
                    prev: selectors.desktopGliderPrev,
                    next: selectors.desktopGliderNext
                },
                duration: 0.2
            });
            desktopGlider.addEventListener("glider-slide-visible", function(event){
                const glider = Glider(this);
                const currentCaption = glider.slides[event.detail.slide].querySelector("img").getAttribute("data-image-caption") || "";
                captionIndexEl.textContent = event.detail.slide + 1;
                captionTextEl.textContent = currentCaption;
            });
            new Glider(mobileGlider, {
                slidesToShow: 1,
                slidesToScroll: 1,
                scrollLock: true,
                duration: 0.2
            });
            dialog.on('show', function(element, event) {
                Glider(mobileGlider).refresh(true);  
            });
        });
    },
    init: function() {
        this.initiateGallery();
    }
};

export default Gallery;