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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class CommonContentRequestModelTest {

    private final AemContext context = BrookfieldTestContext.newAemContext();

    private CommonContentRequestModel toBeTested;

   
    @Test
    void testWithSelector() throws Exception {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);
        
        context.requestPathInfo().setSelectorString("tos");

        toBeTested = context.request().adaptTo(CommonContentRequestModel.class);
        
        Assertions.assertEquals("/content/experience-fragments/common/en/content/tos/master",toBeTested.getResourceToFindPath());
       
        
    }
    
    @Test
    void testWithoutSelector() throws Exception {
        // some very basic junit tests        
        context.currentPage("/content/standalone/the-eugene/tos");
        context.currentResource("/content/standalone/the-eugene/tos");
        
        //context.requestPathInfo().setSelectorString("tos");

        toBeTested = context.request().adaptTo(CommonContentRequestModel.class);
        
        Assertions.assertEquals("/content/experience-fragments/common/en/content/tos/master",toBeTested.getResourceToFindPath());
       
        
    }
    
    
}
