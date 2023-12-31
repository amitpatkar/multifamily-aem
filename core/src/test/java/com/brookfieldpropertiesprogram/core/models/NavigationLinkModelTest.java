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
class NavigationLinkModelTest {
    private final AemContext context = BrookfieldTestContext.newAemContext();
    
    private NavigationLink navigationLink;
            
            
    @BeforeEach
    public void setup() throws Exception {
         
        context.currentResource("/content/navigationcomponentmodel/socialLinks/item0");
        //context.currentPage("/content/experience-fragments/siteconfig/sites/standalone/baseline");
        //System.out.println(context.currentPage().getName());
        navigationLink =  context.currentResource().adaptTo(NavigationLink.class);       
    }

    @Test
    void testSiteConfigModel() throws Exception {
        // some very basic junit tests        
        assertNotNull(navigationLink);       
    }
    
     @Test
    void testLabel() throws Exception {
        // some very basic junit tests        
        assertNotNull(navigationLink.getLabel());       
        Assert.assertEquals("social",navigationLink.getLabel());
        Assert.assertEquals("/",navigationLink.getUrl());
        Assert.assertEquals("icon2",navigationLink.getIcon());
        Assert.assertEquals("false",navigationLink.getOpenLinkInNewTab());
        
        //System.out.println(pageInfoModel.getData());
        //assertNotNull(pageInfoModel.getData());       
    }
  
}
