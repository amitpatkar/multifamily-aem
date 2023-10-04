/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.filters;

import java.io.BufferedReader;
import javax.servlet.ServletInputStream;
import junit.framework.TestCase;

/**
 *
 * @author amitpatkar
 */
public class MultiReadRequestWrapperTest extends TestCase {
    
    public MultiReadRequestWrapperTest(String testName) {
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
     * Test of getInputStream method, of class MultiReadRequestWrapper.
     */
    public void testGetInputStream() throws Exception {
        System.out.println("getInputStream");
        MultiReadRequestWrapper instance = null;
        ServletInputStream expResult = null;
        ServletInputStream result = instance.getInputStream();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReader method, of class MultiReadRequestWrapper.
     */
    public void testGetReader() throws Exception {
        System.out.println("getReader");
        MultiReadRequestWrapper instance = null;
        BufferedReader expResult = null;
        BufferedReader result = instance.getReader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
