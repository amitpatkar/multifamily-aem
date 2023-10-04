<%@page session="false" import="
        org.apache.sling.api.resource.Resource,
        org.apache.sling.api.resource.ResourceUtil,
        org.apache.sling.api.resource.ValueMap,
        org.apache.sling.api.resource.ResourceResolver,
        org.apache.sling.api.resource.ResourceMetadata,
        org.apache.sling.api.wrappers.ValueMapDecorator,
        com.adobe.granite.ui.components.ds.ValueMapResource,
        java.util.Collections,
        java.util.List,
        java.util.ArrayList,
        java.util.HashMap,
        java.util.Locale,
        java.util.Comparator,
        com.adobe.granite.ui.components.ds.DataSource,
        com.adobe.granite.ui.components.ds.EmptyDataSource,
        com.adobe.granite.ui.components.ds.SimpleDataSource,
        com.adobe.granite.ui.components.ds.ValueMapResource,
        com.brookfieldpropertiesprogram.core.models.KeyValuesModel,
        com.day.cq.wcm.api.Page,
com.day.cq.wcm.api.PageManager"%><%
%><%@include file="/libs/foundation/global.jsp"%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%
%><cq:defineObjects/><%
request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());
Locale locale = request.getLocale();
Resource datasource = resource.getChild("datasource");
ResourceResolver resolver = resource.getResourceResolver();
ValueMap dsProperties = ResourceUtil.getValueMap(datasource);
String genericListPath = dsProperties.get("path", String.class);
Boolean storeValueOnly = dsProperties.get("storeValueOnly", false);
Boolean useKeyValuesTemplate = dsProperties.get("useKeyValuesTemplate", false);
// What fields and values do we want from the children resources? This should be inside the component dialog.
//String value = dsProperties.get("value", String.class);
String text = dsProperties.get("jcr:title", String.class);

Resource parentResource = resourceResolver.getResource(genericListPath);
if (parentResource == null) {
    return;
}
Page parentResourcePage = parentResource.adaptTo(Page.class);
if (parentResourcePage != null && parentResourcePage.getContentResource() != null && parentResourcePage.getContentResource().getResourceType().equals("brookfieldpropertiesprogram/components/xfpage-key-values")) {
    useKeyValuesTemplate = true; //auto detection
}  
//log.info("genericListPath:{} useKeyValuesTemplate:{} parentResource.getResourceType():{}" ,genericListPath,useKeyValuesTemplate,parentResource.getResourceType());
// If the path isn't null,  get the resource and loop through the children.

  // Create a list to stuff our values
List<Resource> fakeResourceList = new ArrayList<Resource>();
ValueMap vmDefault = new ValueMapDecorator(new HashMap<String, Object>());            
vmDefault.put("value", "");
vmDefault.put("text", "--Select--");            
fakeResourceList.add(new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vmDefault));

  if (useKeyValuesTemplate) {
     KeyValuesModel keyValuesModel = parentResource.adaptTo(com.brookfieldpropertiesprogram.core.models.KeyValuesModel.class);
     if (keyValuesModel != null && keyValuesModel.getKeyValues() != null) {
        for(String key : keyValuesModel.getKeyValues().keySet()){
            ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());            
            vm.put("value", key);
            vm.put("text", keyValuesModel.getKeyValues().get(key));            
            fakeResourceList.add(new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm));
        }
     }
  }
  else {
    for(Resource child : parentResource.getChildren()){
        if (child.getName().equals("jcr:content")) continue; //skip child node
      ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
      ValueMap childProperties = ResourceUtil.getValueMap(child);
      vm.put("value", child.getPath());
      vm.put("text", childProperties.get("jcr:title",child.getName()));
      if (storeValueOnly) {
          vm.put("value", child.getName());
      }
      fakeResourceList.add(new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm));
    }
  }

  Collections.sort(fakeResourceList, new Comparator<Resource>() {
    public int compare(Resource o1, Resource o2)
    {
        ValueMap v1 = o1.adaptTo(ValueMap.class);
        ValueMap v2 = o2.adaptTo(ValueMap.class);
        return v1.get("text",String.class).compareToIgnoreCase(v2.get("text",String.class)); // Compare by name, for example
    }});
  // Create a new data source from iterating through our fakedResourceList
  DataSource ds = new SimpleDataSource(fakeResourceList.iterator());
  
  // Add the datasource to our request to expose in the view
  request.setAttribute(DataSource.class.getName(), ds);

%>