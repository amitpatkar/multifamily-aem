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
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class PropertyConfigModelTest {

    final AemContext context = BrookfieldTestContext.newAemContext();
    PropertyConfigModel toBeTested;

    @BeforeEach
    public void setup() throws Exception {
        context.currentPage(BrookfieldTestContext.CONFIG_P);
        context.currentResource(BrookfieldTestContext.CONFIG_P);

        toBeTested = context.currentPage().adaptTo(PropertyConfigModel.class);
    }

    @Test
    void testModel() throws Exception {
        // some very basic junit tests        
        assertNotNull(toBeTested);
    }

    @Test
    void testActiveVariation() throws Exception {
        assertNotNull(toBeTested.getActiveVariation());
        Assertions.assertEquals("back_to_school", toBeTested.getActiveVariation());
    }

    @Test
    void testTheme() throws Exception {
        assertNotNull(toBeTested.getTheme());
        Assertions.assertEquals("theme-eugene", toBeTested.getTheme());
    }

    //Chicago Metro Area
    @Test
    void testTitle() throws Exception {
        assertNotNull(toBeTested.getTitle());
        Assertions.assertEquals("The Eugene", toBeTested.getTitle());
    }
    
    @Test
    void testOneSiteId() throws Exception {
        assertNotNull(toBeTested.getTitle());
        Assertions.assertEquals("4545111", toBeTested.getOnesiteID());
    }
    
    
    @Test
    void testTourProviderUnit() throws Exception {
        assertNotNull(toBeTested.getTourProviderUnit());
        Assertions.assertEquals("matterport", toBeTested.getTourProviderUnit());        
    }

    
    @Test
    void testPhoneNumber() throws Exception {
        assertNotNull(toBeTested.getTitle());
        Assertions.assertEquals("8443966697", toBeTested.getPhoneNumber());
        Assertions.assertEquals("(844) 396-6697", toBeTested.getPhoneNumberFormatted());
    }

    @Test
    void testMapLink() throws Exception {
        assertNotNull(toBeTested.getMapLink());
    } 
    
    @Test
    void testAmenities() throws Exception {
        assertNotNull(toBeTested.getAmenities());
        Assertions.assertEquals(false,toBeTested.getAmenities().isEmpty());
        Assertions.assertEquals( "value0", toBeTested.getAmenities().get("key0"));
    } 
    
    @Test
    void testExpiredSpecials() throws Exception {     
        Resource r = context.resourceResolver().getResource(BrookfieldTestContext.CONFIG_P);
        context.pageManager().delete(r, false); //first delelte the resource and then load new one
        context.load().json(BrookfieldTestContext.TEST_CONFIG_P_EXPIRED_SPECIALS, BrookfieldTestContext.CONFIG_P);
        context.currentPage(BrookfieldTestContext.CONFIG_P);
        context.currentResource(BrookfieldTestContext.CONFIG_P);
        PropertyConfigModel toBeTestedWithExpiredSpecials = context.currentPage().adaptTo(PropertyConfigModel.class);
        Assertions.assertEquals(true,toBeTestedWithExpiredSpecials.isSpecialsExpired());
    }
}
