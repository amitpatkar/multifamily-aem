/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services;

import com.google.gson.JsonArray;
import java.io.IOException;

/**
 * @author amitpatkar
 */
public interface PropertyListExternalService {
    JsonArray searchAvailabilityByOneSiteID(String[] oneSiteId) throws IOException;
}
