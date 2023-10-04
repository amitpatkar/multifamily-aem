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
class CollectionConfigModelTest {

    final AemContext context = BrookfieldTestContext.newAemContext();
    CollectionConfigModel toBeTested;

    @BeforeEach
    public void setup() throws Exception {
        Page currentPage = context.currentPage(BrookfieldTestContext.CONFIG_CL);
        Resource currentResource = context.currentResource(BrookfieldTestContext.CONFIG_CL);

        toBeTested = context.currentPage().adaptTo(CollectionConfigModel.class);
    }

    @Test
    void testTypeCast() throws Exception {
        // some very basic junit tests        
        assertNotNull(toBeTested);
    }

    @Test
    void testGetCollectionPropertiesConfigModel() throws Exception {
        // some very basic junit tests        
        assertNotNull(toBeTested.getCollectionPropertiesConfigModel());
        assertNotNull(toBeTested.getCollectionPropertiesConfigModel().getPropertyConfigModels());
        Assertions.assertEquals(2, toBeTested.getCollectionPropertiesConfigModel().getPropertyConfigModels().size());
        Assertions.assertEquals("one-franklin-town", toBeTested.getCollectionPropertiesConfigModel().getPropertyConfigModels().get(0).getName());
    }

    @Test
    void testLocationId() throws Exception {
        Assertions.assertEquals("1234", toBeTested.getLocationId());
        //System.out.println(pageInfoModel.getData());
        //assertNotNull(pageInfoModel.getData());       
    }

}
