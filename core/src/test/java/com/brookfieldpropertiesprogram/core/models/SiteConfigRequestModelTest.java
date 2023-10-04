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
import org.apache.sling.api.resource.Resource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class SiteConfigRequestModelTest {

    final AemContext context = BrookfieldTestContext.newAemContext();
    SiteConfigRequestModel siteConfigModel;
    
    /*
    @BeforeEach
    public void setup() throws Exception {
      
       
    }
*/
    
    @Test
    void testGetRedirectRuleResultTarget() {
        Resource currentResource = context.currentResource("/content/siteconfig/sites/standalone/the-eugene");
        context.requestPathInfo().setSuffix("/availability");
        siteConfigModel = context.request().adaptTo(SiteConfigRequestModel.class);
        assertNotNull(siteConfigModel.getRedirectRuleResult().getResourcePathToRedirectTo());
        assertEquals("/content/standalone/the-eugene/home", siteConfigModel.getRedirectRuleResult().getResourcePathToRedirectTo());
    }

    @Test
    void testGetRedirectRuleResultTargetWithRegEx() {
        context.currentPage("/content/siteconfig/sites/standalone/demo");
        context.currentResource("/content/siteconfig/sites/standalone/demo");
        context.requestPathInfo().setSuffix("/availability");
        siteConfigModel = context.request().adaptTo(SiteConfigRequestModel.class);
        assertNotNull(siteConfigModel.getRedirectRuleResult().getResourcePathToRedirectTo());
        assertEquals("/content/standalone/demo/apartment-availability", siteConfigModel.getRedirectRuleResult().getResourcePathToRedirectTo());
    }

}
