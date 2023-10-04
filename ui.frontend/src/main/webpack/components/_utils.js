import {breakpointDesktop} from '../site/global/_breakpoints-export.scss';

export const Utils = {
  KeyCode: {
    BACKSPACE: 8,
    TAB: 9,
    RETURN: 13,
    ESC: 27,
    SPACE: 32,
    PAGE_UP: 33,
    PAGE_DOWN: 34,
    END: 35,
    HOME: 36,
    LEFT: 37,
    UP: 38,
    RIGHT: 39,
    DOWN: 40,
    DELETE: 46
  },
  hasClass: function(element, className) {
    return (new RegExp('(\\s|^)' + className + '(\\s|$)')).test(element.className);
  },
  addClass: function(element, className) {
    if(!this.hasClass(element, className)) {
      element.className += ' ' + className;
    }
  },
  removeClass: function(element, className) {
    var classRegex = new RegExp('(\\s|^)' + className + '(\\s|$)');
    element.className = element.className.replace(classRegex, ' ').trim();
  },
  listenChanges: function(element, options, callback) {
    const observer = new MutationObserver(callback);
    observer.observe(element, options);
    return observer;
  }
};

export function LoadImages(callback){
  [].forEach.call(this.querySelectorAll('img'),function(img){
      if (!img.getAttribute('src')) {
          const _img = new Image;
          const _src = img.getAttribute('data-src');
          _img.onload = function(){
            img.src = _src;
            img.classList.add('loaded');
            callback && callback(img);
          }
          if(img.src !== _src) {
            _img.src = _src;
          }	
      }
  });
};
export function DetectSwipe(element, callback){

  const touchElement = element;
  let swipeDirection;
  let startX;
  let startY;
  let distX;
  let distY;
  let elapsedTime;
  let startTime;
  const distThreshold = 50;
  const angleThreshold = 30 * Math.PI / 180;
  const timeThreshold = 500;
  const handleSwipe = callback || function(swipeDirection){console.log('swipe direction', swipeDirection);};


  touchElement.addEventListener('touchstart', function(e){
      const touchObj = e.changedTouches[0];
      swipeDirection = 'none';
      distX = 0;
      distY = 0;
      startX = touchObj.pageX;
      startY = touchObj.pageY;
      startTime = new Date().getTime();
  }, false)

  touchElement.addEventListener('touchmove', function(e){
    const touchObj = e.changedTouches[0];
    distX = touchObj.pageX - startX; 
    distY = touchObj.pageY - startY;
    elapsedTime = new Date().getTime() - startTime;
    if (elapsedTime <= timeThreshold){ 
        if (Math.abs(distX) >= distThreshold && (Math.abs(distY)/Math.abs(distX)) <= Math.tan(angleThreshold)){
          e.preventDefault();
        }
    }
  }, false);

  touchElement.addEventListener('touchend', function(e){
      const touchObj = e.changedTouches[0];
      distX = touchObj.pageX - startX; 
      distY = touchObj.pageY - startY;
      elapsedTime = new Date().getTime() - startTime;
      if (elapsedTime <= timeThreshold){ 
          if (Math.abs(distX) >= distThreshold && (Math.abs(distY)/Math.abs(distX)) <= Math.tan(angleThreshold)){
              swipeDirection = (distX < 0)? 'left' : 'right'; 
          }
          else if (Math.abs(distY) >= distThreshold && (Math.abs(distX)/Math.abs(distY)) <= Math.tan(angleThreshold)){ 
              swipeDirection = (distY < 0)? 'up' : 'down'; 
          }
      }
      handleSwipe(swipeDirection);
  }, false)
}
export const getColor = (() => {
    const cachedColors = {};
    return (color) => {
        if (color && color.indexOf("--") === 0) {
            if (!cachedColors[color]) {
                cachedColors[color] = getComputedStyle(document.documentElement).getPropertyValue(color);
            }
            return cachedColors[color]
        }
        return color;
    }
})();

export function ScrollToTop(element, distance) {
  window.scroll({
    top: element.getBoundingClientRect().top + window.scrollY - distance,
    left: 0,    
    behavior: 'smooth'
  });
}
export function ToggleScrolling(enabled){
  if(enabled){
    window.onscroll = function(){console.log('')};
  } else {
    const topScroll = window.pageYOffset || document.documentElement.scrollTop;
    const leftScroll = window.pageXOffset || document.documentElement.scrollLeft;
    window.onscroll = function() {
        window.scrollTo(leftScroll, topScroll);
    };
  }

}
export function IsDesktop(){
  return window.matchMedia(`(min-width: ${breakpointDesktop})`).matches;
}


