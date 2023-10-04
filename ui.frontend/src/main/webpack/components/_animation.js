const Animation = {
    selectors: {
        heroElements: `.animation-on .cmp-hero__title, .animation-on .cmp-hero__banner--default, 
        .animation-on .cmp-hero__logo, .cmp-hero--title .animation-on .cmp-hero__media, 
        .cmp-hero--logo .animation-on .cmp-hero__media`,
        section100Elements: `.animation-on .cmp-section100__lead, .animation-on 
        .cmp-section100__info, .animation-on .cmp-section100__copy, .animation-on .cmp-section100__contacts`,
        contactFormElements: `.animation-on .cmp-get-in-touch__contents-inner, .animation-on .cmp-rentdotContact__contents-inner`,
        section50Elements: '.animation-on .cmp-section50__box'
    },
    init: function () {
        const {heroElements, section100Elements, contactFormElements, section50Elements} = this.selectors;
        const options = {

        };
        const callback = (entries, observer) => {
            entries.forEach((entry) => {
              if (entry.isIntersecting) {
                entry.target.classList.add("animated")
                // stop observing this element
                observer.unobserve(entry.target)
              }
            })
        };
        const animationObserver = new IntersectionObserver(callback, options);
        const animationElements = document.querySelectorAll(`${heroElements}, ${section100Elements}, ${contactFormElements}, ${section50Elements}`);
        console.log(animationElements);
        animationElements.forEach(element => {
            animationObserver.observe(element);
        });

    }
};
export default Animation;