
import * as componentScripts from '../components';

const loadedScripts = {};

let componentName, componentScript;
const DynamicComponents = {
    init: function () {
        const componentElements = document.querySelectorAll('[data-cmp-component]');
        componentElements.forEach(el => {
            componentName = el.getAttribute('data-cmp-component');
            componentScript = componentScripts.default[componentName];
            if ((typeof componentScript !== 'undefined') && !loadedScripts[componentName]) {
                loadedScripts[componentName] = true;
                componentScript.init();
            }
        });
        componentScripts.default["bodyendscheduletour"].init();//this has to be at the end
        console.log('loaded scripts: ', Object.keys(loadedScripts));
    } 
};
export default DynamicComponents;
