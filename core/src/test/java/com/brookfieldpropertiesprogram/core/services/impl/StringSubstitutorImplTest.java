package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
public class StringSubstitutorImplTest {

    private final AemContext context = BrookfieldTestContext.newAemContext();
    
    final StringSubstituorImpl mock = new StringSubstituorImpl();

    @BeforeEach
    public void setup(AemContext context) throws Exception {
        Map<String, Object> props = new HashMap<>();
        props.put("getFromEmailAddress", "no-reply@rent.brookfieldproperties.com");
        props.put("getFromEmailAddressName", "Brookfield Properties");
        context.registerInjectActivateService(mock,props);
        
       
        

    }

    @Test
    public void testParseJSONAndSendEmail() {
        Map<String, Object> replaceProps = new HashMap<>();
        replaceProps.put("getFromEmailAddress", "no-reply@rent.brookfieldproperties.com");
        replaceProps.put("token2", "Brookfield Properties");
        replaceProps.put("name", "Amit Patkar");
        String expectedStr = mock.substitute("this is good ${name} ${token2} ${name}", replaceProps);
        Assert.assertEquals("this is good Amit Patkar Brookfield Properties Amit Patkar", expectedStr);
    }

}
