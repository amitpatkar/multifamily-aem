/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.services.StringSubstitutor;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amitpatkar
 */
@Component(service = StringSubstitutor.class)
public class StringSubstituorImpl implements StringSubstitutor {

    static final Logger LOG = LoggerFactory.getLogger(StringSubstitutor.class);
    final String pattern = "(\\$\\{)(.*?)(\\})";

    @Override
    public String substitute(String inStr, Map<String, Object> valueMap) {
        Pattern p = Pattern.compile(this.pattern,Pattern.MULTILINE);
        Matcher matcher = p.matcher(inStr);
        String finalStrToReturn = inStr;

        while (matcher.find()) {    
            String group = matcher.group(0);
            //String group2= matcher.group(1);
            String group3= matcher.group(2);
            //String group4= matcher.group(3);
            if (valueMap.containsKey(group3)) {
                finalStrToReturn = finalStrToReturn.replaceAll(Pattern.quote(group),(String)valueMap.get(group3));
                LOG.info("Matcher.groupCount(): {}", matcher.groupCount());
            }
        }
        return finalStrToReturn;
    }

}
