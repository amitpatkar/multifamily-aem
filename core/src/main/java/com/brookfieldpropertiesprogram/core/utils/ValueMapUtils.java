/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author amitpatkar
 */
public class ValueMapUtils {

    final static String substitute_pattern = "(\\$\\{)(.*?)(\\})";

    public static String substitute(String inStr, Map<String, Object> valueMap) {
        Pattern p = Pattern.compile(substitute_pattern, Pattern.MULTILINE);
        Matcher matcher = p.matcher(inStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            //String group2= matcher.group(1);
            String group3 = matcher.group(2);
            //String group4= matcher.group(3);
            if (valueMap.containsKey(group3)) {
                matcher.appendReplacement(sb, (String) valueMap.get(group3));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
