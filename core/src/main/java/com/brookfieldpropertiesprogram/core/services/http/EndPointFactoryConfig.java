/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.http;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 *
 * @author amitpatkar
 */
@ObjectClassDefinition(name = Constants.COMPONENT_NAME_PREFIX +  " EndPointFactoryConfig", description = "Configuration")
public @interface EndPointFactoryConfig {

    @AttributeDefinition(name = "EP Name", description = "EndPoint Name")
    String getEndPointName();

    @AttributeDefinition(name = "EP URL", description = "EndPoint URL")
    String getEndPointURL();

    @AttributeDefinition(name = "EP SOAP Action URL", description = "SOAP Action EndPoint URL")
    String getSoapActionURL();

    @AttributeDefinition(name = "Service Type", description = "Service Type (REST/SOAP)")
    String getServiceType();

    @AttributeDefinition(name = "EP AUTH PROVIDER", description = "EndPoint Auth provider")
    String getEndPointAuthProvider();

    @AttributeDefinition(name = "Is SO TimeOut Required", description = "Is SO TimeOut Required")
    boolean isSoTimeOutRequired();

    @AttributeDefinition(name = "SO TimeOut in ms", description = "SO TimeOut in ms")
    int getSoTimeOut() default 20;

    @AttributeDefinition(name = "Black list keys of response", description = "provide array of black list keys to be excluded from response")
    String[] getBlackListedKeysOfResponse() default {"AdditionalInfo"};

    String webconsole_configurationFactory_nameHint() default "Name : {getEndPointName}";
    
    @AttributeDefinition(name = "Authorization Header", description = "Authorization Header")
    String headerAuthorization();
    
    @AttributeDefinition(name = "Content Type Header", description = "Content Type Header")
    String headerContentType();

}
