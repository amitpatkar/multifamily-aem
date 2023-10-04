package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import com.brookfieldpropertiesprogram.core.services.EnvironmentSetting;
import com.brookfieldpropertiesprogram.core.services.PropertyListExternalService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

@ExtendWith(AemContextExtension.class)
public class EnvironmentSettingTest {

    EnvironmentSetting envSettingMockFalse;
    EnvironmentSetting envSettingMockTrue;

    PropertyListExternalService propertyListExternalServiceMock;
    final AemContext aemContext = BrookfieldTestContext.newAemContext();

    @BeforeEach
    public void setup(AemContext context) throws Exception {

    }

    @Test
    public void testIsPublishFalse() throws IOException {
        Map<String, Object> propsEnvSettingMockFalse = new HashMap<>();
        propsEnvSettingMockFalse.put("isPublishServer", false);
        envSettingMockFalse = new EnvironmentSettingImpl();
        aemContext.registerInjectActivateService(envSettingMockFalse, propsEnvSettingMockFalse);
        Assertions.assertNotNull(envSettingMockFalse);
        Assertions.assertEquals(false, envSettingMockFalse.isPublishServer());
    }

    @Test
    public void testIsPublishTrue() throws IOException {

        Map<String, Object> propsEnvSettingMockTrue = new HashMap<>();
        propsEnvSettingMockTrue.put("isPublishServer", true);
        envSettingMockTrue = new EnvironmentSettingImpl();
        aemContext.registerInjectActivateService(envSettingMockTrue, propsEnvSettingMockTrue);
        Assertions.assertNotNull(envSettingMockTrue);
        Assertions.assertEquals(true, envSettingMockTrue.isPublishServer());
    }
}
