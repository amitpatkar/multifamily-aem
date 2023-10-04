import LazyLoad from "vanilla-lazyload";

const Lazyload = {
    init: function () {
        if (document.querySelector('img.lazy')) {
            var lazyLoadInstance = new LazyLoad({
                threshold: 0
            });
        }

    }
};
export default Lazyload;