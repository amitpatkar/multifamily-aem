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

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class PageInfoRequestModelTest {

    private final AemContext context = BrookfieldTestContext.newAemContext();

    private PageInfoRequestModel pageInfoRequestModel;

    @Test
    void getNullWhenNoPage() throws Exception {
        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);
        assertNull(pageInfoRequestModel);        
    }
    
    
    @Test
    void testStandaloneHome() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getPropertyConfigModel());
    }
    
    @Test
    void testStandaloneHomeNotFromRequestAttributes() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getPropertyConfigModel());       
        assertNull(context.request().getAttribute("returnedFromRequestAttribute"));
        
    }
    
    @Test
    void testStandaloneHomeRequestAttributes() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getPropertyConfigModel());
        
         pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);
         assertNotNull(context.request().getAttribute("returnedFromRequestAttribute"));
        
    }

    @Test
    void testPortfolioHome() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P);
        context.currentResource(BrookfieldTestContext.HOME_P);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
    }
    
    @Test
    void testPortfolioProperty() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P_P);
        context.currentResource(BrookfieldTestContext.HOME_P_P);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getPropertyConfigModel());
        assertNull(pageInfoRequestModel.getCityConfigModel());
        assertNull(pageInfoRequestModel.getNeighborhoodConfigModel());        
        assertNull(pageInfoRequestModel.getMetroAreaConfigModel());
    }

    @Test
    void testPortfolioNeighborhood() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P_N);
        context.currentResource(BrookfieldTestContext.HOME_P_N);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getNeighborhoodConfigModel());

        assertNull(pageInfoRequestModel.getPropertyConfigModel());
        assertNull(pageInfoRequestModel.getCityConfigModel());
        assertNull(pageInfoRequestModel.getMetroAreaConfigModel());
    }

    @Test
    void testPortfolioCity() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P_C);
        context.currentResource(BrookfieldTestContext.HOME_P_C);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getCityConfigModel());

        assertNull(pageInfoRequestModel.getNeighborhoodConfigModel());
        assertNull(pageInfoRequestModel.getPropertyConfigModel());
        assertNull(pageInfoRequestModel.getMetroAreaConfigModel());
    }
    
    @Test
    void testPortfolioState() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P_S);
        context.currentResource(BrookfieldTestContext.HOME_P_S);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getStateConfigModel());

        assertNull(pageInfoRequestModel.getNeighborhoodConfigModel());
        assertNull(pageInfoRequestModel.getPropertyConfigModel());
        assertNull(pageInfoRequestModel.getMetroAreaConfigModel());
    }
    
    @Test
    void testPortfolioCollection() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P_CL);
        context.currentResource(BrookfieldTestContext.HOME_P_CL);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getSiteConfigModel());
        assertNotNull(pageInfoRequestModel.getCollectionConfigModel());

        assertNull(pageInfoRequestModel.getStateConfigModel());
        assertNull(pageInfoRequestModel.getNeighborhoodConfigModel());
        assertNull(pageInfoRequestModel.getPropertyConfigModel());
        assertNull(pageInfoRequestModel.getMetroAreaConfigModel());
    }
    
     @Test
    void testUnitImagesModel() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.PROPERTY_DETAIL_S);
        context.currentResource(BrookfieldTestContext.PROPERTY_DETAIL_S);
        context.requestPathInfo().setSuffix("/unit");

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getUnitImagesModel());
    }
    
     @Ignore
    void testUnitTypeImagesModel() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.PROPERTY_TYPE_S);
        context.currentResource(BrookfieldTestContext.PROPERTY_TYPE_S);
        context.requestPathInfo().setSuffix("/unit");
        context.request().setPathInfo("/unit");

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getUnitTypeImagesModel());
    }

    
    @Test
    void testJSONLd() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P_C);
        context.currentResource(BrookfieldTestContext.HOME_P_C);

        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);

        assertNotNull(pageInfoRequestModel.getJsonLdString());        
    }
}
