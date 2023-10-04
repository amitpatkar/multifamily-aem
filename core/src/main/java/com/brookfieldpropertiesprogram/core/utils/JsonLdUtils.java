/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.utils;

import com.google.gson.JsonObject;
import java.util.Map;

/**
 *
 * @author amitpatkar
 */
public class JsonLdUtils {

    public static JsonObject getJsonLd(Map<String, Object> in) {

        JsonObject mainObj = new JsonObject();
        buildJsonLd(in, mainObj);
        return mainObj;

    }

    protected static void buildJsonLd(Map<String, Object> in, JsonObject jsonObj) {
        if (in == null || in.isEmpty()) {
            return;
        }

        for (String aKey : in.keySet()) {
            Object val = in.get(aKey);
            if (val == null) {
                continue;
            }
            if (val instanceof Map) {
                JsonObject childObj = new JsonObject();
                buildJsonLd((Map<String, Object>) val, childObj);
                jsonObj.add(aKey, childObj);
            } else if (val instanceof String) {
                jsonObj.addProperty(aKey, (String) val);
            } else if (val instanceof Boolean) {
                jsonObj.addProperty(aKey, (Boolean) val);
            } else if (val instanceof Number) {
                jsonObj.addProperty(aKey, (Number) val);
            }
        }
    }
}
