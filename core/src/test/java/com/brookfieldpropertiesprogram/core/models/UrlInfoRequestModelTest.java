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
import org.apache.sling.api.resource.Resource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class UrlInfoRequestModelTest {

    final AemContext context = BrookfieldTestContext.newAemContext();
    UrlInfoRequestModel urlInfoRequestModel;
    
    /*
    @BeforeEach
    public void setup() throws Exception {
      
       
    }
*/
    
    @Test
    void testAptAvailabilityStandalone() {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);
        
        urlInfoRequestModel = context.request().adaptTo(UrlInfoRequestModel.class);
        Assert.assertEquals("/content/standalone/the-eugene/commoncontent.apartment-availability.html",urlInfoRequestModel.getAptAvailability());
        Assert.assertEquals("/standalone/the-eugene/commoncontent.apartment-availability.html",urlInfoRequestModel.getAptAvailabilityMapped());
        
        Assert.assertEquals("/content/standalone/the-eugene/commoncontent.apartment-type.html",urlInfoRequestModel.getAptType());
        Assert.assertEquals("/standalone/the-eugene/commoncontent.apartment-type.html",urlInfoRequestModel.getAptTypeMapped());
        
        Assert.assertEquals("/content/standalone/the-eugene/commoncontent.apartment.html",urlInfoRequestModel.getApt());
        Assert.assertEquals("/standalone/the-eugene/commoncontent.apartment.html",urlInfoRequestModel.getAptMapped());
    }
    
    @Test
    void testAptAvailabilityPortfolio() {
        // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_P_P);
        context.currentResource(BrookfieldTestContext.HOME_P_P);
        
        urlInfoRequestModel = context.request().adaptTo(UrlInfoRequestModel.class);
        Assert.assertEquals(BrookfieldTestContext.HOME_P_P + "/commoncontent.apartment-availability.html",urlInfoRequestModel.getAptAvailability());
        Assert.assertEquals(BrookfieldTestContext.HOME_P_P.replaceAll("/content", "") + "/commoncontent.apartment-availability.html",urlInfoRequestModel.getAptAvailabilityMapped());
        
        Assert.assertEquals(BrookfieldTestContext.HOME_P_P + "/commoncontent.apartment-type.html",urlInfoRequestModel.getAptType());
        Assert.assertEquals(BrookfieldTestContext.HOME_P_P.replaceAll("/content", "") + "/commoncontent.apartment-type.html",urlInfoRequestModel.getAptTypeMapped());
        
        Assert.assertEquals(BrookfieldTestContext.HOME_P_P + "/commoncontent.apartment.html",urlInfoRequestModel.getApt());
        Assert.assertEquals(BrookfieldTestContext.HOME_P_P.replaceAll("/content", "") + "/commoncontent.apartment.html",urlInfoRequestModel.getAptMapped());
    }

}
