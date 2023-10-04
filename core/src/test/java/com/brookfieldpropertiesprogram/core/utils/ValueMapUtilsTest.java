/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.utils;

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import com.brookfieldpropertiesprogram.core.models.PageInfoRequestModel;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 *
 * @author amitpatkar
 */
@ExtendWith(AemContextExtension.class)
public class ValueMapUtilsTest  {      
    
    private final AemContext context = BrookfieldTestContext.newAemContext();

    /**
     * Test of substitute method, of class ValueMapUtils.
     */
    @Test
    public void testSubstitute() {
         // some very basic junit tests        
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);

        PageInfoRequestModel pageInfoRequestModel = context.request().adaptTo(PageInfoRequestModel.class);
        Assertions.assertNotNull(pageInfoRequestModel);
        
        System.out.println("substitute");
        String inStr = "This will be replaced with ${phoneNumber}";
        Map<String, Object> valueMap = pageInfoRequestModel.getCombinedProps();
        String expResult = "This will be replaced with 8443966697";
        String result = ValueMapUtils.substitute(inStr, valueMap);
        assertEquals(expResult, result);
    }
    
}
