Handlebars.registerHelper("eachWithLimit", function(context,maxLimit, options) {
  var ret = "";  
  for (var i = 0, j = context.length ; i < j; i++) {
    if (i >= maxLimit) break;
    ret = ret + options.fn(context[i]);
  }

  return ret;
});