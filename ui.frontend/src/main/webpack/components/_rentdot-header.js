import { ScrollToTop, IsDesktop } from "./_utils";
const RentDotHeader = {
    selectors: {
        header: '[data-cmp-component="rentdotheader"]',
        combinedHeader: '.cmp-rentdotHeader--combined',
        globalMenuToggle: '#globalmenu-toggle',
        specificMenuToggle: '#specificmenu-toggle',
        specificMenuLink: 'a.cmp-rentdotHeader__specific-navLink',
        specificMenuActive: '.cmp-rentdotHeader__specific-currentPage'
    },
    registerScrollEvent: function(){
        const headerEl = document.querySelector(this.selectors.header);
        //const scrollOffSetMin =  20;
        const scrollOffSetMax =  200;
        window.addEventListener("scroll", () => {
            if (window.scrollY > scrollOffSetMax) {
                headerEl.classList.add("scrolled");
            } else {
                headerEl.classList.remove("scrolled");
            }
            // if (window.scrollY > scrollOffSetMin) {
            //     headerEl.classList.add("scrolled");
            //     if (window.scrollY > scrollOffSetMax) {
            //         headerEl.classList.add("scrolled-large");
            //     } else {
            //         headerEl.classList.remove("scrolled-large");
            //     }
            // } else {
            //     headerEl.classList.remove("scrolled", "scrolled-large");
            // }
        }); 
    },
    registerMenuToggleEvent: function() {
        const htmlElements= document.querySelectorAll('body, html');
        const {selectors} = this;
        const globalMenuToggleBtn = document.querySelector(selectors.globalMenuToggle);
        const specificMenuToggleBtn = document.querySelector(selectors.specificMenuToggle);
        const headerEl= document.querySelector(selectors.header);   
        if (globalMenuToggleBtn) {
            globalMenuToggleBtn.addEventListener('click', () => {
                //console.log('global menu toggled!');
                headerEl.classList.remove("specific-nav-expanded");
                headerEl.classList.toggle("global-nav-expanded");
                if (headerEl.classList.contains("global-nav-expanded")) {
                    globalMenuToggleBtn.setAttribute('aria-expanded', 'true');
                    this.changeScrollable(htmlElements, 'add');
                } else {
                    globalMenuToggleBtn.setAttribute('aria-expanded', 'false');
                    this.changeScrollable(htmlElements, 'remove');
                }
            });
        }
        if (specificMenuToggleBtn) {
            specificMenuToggleBtn.addEventListener('click', ()=>{
                //console.log('specific menu toggled!');
                headerEl.classList.remove("global-nav-expanded");
                headerEl.classList.toggle("specific-nav-expanded");
                if (headerEl.classList.contains("specific-nav-expanded")) {
                    specificMenuToggleBtn.setAttribute('aria-expanded', 'true');
                    this.changeScrollable(htmlElements, 'add');
                } else {
                    specificMenuToggleBtn.setAttribute('aria-expanded', 'false');
                    this.changeScrollable(htmlElements, 'remove');
                }
            });
        }
    },
    registerspecificNavEvent: function() {
        const {selectors} = this;
        const headerEl = document.querySelector(selectors.header)
        const specificMenuToggleBtn = headerEl.querySelector(selectors.specificMenuToggle);
        const specificMenuLinks = Array.prototype.slice.call(headerEl.querySelectorAll(selectors.specificMenuLink));
        const specificMenuActive= headerEl.querySelector(selectors.specificMenuActive);
        if (specificMenuToggleBtn) {
            // const urlString = window.location.href.substring(window.location.origin.length);
            // const urlHash = window.location.hash;
            const options = {
                root: null,
                rootMargin: '-10% 0px -30% 0px',
                threshold: 0.1
            };
            const observer = new IntersectionObserver((entries)=>{
                entries.forEach(entry => {
                    const matchingLink = specificMenuLinks.find(link => link.getAttribute('href') 
                        && link.getAttribute('href').substring(1) == entry.target.id);
                    if (matchingLink) {
                        if (!entry.isIntersecting) {
                            matchingLink.classList.remove("active");
                            return;
                        }
                        specificMenuLinks.forEach(link=>{
                            if (link != matchingLink) {
                                link.classList.remove("active");
                            }
                        });
                        if (!matchingLink.classList.contains("active")) {
                            matchingLink.classList.add("active");
                        }
                        if (specificMenuActive && matchingLink.textContent) {
                            specificMenuActive.textContent = matchingLink.textContent;
                        }

                    }
                });
            }, options);
            specificMenuLinks.forEach(link => {
                const hrefString = link.getAttribute('href');
                if (hrefString && hrefString == window.location.pathname) {
                    link.classList.add("active");
                    specificMenuActive.textContent = innerText;
                }
                if (hrefString && hrefString != "#" && hrefString.startsWith('#')) {
                    const scrollToElement = document.querySelector(hrefString);
                    link.addEventListener('click', (event)=>{
                        event.preventDefault();
                        event.stopPropagation();
                        if (scrollToElement) {
                            if (!link.classList.contains("active")) {
                                link.classList.add("active");
                            }
                            if (specificMenuActive && link.textContent) {
                                specificMenuActive.textContent = link.textContent;
                            }
                            
                            if (headerEl.classList.contains('specific-nav-expanded')) {
                                specificMenuToggleBtn.click();
                            }
                            //scrollToElement.scrollIntoView();
                            const offSet = IsDesktop() ? 150 : 100;
                            console.log('scroll offSet', offSet);
                            ScrollToTop(scrollToElement, offSet);
                        } else {
                            return;
                        }
                    });
                    if(scrollToElement) {
                        observer.observe(scrollToElement);
                    }
                } 
            });
        }
    },
    addGolbalNavActiveClass: function(){
        const currentPath = location.pathname;
        if (currentPath) {
            const activeLinks = document.querySelectorAll('#rentdot-global-navigation a.cmp-rentdotHeader__global-navLink');
            activeLinks.forEach(link => {
                const linkHref = link.getAttribute('href');
                if(linkHref && linkHref != '/' && linkHref == currentPath ) {
                    link.classList.add("active");
                } 
            });
        }
    },
    changeScrollable: function(elements, option) {
        switch(option) {
            case 'add':
                elements.forEach(el => {
                    el.classList.add('no-scroll');
                });
                break;
            case 'remove':
                elements.forEach(el => {
                    el.classList.remove('no-scroll');
                });
                break;
        }
    },
    init: function() {
        this.registerScrollEvent();
        this.registerMenuToggleEvent();
        this.registerspecificNavEvent();
        this.addGolbalNavActiveClass();
    },

};
export default RentDotHeader;