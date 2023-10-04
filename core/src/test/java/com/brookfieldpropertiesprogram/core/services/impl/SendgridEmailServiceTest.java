package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import com.brookfieldpropertiesprogram.core.services.EmailService;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.impl.EndPointImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(AemContextExtension.class)
public class SendgridEmailServiceTest {

    private final AemContext context = BrookfieldTestContext.newAemContext();
    EndPoint sendMock;
    EmailService sendgridEmailServiceMock;
    
    Logger LOG = LoggerFactory.getLogger(SendgridEmailServiceTest.class);
    
    
    @BeforeEach
    public void setup(AemContext context) throws Exception {
        context.currentPage(BrookfieldTestContext.HOME_S);     
        context.currentResource(BrookfieldTestContext.HOME_S); 
        sendMock = new EndPointImpl();
        Map<String, Object> propsEndPointSendEmail = new HashMap<>();
        propsEndPointSendEmail.put("getEndPointAuthProvider", "com.brookfieldpropertiesprogram.core.services.http.impl.BearerTokenEndPointAuthProviderImpl");
        propsEndPointSendEmail.put("getEndPointName", "sendEmail");
        propsEndPointSendEmail.put("getEndPointURL", "https://api.sendgrid.com/v3/mail/send");
        propsEndPointSendEmail.put("headerAuthorization", "Bearer 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");
        propsEndPointSendEmail.put("headerContentType", "application/json");

        context.registerInjectActivateService(sendMock, propsEndPointSendEmail);

        sendgridEmailServiceMock = new SendgridEmailService();
        //imsPrivateKeyMock = Mockito.mock(ImsPrivateKeyImpl.class);
        //Mockito.when(imsPrivateKeyMock.getPrivateKeyResource()).thenReturn(privateKeyResource);                        
        Map<String, Object> props = new HashMap<>();
        props.put("getFromEmailAddress", "no-reply@rent.brookfieldproperties.com");
        props.put("getFromEmailAddressName", "Brookfield Properties");
        context.registerInjectActivateService(sendgridEmailServiceMock,props);
    }

    @Test
    public void testSendPlainText() {
        System.out.println(context.pageManager().getClass().toGenericString());
        try {
            sendgridEmailServiceMock.sendPlainText("amitpatkar@gmail.com",null,"test email","{\"test:\":\"amitpatkar@gmail.com\",\"firstName:\":\"amitpatkar@gmail.com\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
