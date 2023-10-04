/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services;

import com.brookfieldpropertiesprogram.core.models.GlobalConfigRequestModel;
import com.brookfieldpropertiesprogram.core.models.PropertyConfigModel;
import java.io.IOException;
import org.apache.sling.api.resource.LoginException;

/**
 *
 * @author amitpatkar
 */
public interface ContactUsService {
    void parseJSONAndSendEmail(GlobalConfigRequestModel globalConfigRequestModel,PropertyConfigModel propertyConfigModel, String jsonData) throws IOException,LoginException;    
}