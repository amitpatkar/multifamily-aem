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
import junitx.framework.ComparableAssert;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class DataLayerRequestModelTest {

    private final AemContext context = BrookfieldTestContext.newAemContext();

    @BeforeEach
    public void setup() throws Exception {
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);

    }

    @Test
    public void testDataLayerForNavagionStandalone() {     
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.CMP_NAVIGATION_STANDALONE);
        DataLayerRequestModel toBeTested = context.request().adaptTo(DataLayerRequestModel.class);
        assertNotNull(toBeTested.getData());
        int indexOf = toBeTested.getData().indexOf("brookfieldpropertiesprogram/components/navigation_standalone");
        ComparableAssert.assertGreater(-1,indexOf);
    }
    
    @Test
    public void testDataLayerForFooterStandalone() {     
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.CMP_FOOTER_STANDALONE);
        DataLayerRequestModel toBeTested = context.request().adaptTo(DataLayerRequestModel.class);
        assertNotNull(toBeTested.getData());
        int indexOf = toBeTested.getData().indexOf("brookfieldpropertiesprogram/components/footer_standalone");
        ComparableAssert.assertGreater(-1,indexOf);
    }


}