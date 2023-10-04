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
class NavigationComponentModelTest {
    private final AemContext context = BrookfieldTestContext.newAemContext();
    
    private NavigationComponentModel navigationComponentModel;
            
            
    @BeforeEach
    public void setup() throws Exception {
         
        context.currentResource("/content/navigationcomponentmodel");
        //context.currentPage("/content/experience-fragments/siteconfig/sites/standalone/baseline");
        //System.out.println(context.currentPage().getName());
        navigationComponentModel =  context.currentResource().adaptTo(NavigationComponentModel.class);       
    }

    @Test
    void testSiteConfigModel() throws Exception {
        // some very basic junit tests        
        assertNotNull(navigationComponentModel);       
    }
    
     @Test
    void testGetSocialLinks() throws Exception {
        // some very basic junit tests        
        assertNotNull(navigationComponentModel.getSocialLinks());       
        Assert.assertEquals(2,navigationComponentModel.getSocialLinks().size());       
        //System.out.println(pageInfoModel.getData());
        //assertNotNull(pageInfoModel.getData());       
    }
    
    @Test
    void testGetMenuLinks() throws Exception {
        // some very basic junit tests        
        assertNotNull(navigationComponentModel.getMenuLinks());     
        Assert.assertEquals(2,navigationComponentModel.getMenuLinks().size());     
        //System.out.println(pageInfoModel.getData());
        //assertNotNull(pageInfoModel.getData());       
    }

    @Test
    void testOpenInNewTabTrue() throws Exception {       
        Assert.assertEquals("true",navigationComponentModel.getMenuLinks().get(0).getOpenLinkInNewTab());  
    }
    
     @Test
    void testOpenInNewTabFalse() throws Exception {       
        Assert.assertEquals("false",navigationComponentModel.getMenuLinks().get(1).getOpenLinkInNewTab());     
    }

    
    @Test
    void testTargetBlank() throws Exception {               
        Assert.assertEquals("_blank",navigationComponentModel.getMenuLinks().get(0).getTarget());     
    }
    
    @Test
    void testTargetSelf() throws Exception {               
        Assert.assertEquals("_self",navigationComponentModel.getMenuLinks().get(1).getTarget());     
    }

}
