/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.dto;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 *
 * @author amitpatkar
 */
@ExtendWith(AemContextExtension.class)
public class UnitImagesRequestTest {     
    UnitImagesRequest instance;
    @BeforeEach
    public void setup() throws Exception {
        instance = new UnitImagesRequest("the-eugene","The Eugene","unitNumber2");
        //context.request().setResource(context.currentResource());
        //context.currentPage("/content/experience-fragments/siteconfig/sites/standalone/baseline");
        //System.out.println(context.currentPage().getName());

    }
    /**
     * Test of getPropertyId method, of class UnitImagesRequest.
     */
    @Test
    public void testGetPropertyId() {        
        String expResult = "the-eugene";
        String result = instance.getPropertyId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getPropertyName method, of class UnitImagesRequest.
     */
    @Test
    public void testGetPropertyName() {        
        String expResult = "The Eugene";
        String result = instance.getPropertyName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUnit method, of class UnitImagesRequest.
     */
    public void testGetUnit() {
        String expResult = "unitNumber2";
        String result = instance.getUnit();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSearchBasePath method, of class UnitImagesRequest.
     */
    public void testGetSearchBasePath() {
        String expResult = "/content/dam/common/properties/the-eugene";
        String result = instance.getSearchBasePath();
        assertEquals(expResult, result);        
    }

    /**
     * Test of getGalleryImagePaths method, of class UnitImagesRequest.
     */
    public void testGetGalleryImagePaths() {
        String[] expResult = new String[]{"gallery\\-" + Pattern.quote(instance.getUnit()) + "\\-(.*?)\\.jpg"};
        String[] result = instance.getGalleryImagePaths();
        assertArrayEquals(expResult, result);        
    }

    /**
     * Test of getHeroImagePaths method, of class UnitImagesRequest.
     */
    public void testGetHeroImagePaths() {
        String[] expResult = new String[]{"hero\\-" + Pattern.quote(instance.getUnit()) + "\\-(.*?)\\.jpg"};
        String[] result = instance.getHeroImagePaths();
        assertArrayEquals(expResult, result);        
    }
    
}
