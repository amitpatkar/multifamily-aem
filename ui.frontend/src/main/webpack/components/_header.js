const Header = {
    selectors: {
        self: '[data-cmp-component="header"]',
        navBar: '.cmp-header__navbar',
        logoContainer: '.cmp-header__logo',
        desktopMenu: '.cmp-header__links',
        desktopMenuLeft: '.cmp-header__links--left',
        desktopMenuRight: '.cmp-header__links--right',
        mobileMenu: '#cmp-header-mobileMenu',
        tabletMenu: '#cmp-header-tabletMenu',
        mobileMenuTrigger: '.cmp-header__mobileTrigger',
        tabletMenuTrigger: '.cmp-header__tabletTrigger',
        mobileMenuLeft: '.cmp-header__mobileMenu--left',
        mobileMenuRight: '.cmp-header__mobileMenu--right',
        indexPageWithHeroLogo: '.index-page.has-hero-logo'
    },
    setHeaderBreakpoint: function() {
        const {selectors} = this;
        const navBar = document.querySelector(selectors.navBar);
        const logoContainer = document.querySelector(selectors.logoContainer);
        const desktopMenuLeft = document.querySelector(selectors.desktopMenuLeft);
        const desktopMenuRight = document.querySelector(selectors.desktopMenuRight);
        const mobileMenu = document.querySelector(selectors.mobileMenu);
        const mobileMenuTrigger = document.querySelector(selectors.mobileMenuTrigger);
        const tabletMenu = document.querySelector(selectors.tabletMenu);
        const tabletMenuTrigger = document.querySelector(selectors.tabletMenuTrigger);
        let desktopBreakpoint = 1024;
        let tabletBreakpoint = 768;
        if (desktopMenuLeft && desktopMenuRight) {
            if (desktopMenuLeft.offsetWidth + desktopMenuRight.offsetWidth + logoContainer.offsetWidth + 120 > 1024) {
                desktopBreakpoint = desktopMenuLeft.offsetWidth + desktopMenuRight.offsetWidth + logoContainer.offsetWidth + 120;
            }
            if (desktopMenuLeft.offsetWidth + logoContainer.offsetWidth + 380 > 768) {
                tabletBreakpoint = desktopMenuLeft.offsetWidth + logoContainer.offsetWidth + 380;
            }
        } 

        console.log('initial breakpoints: ', desktopBreakpoint, tabletBreakpoint);
        //console.log('logoContainer: ', logoContainer.offsetWidth);
        const indexPageWithHeroLogo = document.querySelector(selectors.indexPageWithHeroLogo);
        if (indexPageWithHeroLogo) {
            const indexPageLeftMenu = indexPageWithHeroLogo.querySelector(".cmp-header__links--left");
            indexPageLeftMenu.style.cssText = `margin-left: -${logoContainer.offsetWidth + 30}px;`;
        }
        const mQueryTablet = window.matchMedia(`(min-width: ${tabletBreakpoint}px) and (max-width:  ${desktopBreakpoint}px)`);
        const mQueryMobile= window.matchMedia('(max-width: ' + tabletBreakpoint +'px)');
        if (mQueryTablet.matches) {
            navBar.classList.add("tablet-view");
            navBar.classList.remove("mobile-view");
        } else if (mQueryMobile.matches){
            navBar.classList.add("mobile-view");
            navBar.classList.remove("tablet-view");
        } else {
            navBar.classList.remove("mobile-view");
            navBar.classList.remove("tablet-view");
        }
        const mobileHandler = (e) => {
            if (e.matches) {
                navBar.classList.add("mobile-view");
                navBar.classList.remove("tablet-view");
            } else {
                navBar.classList.remove("mobile-view");
                mobileMenu.classList.add("hidden");
                mobileMenuTrigger.setAttribute("aria-expanded", "false");
            }
        }
        const tabletHandler = (e)=>{
            if (e.matches) {
                navBar.classList.add("tablet-view");
                navBar.classList.remove("mobile-view");
            } else {
                navBar.classList.remove("tablet-view");
                tabletMenu.classList.add("hidden");
                tabletMenuTrigger.setAttribute("aria-expanded", "false");
            }
        }
        if (mQueryMobile.addEventListener) {
            mQueryMobile.addEventListener('change', mobileHandler);
        } else {
            mQueryMobile.addListener(mobileHandler);
        }
        if (mQueryTablet.addEventListener) {
            mQueryTablet.addEventListener('change', tabletHandler);
        } else {
            mQueryTablet.addListener(tabletHandler);
        }
        // mQueryMobile.addEventListener('change', (e) => {
        //     if (e.matches) {
        //         navBar.classList.add("mobile-view");
        //         navBar.classList.remove("tablet-view");
        //     } else {
        //         navBar.classList.remove("mobile-view");
        //         mobileMenu.classList.add("hidden");
        //         mobileMenuTrigger.setAttribute("aria-expanded", "false");
        //     }
        // });
        // mQueryTablet.addEventListener('change', (e) => {
        //     if (e.matches) {
        //         navBar.classList.add("tablet-view");
        //         navBar.classList.remove("mobile-view");
        //     } else {
        //         navBar.classList.remove("tablet-view");
        //         tabletMenu.classList.add("hidden");
        //         tabletMenuTrigger.setAttribute("aria-expanded", "false");
        //     }
        // });
    },
    registerMenuToggleEvent: function() {
        const htmlEl = document.documentElement;
        const {selectors} = this;
        const mobileMenu = document.querySelector(selectors.mobileMenu);
        const mobileMenuTrigger = document.querySelector(selectors.mobileMenuTrigger);
        const tabletMenu = document.querySelector(selectors.tabletMenu);
        const tabletMenuTrigger = document.querySelector(selectors.tabletMenuTrigger);
        const header = document.querySelector(selectors.self);
        if (mobileMenuTrigger) {
            mobileMenuTrigger.addEventListener("click", () => {
                mobileMenu.classList.toggle("hidden");
                header.classList.toggle("mobile-menu-open");
                if (mobileMenu.classList.contains("hidden")){
                    mobileMenuTrigger.setAttribute("aria-expanded", "false");
                } else {
                    mobileMenuTrigger.setAttribute("aria-expanded", "true");
                }
            });
        } 
        if (tabletMenuTrigger) {
            const tabletMenuTriggerText = tabletMenuTrigger.querySelector(".visually-hidden");
            tabletMenuTrigger.addEventListener("click", () => {
                function setTabletMenuAttr(status){
                    if (status == 'open') {
                        tabletMenuTrigger.setAttribute("aria-expanded", "true");
                        tabletMenuTriggerText.textContent = "close";
                    } else if (status == "closed") {
                        tabletMenuTrigger.setAttribute("aria-expanded", "false");
                        tabletMenuTriggerText.textContent = "open";
                    }
                }
                function closeTabletMenu(event) {
                    if (event.target.closest("header")) {
                        return;
                    } else {
                        tabletMenu.classList.add("hidden");
                        header.classList.remove("tablet-menu-open");
                        setTabletMenuAttr('closed');
                    }
                }
                tabletMenu.classList.toggle("hidden");
                header.classList.toggle("tablet-menu-open");
                if (tabletMenu.classList.contains("hidden")){
                    setTabletMenuAttr('closed');
                    htmlEl.removeEventListener('click', closeTabletMenu);
                } else {
                    setTabletMenuAttr('open');
                    htmlEl.addEventListener('click', closeTabletMenu);
                }
            });
        } 
    },
    registerScrollEvent: function(){
        const header = document.querySelector(this.selectors.self);
        const leftMenu = header.querySelector(".cmp-header__links--left");
        const isIndexPageWithHeroLogo = document.querySelector(this.selectors.indexPageWithHeroLogo);
        const logoContainer = document.querySelector(this.selectors.logoContainer);
        window.addEventListener("scroll", function(){
                const scrollOffSet =  Math.round(0.15 * window.innerHeight);
                if (window.scrollY > scrollOffSet) {
                    header.classList.add("scrolled");
                    if (isIndexPageWithHeroLogo) {
                        console.log("remore style");
                        leftMenu.removeAttribute("style");
                    }
                } else {
                    header.classList.remove("scrolled");
                    if (isIndexPageWithHeroLogo) { 
                        leftMenu.style.cssText = `margin-left: -${logoContainer.offsetWidth + 30}px;`;
                    }
                }
        }); 
    },
    addActiveMenuClass: function(){
        const currentPath = location.pathname;
        if (currentPath) {
            const activeLinks = document.querySelectorAll('nav a.cmp-header__mobileMenu--link, nav a.cmp-header__tabletMenu--link, nav a.cmp-header__link');
            activeLinks.forEach(link => {
                const linkHref = link.getAttribute('href');
                if(linkHref && linkHref != '/' &&  linkHref == currentPath ) {
                    link.classList.add("active");
                }
            });
        }
    },
    init: function() {
        console.log('Header init!');
        this.setHeaderBreakpoint();
        this.registerMenuToggleEvent();
        this.registerScrollEvent();
        this.addActiveMenuClass();
    },

};
export default Header;

