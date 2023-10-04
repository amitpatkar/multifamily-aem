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
import com.brookfieldpropertiesprogram.core.services.ContactUsService;
import com.brookfieldpropertiesprogram.core.services.EmailService;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.impl.EndPointImpl;
import com.brookfieldpropertiesprogram.core.services.impl.ContactUsServiceAnyoneHome;
import com.brookfieldpropertiesprogram.core.services.impl.SendgridEmailService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class FormPostServletTest {

    private final AemContext context = BrookfieldTestContext.newAemContext();
    private final FormPostServlet fixture = new FormPostServlet();
    EndPoint sendMock;
    EmailService sendgridEmailServiceMock;

    @BeforeEach
    public void setup() throws Exception {

        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);

        sendMock = new EndPointImpl();
        Map<String, Object> propsEndPointSendEmail = new HashMap<>();
        propsEndPointSendEmail.put("getEndPointAuthProvider", "com.brookfieldpropertiesprogram.core.services.http.impl.BearerTokenEndPointAuthProviderImpl");
        propsEndPointSendEmail.put("getEndPointName", "sendEmail");
        propsEndPointSendEmail.put("getEndPointURL", "https://api.sendgrid.com/v3/mail/send");
        propsEndPointSendEmail.put("getEndPointURL", "https://api.sendgrid.com/v3/mail/send");
        propsEndPointSendEmail.put("headerAuthorization", "Bearer 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");
        propsEndPointSendEmail.put("headerContentType", "application/json");

        context.registerInjectActivateService(sendMock, propsEndPointSendEmail);
        sendgridEmailServiceMock = new SendgridEmailService();
        //imsPrivateKeyMock = Mockito.mock(ImsPrivateKeyImpl.class);
        //Mockito.when(imsPrivateKeyMock.getPrivateKeyResource()).thenReturn(privateKeyResource);                        
        Map<String, Object> props = new HashMap<>();
        context.registerInjectActivateService(sendgridEmailServiceMock);

        ContactUsService contactUsServiceMock;
        contactUsServiceMock = new ContactUsServiceAnyoneHome();
        context.registerInjectActivateService(contactUsServiceMock);

        context.registerInjectActivateService(fixture);

    }

    @Test
    void testDoPostEmptyFormData() throws ServletException, IOException {
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        Exception exception = assertThrows(IOException.class, () -> {
            fixture.doPost(request, response);
        });

    }

}
