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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class SliderConfigModelTest {

    private final AemContext aemContext = BrookfieldTestContext.newAemContext();

    private SliderConfig mock;

    public SliderConfigModelTest() {
        super();
    }

    @BeforeEach
    public void setup() throws Exception {
        aemContext.currentPage(BrookfieldTestContext.CONFIG_P);

        aemContext.load().json("/sliderconfig.json", "/sliderconfig");
        aemContext.currentResource("/sliderconfig");
        mock = aemContext.currentResource().adaptTo(SliderConfig.class);
    }

    @Test
    public void testTitle() throws Exception {
        Assert.assertEquals("title", mock.getTitle());
    }

    @Test
    public void sliderImagePath() throws Exception {
        Assert.assertEquals("sliderImagePath", mock.getSliderImagePath());
    }

    @Test
    public void getSliderImageLabel() throws Exception {
        Assert.assertEquals("sliderImageLabel", mock.getSliderImageLabel());
    }
    
    @Test
    public void getFeaturedPropertyList() throws Exception {
        Assert.assertNotNull(mock.getFeaturedPropertyList());
        Assert.assertEquals(3, mock.getFeaturedPropertyList().size());
        
        Assert.assertEquals("title", mock.getFeaturedPropertyList().get(0).getTitle());
        Assert.assertEquals("title", mock.getFeaturedPropertyList().get(1).getTitle());
        Assert.assertEquals("title", mock.getFeaturedPropertyList().get(2).getTitle());
    }

}
