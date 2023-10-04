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
import junit.framework.Assert;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class KeyValuesModelTest {

    final AemContext context = BrookfieldTestContext.newAemContext();
    KeyValuesModel toBeTested;

    @BeforeEach
    public void setup() throws Exception {
        Page currentPage = context.currentPage(BrookfieldTestContext.CONFIG_LINK_TYPES);
        Resource currentResource = context.currentResource(BrookfieldTestContext.CONFIG_LINK_TYPES);

        toBeTested = context.currentPage().adaptTo(KeyValuesModel.class);
    }

    @Test
    public void testKeyValues() {
        Assert.assertNotNull(toBeTested);
    }
    
    @Test
    public void testKeyValuesNotNull() {
        Assert.assertNotNull(toBeTested.getKeyValues());
    }

    @Test
    public void testKeyValuesTotal() {
        Assert.assertEquals(3, toBeTested.getKeyValues().size());
    }
    @Test
    public void getValue() {
        Assert.assertEquals("value0", toBeTested.getKeyValues().get("key0"));
        Assert.assertEquals("value2", toBeTested.getKeyValues().get("key2"));
    }
}
