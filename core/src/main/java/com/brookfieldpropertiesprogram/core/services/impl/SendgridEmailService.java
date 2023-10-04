/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.services.EmailService;
import com.brookfieldpropertiesprogram.core.services.http.Constants;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import com.brookfieldpropertiesprogram.core.services.impl.SendgridEmailService.SendgridEmailServiceConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amitpatkar
 */
@Component(service = EmailService.class)
@Designate(ocd = SendgridEmailServiceConfig.class)
public class SendgridEmailService implements EmailService{
    final static Logger LOG = LoggerFactory.getLogger(SendgridEmailService.class);
    

    @Reference(target = "(getEndPointName=sendEmail)")
    EndPoint sendEmail;
    
    SendgridEmailServiceConfig config;
    
    @ObjectClassDefinition(name = Constants.COMPONENT_NAME_PREFIX +  "SendgridEmailServiceConfig", description = "Configuration")
    @interface SendgridEmailServiceConfig {

        @AttributeDefinition(name = "From Email Address", description = "From Email Address", defaultValue = "no-reply@rent.brookfieldproperties.com")
        String getFromEmailAddress();       
        
        @AttributeDefinition(name = "From Email Address Name", description = "From Email Address", defaultValue = "Brookfield Properties")
        String getFromEmailAddressName();       

    }
    @Activate
    protected void activate(SendgridEmailServiceConfig config) {
        this.config = config;
    }

    @Override
    public void sendPlainText(String to,String[] cc,String subject, String emailData) throws IOException{
        
        JsonObject mainObj = new JsonObject();
        
        JsonArray personalizations = new JsonArray();
        JsonObject personalizations_sender_wrapper = new JsonObject();
        personalizations.add(personalizations_sender_wrapper);
        //TO
        JsonArray personalizations_to_arr = new JsonArray();
        JsonObject personalizations_to_obj = new JsonObject();
        personalizations_to_obj.addProperty("email", to);
        personalizations_to_obj.addProperty("name", to);       
        personalizations_to_arr.add(personalizations_to_obj);
        personalizations_sender_wrapper.add("to", personalizations_to_arr);
        //CC
        if (cc != null && cc.length >0 ){
            JsonArray personalizations_cc_arr = new JsonArray();        
            for (String aCC:cc) {
                JsonObject personalizations_cc_obj = new JsonObject();
                personalizations_cc_obj.addProperty("email", aCC);
                personalizations_cc_obj.addProperty("name", aCC); 
                personalizations_cc_arr.add(personalizations_cc_obj);
            }
            personalizations_sender_wrapper.add("cc",personalizations_cc_arr);
        }
        mainObj.add("personalizations", personalizations);
        //FROM                
        JsonObject fromObj = new JsonObject();
        fromObj.addProperty("email", config.getFromEmailAddress() );
        fromObj.addProperty("name", config.getFromEmailAddressName());
        mainObj.add("from", fromObj);
        //SUBJECT
        mainObj.addProperty("subject",subject);
        
        //Content
        JsonArray contentArr = new JsonArray();
        JsonObject contentTypePlain = new JsonObject();
        contentTypePlain.addProperty("type", "text/html");
        contentTypePlain.addProperty("value", emailData);
        contentArr.add(contentTypePlain);
        mainObj.add("content", contentArr);
        
        //lets send the data as is
        String finalData = mainObj.toString();
        
        sendEmail.callEndPoint(HttpMethodType.POST, finalData);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
    
    
}
