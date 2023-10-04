/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 *
 * @author patkara
 */
@ObjectClassDefinition(
        name = "PropertyListExternalServiceImplConfig",
        description = "PropertyListExternalServiceImplConfig")
public @interface PropertyListExternalServiceImplConfig {

    @AttributeDefinition(
            name = "query",
            description = "query",
            type = AttributeType.STRING) String query() default "";       
}
