/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.dto;

import junit.framework.TestCase;
import org.apache.sling.api.resource.Resource;

/**
 *
 * @author amitpatkar
 */
public class RedirectRuleResultTest extends TestCase {
    
    public RedirectRuleResultTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getInputUrl method, of class RedirectRuleResult.
     */
    public void testGetInputUrl() {
        System.out.println("getInputUrl");
        RedirectRuleResult instance = null;
        String expResult = "";
        String result = instance.getInputUrl();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRedirectType method, of class RedirectRuleResult.
     */
    public void testGetRedirectType() {
        System.out.println("getRedirectType");
        RedirectRuleResult instance = null;
        String expResult = "";
        String result = instance.getRedirectType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getResourceToRedirectTo method, of class RedirectRuleResult.
     */
    public void testGetResourceToRedirectTo() {
        System.out.println("getResourceToRedirectTo");
        RedirectRuleResult instance = null;
        //Resource expResult = null;
        //Resource result = instance.getResourcePathToRedirectTo();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
