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
import junit.framework.Assert;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class NavigationRequestModelTest {
    private final AemContext context = BrookfieldTestContext.newAemContext();
    
    private NavigationRequestModel navigationRequestModel;
            
            
    @BeforeEach
    public void setup() throws Exception {
        context.currentPage(BrookfieldTestContext.HOME_S);             
        context.currentResource("/content/navigationcomponentmodel");
        //context.request().setResource(context.currentResource());
        //context.currentPage("/content/experience-fragments/siteconfig/sites/standalone/baseline");
        //System.out.println(context.currentPage().getName());
        navigationRequestModel =  context.request().adaptTo(NavigationRequestModel.class);       
    }

    @Test
    void testSiteConfigModel() throws Exception {
        // some very basic junit tests        
        assertNotNull(navigationRequestModel.getComponentModel());       
    }
    
     @Test
    void testGetSocialLinks() throws Exception {
        // some very basic junit tests        
        assertNotNull(navigationRequestModel.getComponentModel().getSocialLinks());       
        Assert.assertEquals(2,navigationRequestModel.getComponentModel().getSocialLinks().size());       
        //System.out.println(pageInfoModel.getData());
        //assertNotNull(pageInfoModel.getData());       
    }
    
    @Test
    void testGetMenuLinks() throws Exception {
        // some very basic junit tests        
        assertNotNull(navigationRequestModel.getComponentModel().getMenuLinks());     
        Assert.assertEquals(2,navigationRequestModel.getComponentModel().getMenuLinks().size());     
        //System.out.println(pageInfoModel.getData());
        //assertNotNull(pageInfoModel.getData());       
    }

}
