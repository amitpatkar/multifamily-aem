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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class PropertyInfoModelTest {

    private final AemContext context = BrookfieldTestContext.newAemContext();

    private PropertyConfigModel mock;

    public PropertyInfoModelTest() {
        super();
    }

    @BeforeEach
    public void setup() throws Exception {
        context.currentPage(BrookfieldTestContext.CONFIG_P);
        context.currentResource(BrookfieldTestContext.CONFIG_P);
        mock = context.currentPage().adaptTo(PropertyConfigModel.class);

    }

    @Test
    void testAnyoneHomeEmailAddress() throws Exception {
        // some very basic junit tests        
        assertNotNull(mock);
        assertNotNull(mock.getAddress());
        //System.out.println(mock.getAddress());
        //assertNotNull(mock.getAnyoneHomeEmailAddress());       
        //assertNotNull(mock.getCity());       
        assertNotNull(mock.getCustServiceEmailAddress());
        assertNotNull(mock.getData());
        assertNotNull(mock.getDescription());

        assertNotNull(mock.getFeaturedImage());
        assertNotNull(mock.getGoogleAddress());
        assertNotNull(mock.getHeadline());

        assertNotNull(mock.getLogo());
        assertNotNull(mock.getOnesiteID());
        assertNotNull(mock.getPhoneNumber());
        assertNotNull(mock.getTitle());

        assertNotNull(mock.getResidentPortalURL());
        assertNotNull(mock.getFacebookURL());
        assertNotNull(mock.getInstagramURL());
        assertNotNull(mock.getTwitterURL());
        assertNotNull(mock.getWebsiteURL());

        TestLoggerFactory.getInstance().getLogger(PropertyInfoModelTest.class).info(mock.getAnyoneHomeEmailAddress());    
    }
    
    @Test
    void testGetMenuLinksRight() throws Exception {
        Assertions.assertEquals(0, mock.getMenuLinksRight().size());   
    }
    
    @Test
    void testGetUnitTypeIdTourMappingMap() throws Exception {
        Assertions.assertEquals(1, mock.getUnitTypeIdTourMappingMap().size());
    }
    
    @Test
    void testGetUnitIdTourMappingMap() throws Exception {
        Assertions.assertEquals(1, mock.getUnitIdTourMappingMap().size());     
    }

    
    @Test
    void testAmenities() throws Exception {
        Assertions.assertEquals(2, mock.getAmenities().size());
    }

    @Test
    void testLocationId() throws Exception {
        Assertions.assertEquals("1234", mock.getLocationId());
    }
    
    @Test
    void testOfficeHoursMon() throws Exception {
        Assertions.assertEquals("7-6", mock.getOfficeHoursMon());
    }
    
    @Test
    void testOfficeHoursTue() throws Exception {
        Assertions.assertEquals("8-6", mock.getOfficeHoursTue());
    }
    
    @Test
    void testOfficeHoursWed() throws Exception {
        Assertions.assertEquals("9-6", mock.getOfficeHoursWed());
    }
    
    @Test
    void testOfficeHoursThu() throws Exception {
        Assertions.assertEquals("10-6", mock.getOfficeHoursThu());
    }
    
    @Test
    void testOfficeHoursFri() throws Exception {
        Assertions.assertEquals("11-6", mock.getOfficeHoursFri());
    }
    
    @Test
    void testOfficeHoursSat() throws Exception {
        Assertions.assertEquals("12-6", mock.getOfficeHoursSat());
    }
    
    @Test
    void testOfficeHoursSun() throws Exception {
        Assertions.assertEquals("13-6", mock.getOfficeHoursSun());
    }
    
    @Test
    void testOfficeHours() throws Exception {
        Assertions.assertEquals(7, mock.getOfficeHours().size());
        Assertions.assertEquals("Monday: 7-6", mock.getOfficeHours().get(0));
        Assertions.assertEquals("Tuesday: 8-6", mock.getOfficeHours().get(1));
        Assertions.assertEquals("Wednesday: 9-6", mock.getOfficeHours().get(2));
        Assertions.assertEquals("Thursday: 10-6", mock.getOfficeHours().get(3));
        Assertions.assertEquals("Friday: 11-6", mock.getOfficeHours().get(4));
        Assertions.assertEquals("Saturday: 12-6", mock.getOfficeHours().get(5));
        Assertions.assertEquals("Sunday: 13-6", mock.getOfficeHours().get(6));
    }

}
