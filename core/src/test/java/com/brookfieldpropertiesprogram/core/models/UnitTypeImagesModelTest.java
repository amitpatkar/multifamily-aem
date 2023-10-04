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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class UnitTypeImagesModelTest {
    final AemContext context = BrookfieldTestContext.newAemContext();
    UnitTypeImagesModel toBeTested;

    @BeforeEach
    public void setup() throws Exception {                
          // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.PROPERTY_TYPE_S);
        context.currentResource(BrookfieldTestContext.PROPERTY_TYPE_S);
        
        context.request().setAttribute("propertyId", "the-eugene");
        context.request().setAttribute("propertyName", "The Eugene");
        context.request().setAttribute("unitTypeOrUnit", "/unit2/1234");
        
        toBeTested =  context.request().adaptTo(UnitTypeImagesModel.class);       
    }

    @Test
    void testSiteConfigModel() throws Exception {
        // some very basic junit tests        
        assertNotNull(toBeTested);       
    }
    
    @Test
    void testFloorPlanImagePaths() throws Exception {
        // some very basic junit tests        
        assertNotNull(toBeTested.getFloorPlanImagePaths());       
    }
    
    @Test
    void testGalleryImagePaths() throws Exception {
        // some very basic junit tests        
        assertNotNull(toBeTested.getGalleryImagePaths());       
    }

}
