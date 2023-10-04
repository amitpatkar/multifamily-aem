import A11yDialog from 'a11y-dialog';
import Glider from 'glider-js';
import {LoadImages} from './_utils';
import LazyLoad from "vanilla-lazyload";
const Section33 = {
    selectors: {
        section33: '[data-cmp-component="section33"]',
        section33WithGallery: '.gallery-on[data-cmp-component="section33"]',
        priorityCards: '.cmp-section33__card.priority',
        emptyCards: '.cmp-section33__card--empty33',
        imageCards: '.cmp-section33__card--image33, .cmp-section33__card--image66',
        column: '.cmp-section33__col',
        slides: '.slide-contents:not(.schedule-tour)'
    },
    galleryInfo: {},
    updateColumnCssClass: function() {
        const {selectors} = this;
        const sections = document.querySelectorAll(selectors.section33);
        sections.forEach(section => {
            const prioritycards = section.querySelectorAll(selectors.priorityCards);
            const emptycards = section.querySelectorAll(selectors.emptyCards);
            prioritycards.forEach(card => {
                const col = card.closest(selectors.column);
                if (col) {
                    col.classList.add('priority');
                }
            });
            emptycards.forEach(card => {
                const col = card.closest(selectors.column);
                if (col) {
                    col.classList.add('empty');
                }
            });
        });
    },
    createGalleryModals: function(sectionsWithGallery) {
        for (let i = 0; i < sectionsWithGallery.length; i++) {
            const dialogId = sectionsWithGallery[i].getAttribute("data-dialog-id");
           // console.log("create gallery view from masonry", this.galleryInfo);
            const galleryImageArray = this.galleryInfo[dialogId];
            let slidesHtml = "";
            if (galleryImageArray && galleryImageArray.length > 0) {
                for (let i = 0; i < galleryImageArray.length; i++){
                    slidesHtml += `
                    <div class="slide-contain">
                        <div class="slide-contents">
                            <div class="slide-imageWrap">
                                <div class="slide-image">
                                    <img data-src="${galleryImageArray[i].src || galleryImageArray[i].dataSrc}" alt="${galleryImageArray[i].alt}">
                                </div>
                            </div>
                            <h3 class="slide-caption--title">${i + 1}<span class="hide-for-mobile"> of </span><span class="hide-for-desktop">/</span>${galleryImageArray.length} Images
                            </h3>
                            <p class="slide-caption--body">${galleryImageArray[i].caption}</p>
                        </div>
                    </div>`
                }
            }
            const dialogEl = document.getElementById(dialogId);
            const dialog = new A11yDialog(dialogEl);
            const gliderEl = dialogEl.querySelector('.glider');
            const scheduleButton = dialogEl.querySelector("button.dialog-schedule-tour");
            slidesHtml += gliderEl.innerHTML;
            gliderEl.innerHTML = slidesHtml;
            new Glider(gliderEl, {
                slidesToShow: 1,
                slidesToScroll: 1,
                scrollLock: true,
                arrows: {
                    prev: `#${dialogId} .glider-prev`,
                    next: `#${dialogId} .glider-next`
                },
                duration: 0.1
            });
            if (scheduleButton) {
                dialogEl.classList.add("schedule-tour-on");
            }

            gliderEl.addEventListener("glider-slide-visible", function(event){
                const glider = Glider(this);
                if (scheduleButton) {
                    if (glider.slides[event.detail.slide].querySelector(".schedule-tour")) {
                        dialogEl.classList.add("schedule-tour-slide-visible");
                    } else {
                        dialogEl.classList.remove("schedule-tour-slide-visible");
                    }
                }
            });
            
            const imageSlides = gliderEl.querySelectorAll(this.selectors.slides);
            imageSlides.forEach(slide => {
                slide.addEventListener('touchstart', ()=>{
                    slide.classList.add("touched");
                    console.log("touched!");
                });
                slide.addEventListener('touchend', ()=>{
                    slide.classList.remove("touched");
                    console.log("remove touched!");
                });
            });

            dialog.on('show', function(element, event) {
                const glider = Glider(dialogEl.querySelector('.glider'));
                const index = event.currentTarget.getAttribute("data-image-index");
                const gliderSlides = Array.from(glider.slides);
                gliderSlides.forEach(slide => {
                    LoadImages.call(slide);
                });
                glider.refresh(true);
                glider.scrollItem(index, true);
                if (dialogEl.querySelector('.glider-prev:not([aria-disabled="true"])')) {
                    dialogEl.querySelector('.glider-prev:not([aria-disabled="true"])').focus();
                } else {
                    dialogEl.querySelector(".glider-next").focus();
                }


            });
        }
                    
    },
    registerImageButtonEvents: function(sectionsWithGallery){
        for (let i = 0; i < sectionsWithGallery.length; i ++) {
            const dialogId = sectionsWithGallery[i].getAttribute("data-dialog-id");
            const imageCards = Array.prototype.slice.call(sectionsWithGallery[i].querySelectorAll(this.selectors.imageCards));
            const imageArray = [];
            let counter = 0;
            for (let k = 0; k < imageCards.length; k++) {
                const card = imageCards[k];
                const imageEl = card.querySelector("img");
                if (imageEl) {
                    const imageObj = {};
                    imageObj.dataSrc = imageEl.getAttribute("data-src") || "";
                    imageObj.src = imageEl.getAttribute("src") || "";
                    imageObj.caption = imageEl.getAttribute("data-image-caption") || "";
                    imageObj.alt = imageEl.getAttribute("alt") || "";
                    // imageObj.aspectRatio = (imageEl.naturalWidth / imageEl.naturalHeight) || "";
                    imageArray.push(imageObj);
                    const indicator = document.createElement('div');
                    indicator.className = "cmp-section33__gallery-indicator";
                    indicator.innerHTML = `<div class="cmp-section33__gallery-indicator--inner"></div>`;
                    card.querySelector(".cmp-section33__cardContent").appendChild(indicator);
                    card.setAttribute("role", "button");
                    card.setAttribute("tabindex", "0");
                    card.setAttribute("aria-label", "Open the gallery view");
                    card.setAttribute("data-a11y-dialog-show", dialogId);
                    card.setAttribute("data-image-index", counter);
                    card.addEventListener('keydown', (event)=>{
                        if (event.keyCode === 32) {
                            event.preventDefault();
                        } else if (event.keyCode === 13) {
                            event.preventDefault();
                            card.click();
                        }
                    });
                    card.addEventListener('keyup', (event)=>{
                        if (event.keyCode === 32) {
                            event.preventDefault();
                            card.click();
                        } 
                    });
                    counter++;
                }

            }
            this.galleryInfo[dialogId]=imageArray;

        }

    },
    init: function() {
        this.updateColumnCssClass();
        const {selectors} = this;
        const sectionsWithGallery = Array.prototype.slice.call(document.querySelectorAll(selectors.section33WithGallery));
        if (sectionsWithGallery && sectionsWithGallery.length > 0) {
            this.registerImageButtonEvents(sectionsWithGallery);
            this.createGalleryModals(sectionsWithGallery);
        }
        if (document.querySelector('img.lazy')) {
            var lazyLoadInstance = new LazyLoad({
                threshold: 0
            });
        }

    }
};
export default Section33;
