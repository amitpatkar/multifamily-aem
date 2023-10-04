use(function () {
    var testStr= this.value;
    return {
        isIcon: (testStr!= null && testStr.indexOf("/icons/") != -1)
    };

});