package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import com.brookfieldpropertiesprogram.core.models.GlobalConfigRequestModel;
import com.brookfieldpropertiesprogram.core.models.PageInfoRequestModel;
import com.brookfieldpropertiesprogram.core.services.ContactUsService;
import com.brookfieldpropertiesprogram.core.services.EmailService;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.impl.EndPointImpl;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
public class ContactUsServiceAnyoneHomeTest {

    private final AemContext context = BrookfieldTestContext.newAemContext();
    EndPoint sendMock;
    EmailService sendgridEmailServiceMock;
    
    ContactUsService contactUsServiceMock;
    Page currentPage;
    
    PageInfoRequestModel pageInfoRequestModel;
    
    GlobalConfigRequestModel globalConfigModel;

    @BeforeEach
    public void setup(AemContext context) throws Exception {
        context.currentPage(BrookfieldTestContext.HOME_S);     
        context.currentResource(BrookfieldTestContext.HOME_S);    
        
        pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);
               
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
        
        contactUsServiceMock = new ContactUsServiceAnyoneHome();
        context.registerInjectActivateService(contactUsServiceMock);
        
        globalConfigModel = context.currentResource().adaptTo(GlobalConfigRequestModel.class);
    
        

    }

    @Test
    public void testParseJSONAndSendEmail() {
        Assertions.assertNotNull(pageInfoRequestModel);
        //System.out.println(context.pageManager().getClass().toGenericString());
        try {
            contactUsServiceMock.parseJSONAndSendEmail(globalConfigModel,pageInfoRequestModel.getPropertyConfigModel(),"{\n" +
                    "\t\"first_name\": \"John\",\n" +
                    "\t\"last_name\": \"Doe\",\n" +
                    "\t\"email\": \"john@doe.com\",\n" +
                    "\t\"phone_number\": \"1234567890\",\n" +
                    "\t\"move_in_date\": \"20-10-2010\",\n" +
                    "\t\"bedrooms\": \"2 Bed, Pent\",\n" +
                    "\t\"pets\": \"Cat\",\n" +
                    "\t\"comments\": \"TEST - ignore please\"\n" +
                    "}");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
