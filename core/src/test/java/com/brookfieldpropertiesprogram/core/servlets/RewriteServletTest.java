/*
 *  Copyright 2018 Adobe Systems Incorporated
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
package com.brookfieldpropertiesprogram.core.servlets;

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class RewriteServletTest {

    private final AemContext context = BrookfieldTestContext.newAemContext();
    private final RewriteServlet fixture = new RewriteServlet();

    @BeforeEach
    public void setup() throws Exception {
        context.currentPage("/content/siteconfig/sites/standalone/the-eugene");
        context.currentResource("/content/siteconfig/sites/standalone/the-eugene");        
        
        context.registerInjectActivateService(fixture);

    }

    @Test
    void test404() throws ServletException, IOException {
        context.currentPage("/content/siteconfig/sites/standalone/the-eugene");
        context.currentResource("/content/siteconfig/sites/standalone/the-eugene");
        context.requestPathInfo().setSelectorString("rewrite");
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();
        fixture.doGet(request, response);
        Assertions.assertEquals(404, response.getStatus());
        //StringAssert.assertContains("home",response.getOutputAsString());
    }
    
     @Test
    void test200() throws ServletException, IOException {
        context.currentPage("/content/siteconfig/sites/standalone/the-eugene");
        context.currentResource("/content/siteconfig/sites/standalone/the-eugene");        
        context.requestPathInfo().setSuffix("/availability");
        //MockSlingHttpServletRequest request = context.request();        
        context.requestPathInfo().setSelectorString("rewrite");
        //MockSlingHttpServletResponse response = context.response();
        //fixture.doGet(context.request(), context.response());
        //Assertions.assertEquals(404, context.response().getStatus());
        //StringAssert.assertContains("home",context.response().getOutputAsString());
    }

}
