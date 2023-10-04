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
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class AssociationConfigModelTest {
    final AemContext context = BrookfieldTestContext.newAemContext();
    AssociationConfigModel toBeTested;

    @BeforeEach
    public void setup() throws Exception {                
        Page currentPage = context.currentPage(BrookfieldTestContext.CONFIG_CL);        
        Resource currentResource = context.currentResource(BrookfieldTestContext.CONFIG_CL);        
        toBeTested =  context.currentPage().getContentResource().adaptTo(AssociationConfigModel.class);       
    }

    @Test
    void testCityConfigModel() throws Exception {
        // some very basic junit tests                
        assertNotNull(toBeTested);       
    }
    
    @Test
    void  testGetStateCode() {
        assertNotNull(toBeTested.getStateCode());
    }

   @Test
    void  testGetStateName() {
        assertNotNull(toBeTested.getStateName());
    }

    @Test
    void  testGetCity() {
        assertNotNull(toBeTested.getCity());
    }
    
    @Test
    void  testGetPropertyConfigModels() {        
        assertNotNull(toBeTested.getPropertyConfigModels());
    }
    
    @Test
    void  testGetCollectionsConfigModels() {
        //Page currentPage =context.currentPage("/content/cmp_section_50_50_carousel");
        Resource currentResource = context.currentResource("/content/cmp_section_50_50_carousel");
        toBeTested =  context.currentResource().adaptTo(AssociationConfigModel.class);       
        assertNotNull(toBeTested.getCollectionConfigModels());
        
        Assertions.assertEquals(3,toBeTested.getCollectionConfigModels().size());
    }

}
