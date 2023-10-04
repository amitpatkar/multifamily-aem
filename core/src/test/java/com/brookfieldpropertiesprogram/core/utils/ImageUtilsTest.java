/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.utils;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 *
 * @author amitpatkar
 */
@ExtendWith(AemContextExtension.class)
public class ImageUtilsTest  {

    /**
     * Test of escape method, of class ImageUtils.
     */
    @Test
    public void testEscape() {        
        String inStr = "amit patkar is working hard";
        String expResult ="amit-patkar-is-working-hard";
        String result = ImageUtils.escape(inStr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.        
    }

    /**
     * Test of escapeRegEx method, of class ImageUtils.
     */
    @Test
    public void testEscapeRegEx() {        
        String inStr = "amit patkar is working hard this is between ' quotes";
        String expResult = "\\Qamit-patkar-is-working-hard-this-is-between-'-quotes\\E";
        String result = ImageUtils.escapeRegEx(inStr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.        
    }
    
}
