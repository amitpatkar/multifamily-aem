
// Stylesheets
import "./main.scss";
//import "../components/_availability-feed.js";

// Javascript or Typescript
// import "./**/*.js";
// import "./**/*.ts";
import FormStyles from "../components/_form-styles";
import Animation from "../components/_animation";
//import Lazyload from "../components/_lazyload";


import DynamicComponents from "./dynamic-components.js";

document.addEventListener("DOMContentLoaded", () => {
    DynamicComponents.init();
    Animation.init();
    FormStyles.init();
   // Lazyload.init();
});


