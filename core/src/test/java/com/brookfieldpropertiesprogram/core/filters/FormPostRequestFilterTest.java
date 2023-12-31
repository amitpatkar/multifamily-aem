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
package com.brookfieldpropertiesprogram.core.filters;

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import com.brookfieldpropertiesprogram.core.services.EmailService;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;

@ExtendWith(AemContextExtension.class)
class FormPostRequestFilterTest{
    private final AemContext context = BrookfieldTestContext.newAemContext();
    private final FormPostRequestFilter fixture = new FormPostRequestFilter();
    EndPoint sendMock;
    EmailService sendgridEmailServiceMock;
    
    @Mock
    FilterChain chain;
    
  
    @BeforeEach
    public void setup() throws Exception {
       context.currentPage(BrookfieldTestContext.HOME_S);     
       context.currentResource(BrookfieldTestContext.HOME_S);    
        
      
        context.registerInjectActivateService(fixture);

    }

    @Test
    void testDoPostEmptyFormData() throws ServletException, IOException {
        
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();
        
        fixture.init(mock(FilterConfig.class));
        fixture.doFilter((ServletRequest) request,(ServletResponse)  response, mock(FilterChain.class));
        fixture.destroy();
       

    }
    /*
    @Test
    void testDoPostEmptyFormData() throws ServletException, IOException {
        context.currentPage("/content/standalone/the-eugene/en/index");     
        context.currentResource("/content/standalone/the-eugene/en/index"); 
        
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();
        
        fixture.init(mock(FilterConfig.class));
        fixture.doFilter((ServletRequest) request,(ServletResponse)  response, mock(FilterChain.class));
        fixture.destroy();
       

    }
   */
    
}
