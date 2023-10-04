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
class NeighborhoodConfigModelTest {

    final AemContext context = BrookfieldTestContext.newAemContext();
    NeighborhoodConfigModel toBeTested;

    @BeforeEach
    public void setup() throws Exception {
        Page currentPage = context.currentPage(BrookfieldTestContext.CONFIG_N);
        Resource currentResource = context.currentResource(BrookfieldTestContext.CONFIG_N);

        toBeTested = context.currentPage().adaptTo(NeighborhoodConfigModel.class);
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
        Assertions.assertEquals("Reston", toBeTested.getTitle());
    }
}
