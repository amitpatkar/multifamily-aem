import { DetectSwipe } from "./_utils";

const NotableContentsCarousel = {
    selectors: {
        self: '[data-cmp-component="notablecontentscarousel"]',
        sliderEl: '.slider',
        slideEl: '.slide-contain',
        sliderPrev: 'button.carouselSection__btn--prev',
        sliderNext: 'button.carouselSection__btn--next',
        currentPage: '.carouselSection__status .current-page'
    },
    intiateSlider: function() {
        const {selectors} = this;
        const components = document.querySelectorAll(selectors.self);
        components.forEach(component => {
            const slider = component.querySelector(selectors.sliderEl);
            if (slider) {
                const initialSlides = Array.from(slider.querySelectorAll(selectors.slideEl));
                const prevButton = component.querySelector(selectors.sliderPrev);
                const nextButton = component.querySelector(selectors.sliderNext);
                const slidesToShow = 3;
                const initialSlideCount = initialSlides.length;
                const slideAction = (swipeDirection) => {
                    if(swipeDirection == 'left') {
                        nextButton.click();
                    } else if (swipeDirection == 'right') {
                        prevButton.click();
                    }

                }

                if (initialSlideCount == 1) {
                    nextButton.disabled = true;
                    nextButton.classList.add('disabled');
                    prevButton.disabled = true;
                    prevButton.classList.add('disabled');
                } else if (initialSlideCount == 2) {
                    slider.style.width = initialSlides.length * 100 + '%';
                    let currentIndex = 0;
                    this.updateActiveSlide(component, currentIndex, initialSlides, slider, initialSlideCount);
                    nextButton.addEventListener('click', () => {
                        if(currentIndex >= 1) {
                            return;
                        }
                        slider.style.transition = "transform 1s ease-in-out";
                        currentIndex++;
                        this.updateActiveSlide(component, currentIndex, initialSlides, slider, initialSlideCount);
                        nextButton.disabled = true;
                        nextButton.classList.add('disabled');
                        prevButton.disabled = false;
                        prevButton.classList.remove('disabled');
                    });
                    prevButton.addEventListener('click', () => {
                        if (currentIndex <= 0) {
                            return;
                        }
                        slider.style.transition = "transform 1s ease-in-out";
                        currentIndex--;
                        this.updateActiveSlide(component, currentIndex, initialSlides, slider, initialSlideCount);
                        prevButton.disabled = true;
                        prevButton.classList.add('disabled');
                        nextButton.disabled = false;
                        nextButton.classList.remove('disabled');
                    });
                } else if (initialSlideCount >= slidesToShow) {
                    //clone and append slides
                    for (let i = initialSlideCount; i > (initialSlideCount - slidesToShow); i--) {
                        const slideIndex = i - 1;
                        const slideClone = initialSlides[slideIndex].cloneNode(true);
                        slideClone.setAttribute("data-slide-index", slideIndex - initialSlideCount);
                        slideClone.classList.add('slide-cloned');
                        slideClone.setAttribute('aria-label', `slide ${i}`);
                        slider.prepend(slideClone);
                    }
                    for (let i = 0; i < slidesToShow; i++) {
                        const slideIndex = i;
                        const slideClone = initialSlides[slideIndex].cloneNode(true);
                        slideClone.setAttribute("data-slide-index", slideIndex + initialSlideCount);
                        slideClone.classList.add('slide-cloned');
                        slideClone.setAttribute('aria-label', `slide ${i+1}`);
                        slider.append(slideClone);
                    }
                    const slides = Array.from(document.querySelectorAll('.slider .slide-contain'));
                    slider.style.width = slides.length * 100 + '%';
                    let currentIndex = slidesToShow;
                    this.updateActiveSlide(component, currentIndex, slides, slider, initialSlideCount);
                    nextButton.addEventListener('click', () => {
                        if(currentIndex >= slides.length - slidesToShow) {
                            return;
                        }
                        slider.style.transition = "transform 1s ease-in-out";
                        currentIndex++;
                        this.updateActiveSlide(component, currentIndex, slides, slider, initialSlideCount);
                    });
                    prevButton.addEventListener('click', () => {
                        if (currentIndex <= slidesToShow - 1) {
                            return;
                        }
                        slider.style.transition = "transform 1s ease-in-out";
                        currentIndex--;
                        this.updateActiveSlide(component, currentIndex, slides, slider, initialSlideCount);
                    });
                    slider.addEventListener('transitionend', () => {
                        if(currentIndex == slides.length - slidesToShow){
                            slider.style.transition = "none";
                            currentIndex = slides.length - currentIndex;
                            this.updateActiveSlide(component, currentIndex, slides, slider, initialSlideCount);
                        }
                        if(currentIndex == slidesToShow - 1){
                            slider.style.transition = "none";
                            currentIndex = slides.length - slidesToShow - 1;
                            this.updateActiveSlide(component, currentIndex, slides, slider, initialSlideCount);
                        }      
                    });
                }
                DetectSwipe(slider, slideAction);
            }
        });
    },
    updateActiveSlide: function(component, currentIndex, slideArray, slider, initialSlideCount){
        const slideDistance = (1/slideArray.length) * 100 ;
        const currentPageEl = component.querySelector(this.selectors.currentPage);
        for (let i = 0; i < slideArray.length; i++) {
            if (i != currentIndex) {
                slideArray[i].setAttribute('aria-hidden', 'true');
                slideArray[i].classList.remove('slide-active');
                slideArray[i].querySelectorAll('a, input, button, select').forEach(el => {
                    el.setAttribute('tabindex', '-1');
                });
            } else {
                slideArray[i].removeAttribute('aria-hidden');
                slideArray[i].classList.add('slide-active');
                slideArray[i].querySelectorAll('a, input, button, select').forEach(el => {
                    el.removeAttribute('tabindex');
                });
            }
        }
        const dataIndex = parseInt(slideArray[currentIndex].getAttribute("data-slide-index"));
        currentPageEl.innerText = dataIndex < 0 ? (dataIndex + initialSlideCount + 1) : (dataIndex >= initialSlideCount ? dataIndex - initialSlideCount + 1: dataIndex+1);
        //console.log('current:', currentPageEl.innerText);
        slider.style.transform = 'translateX(' + (-slideDistance * currentIndex) + '%)';
    },
    init: function() {
        this.intiateSlider();

    }

};
export default NotableContentsCarousel;