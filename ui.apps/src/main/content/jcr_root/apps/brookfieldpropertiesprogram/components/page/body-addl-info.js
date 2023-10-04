use(function () {
    var rootClasses = this.rootClasses;
    var pageClass = currentPage.getName() + "-page";
    return {
        cssClassName: (rootClasses !== null ? rootClasses.join(' ') + ' ' + pageClass: pageClass)
    }
});