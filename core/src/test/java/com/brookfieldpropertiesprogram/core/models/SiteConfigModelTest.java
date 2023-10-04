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
import junitx.framework.ComparableAssert;
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
class SiteConfigModelTest {
    final AemContext context = BrookfieldTestContext.newAemContext();
    SiteConfigModel siteConfigModel;

    @BeforeEach
    public void setup() throws Exception {                
        Page currentPage = context.currentPage("/content/siteconfig/sites/standalone/the-eugene");        
        Resource currentResource = context.currentResource("/content/siteconfig/sites/standalone/the-eugene");
        
        siteConfigModel =  context.currentPage().adaptTo(SiteConfigModel.class);       
    }

    @Test
    void testSiteConfigModel() throws Exception {
        // some very basic junit tests        
        assertNotNull(siteConfigModel);       
    }
    
    @Test
    void testSiteConfigModelData() throws Exception {
        // some very basic junit tests        
        assertNotNull(siteConfigModel);       
        //System.out.println(siteConfigModel.getData());
        assertNotNull(siteConfigModel.getData());       
    }
    
    @Test
    void testFooterCtaHeading() throws Exception {
        Assertions.assertEquals("Features & Amenities ${title}",siteConfigModel.getFooter_cta_heading());
    }
    
    @Test
    void testgetFooter_cta_label() throws Exception {
        Assertions.assertEquals("hello",siteConfigModel.getFooter_cta_label());
    }
    
    @Test
    void testgetFooter_cta_absUrl() throws Exception {
        Assertions.assertEquals("/content/somecontentpage.html",siteConfigModel.getFooter_cta_absUrl());
    }
    
    @Test
    void getFooter_cta_target() throws Exception {
        Assertions.assertEquals("_self",siteConfigModel.getFooter_cta_target());
    }
    
    @Test
    void getFooter_cta_url() throws Exception {
        Assertions.assertEquals("/content/somecontentpage",siteConfigModel.getFooter_cta_url());
    }
    
    @Test
    void getFooterMenuLinksLeft() throws Exception {
        Assertions.assertEquals(1,siteConfigModel.getFooterMenuLinksLeft().size());
    }
    
    @Test
    void getFooterMenuLinksRight() throws Exception {
        Assertions.assertEquals(8,siteConfigModel.getFooterMenuLinksRight().size());
    }
    
    @Test
    void testTheme()  throws Exception {
        Assertions.assertEquals("theme-1",siteConfigModel.getTheme());
    }
    
    @Test
    void testActiveVariation()  throws Exception {
        Assertions.assertEquals("master",siteConfigModel.getActiveVariation());
    }
    
    @Test
    void testImageServerUrl()  throws Exception {
        Assertions.assertEquals("https://imageserver.com",siteConfigModel.getImageServiceUrl());
    }
    
    @Test
    void testHeaderMenuLinksLeft() {
        assertNotNull(siteConfigModel);  
        ComparableAssert.assertGreater(0,siteConfigModel.getHeaderMenuLinksLeft().size());
    }

}
