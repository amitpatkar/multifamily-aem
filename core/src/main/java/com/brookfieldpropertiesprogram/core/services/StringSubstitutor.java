/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services;

import java.util.Map;

/**
 *
 * @author amitpatkar
 */
public interface StringSubstitutor {
    
    
    String substitute (String inStr,Map<String,Object> valuemap);
}
