/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.brookfieldpropertiesprogram.core.models;

import com.brookfieldpropertiesprogram.core.dto.UnitImagesRequest;
import com.brookfieldpropertiesprogram.core.utils.ImageUtils;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class)
public class UnitImagesModel {

    private static final Logger LOG = LoggerFactory.getLogger(UnitImagesModel.class);
    //final Resource resource;

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @Inject
    @Source("request-attributes")
    String propertyId;

    @Inject
    @Source("request-attributes")
    String propertyName;

    final SlingHttpServletRequest request;
    @Inject
    @Source("request-attributes")
    String unitTypeOrUnit;

    final Map<String, Object> floorPlanImagePaths = new HashMap<>();
    final Map<String, Object> galleryImagePaths = new HashMap<>();

    final Map<String, String> availableSections = new HashMap<>();

    String unitTypeName;
    String unitName;

    String unitNumber;
    String unitNumberKey;

    String unitTypeKey;
    String unitKey;
    
    String floorPlanId;

    String heroImagePath;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    public UnitImagesModel(SlingHttpServletRequest request) {
        this.request = request;
    }

    @PostConstruct
    protected void init() {
        //String unit = request.getRequestPathInfo().getSuffix();
        if (StringUtils.isEmpty(unitTypeOrUnit)) {
            LOG.error("Unit is required");
            return;
        }
        LOG.info("UnitImages to Fetch:{}", unitTypeOrUnit);
        String unitPk;
        String[] parts = unitTypeOrUnit.split("[/]");
        if (parts.length < 3) { //<<Type>>/<<unitPK>>
            return;
        }
        unitTypeName = parts[1];
        unitPk = parts[2];
        String[] partsUnitPk = unitPk.split("[:]");
        unitName = partsUnitPk[partsUnitPk.length - 1];

        unitNumber = parts[3];
        unitNumberKey = ImageUtils.escape(unitNumber);

        UnitImagesRequest imagesRequest = new UnitImagesRequest(propertyId, propertyName, unitTypeOrUnit);

        unitTypeKey = ImageUtils.escape(unitTypeName);
        unitKey = ImageUtils.escape(unitPk);
        
        if (parts.length > 4) {
            floorPlanId = parts[4];
        }

        String[] supportedSections = new String[]{"floorplan", "gallery"};
        String[] pathsToFindSections = new String[]{
            "/content/experience-fragments/common/en/properties/" + propertyId + "/{0}/unit-" + unitKey + "/master/jcr:content/root",
            "/content/experience-fragments/common/en/properties/" + propertyId + "/{0}/unit" + "/master/jcr:content/root",
            "/content/experience-fragments/common/en/properties/shared/{0}/unit-" + unitKey + "/master/jcr:content/root",
            "/content/experience-fragments/common/en/properties/shared/{0}/unit" + "/master/jcr:content/root",};

        for (String aSection : supportedSections) {
            for (String aPathToSection : pathsToFindSections) {
                String actualPath = aPathToSection.replaceAll(Pattern.quote("{0}"), aSection);
                LOG.info("Finding section {}", actualPath);
                Resource r = request.getResourceResolver().getResource(actualPath);
                if (r != null && r.hasChildren()) {
                    availableSections.put(aSection, actualPath);
                    break;
                }
            }
        }

        //ResourceResolver resourceResolver;
        int counter = 1;

        Resource theParentFolder = request.getResourceResolver().getResource(imagesRequest.getSearchBasePath());
        if (theParentFolder == null || !theParentFolder.hasChildren()) {
            return;
        }
        for (String aPath : imagesRequest.getGalleryImagePaths()) {
            String[] toFindImages = new String[]{aPath + "-" + unitTypeName + "-" + unitNumber + ".jpg",aPath + "-" + unitNumber + ".jpg",aPath + "-unit" + ".jpg", aPath + ".jpg",aPath + "-" + unitTypeName + ".jpg",aPath + "-floorplan" + ".jpg"};
            boolean found = false;
            for (String toFindPath : toFindImages) {
                Resource childResource = theParentFolder.getChild(toFindPath);
                if (childResource != null) {
                    Asset childAsset = childResource.adaptTo(Asset.class);
                    if (childAsset != null) {
                        galleryImagePaths.put(aPath, childResource.getPath());
                        galleryImagePaths.put(aPath + "-title", (childAsset.getMetadataValue(JcrConstants.JCR_TITLE) == null) ? ("Gallery " + aPath + " for " + propertyName) : childAsset.getMetadataValue(JcrConstants.JCR_TITLE));
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                galleryImagePaths.put(aPath, "");
                galleryImagePaths.put(aPath + "-title", "");
            }
        }
        String fp = DamConstants.MOUNTPOINT_ASSETS + "/common/properties/" + propertyId + "/floorplan/" + unitTypeName + ".jpg";
        if (request.getResourceResolver().getResource(fp) != null) {
            floorPlanImagePaths.put("floorPlan", fp);
            floorPlanImagePaths.put("floorPlanTitle", propertyName);
        }
        else {
            floorPlanImagePaths.put("floorPlan", "{{@root.unit.floorPlan.floorPlanImage}}");
            floorPlanImagePaths.put("floorPlanTitle", propertyName);
        }

        if (galleryImagePaths != null && !galleryImagePaths.isEmpty() && galleryImagePaths.containsKey("hero") && StringUtils.isNotEmpty((String) galleryImagePaths.get("hero"))) {
            heroImagePath = (String) galleryImagePaths.get("hero");
        }
    }

    public String getFloorPlanId() {
        return floorPlanId;
    }
        
    public String getUnitTypeName() {
        return unitTypeName;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getUnitTypeKey() {
        return unitTypeKey;
    }

    public String getUnitKey() {
        return unitKey;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public String getUnitNumberKey() {
        return unitNumberKey;
    }

    public Map<String, String> getAvailableSections() {
        return availableSections;
    }

    public Map<String, Object> getGalleryImagePaths() {
        return galleryImagePaths;
    }

    public Map<String, Object> getFloorPlanImagePaths() {
        return floorPlanImagePaths;
    }

    public String getHeroImagePath() {
        return heroImagePath;
    }

}
