/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.utils;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 *
 * @author amitpatkar
 */
@ExtendWith(AemContextExtension.class)
public class CommonConstantsTest {
    
    
    @Test
    public void testConstants() {
        Assertions.assertEquals("brookfieldpropertiesprogram/components/page",CommonConstants.COMPONENTS_PAGE);
        Assertions.assertEquals("sling/servlet/default",CommonConstants.SLING_SERVLET_DEFAULT);
    }

}
