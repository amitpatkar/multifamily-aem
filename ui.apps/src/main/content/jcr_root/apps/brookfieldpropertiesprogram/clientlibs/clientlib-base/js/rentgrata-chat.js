(function () {
    var props = document.getElementById('rentgrata-props');
    if (!props) return;
    var hjs = document.getElementsByTagName('script')[0];
    var js = document.createElement('script');
    js.async = true;
    js.src = 'https://widget.rentgrata.com/rentgrata.js';
    js.addEventListener('load', function () {
        var args = {
            widget_key: props.getAttribute("data-chat-key")
        }
        window.rentgrata.Widget.render(args);
    });
    hjs.parentNode.insertBefore(js, hjs);
})();