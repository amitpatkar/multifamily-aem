/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.brookfieldpropertiesprogram.core.models;

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import com.brookfieldpropertiesprogram.core.services.EnvironmentSetting;
import com.brookfieldpropertiesprogram.core.services.impl.EnvironmentSettingImpl;
import io.jsonwebtoken.lang.Assert;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class UpdatedPropRequestModelTest {

    final AemContext context = BrookfieldTestContext.newAemContext();
    UpdatedPropRequestModel toBeTested;

    EnvironmentSetting envSettingMockTrue;

    @BeforeEach
    public void setup() throws Exception {

        Map<String, Object> propsEnvSettingMockTrue = new HashMap<>();
        propsEnvSettingMockTrue.put("isPublishServer", true);
        envSettingMockTrue = new EnvironmentSettingImpl();
        context.registerInjectActivateService(envSettingMockTrue, propsEnvSettingMockTrue);

    }

    @Test
    public void testIsNotNull() throws IOException {
        TestLoggerFactory.getInstance().setPrintLevel(Level.TRACE);
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.HOME_S);
        toBeTested = context.request().adaptTo(UpdatedPropRequestModel.class);
        Assertions.assertNotNull(toBeTested);
    }
    
    @Test
    public void testChangeHero() throws IOException {
        TestLoggerFactory.getInstance().setPrintLevel(Level.TRACE);
        context.currentPage(BrookfieldTestContext.HOME_S);
        context.currentResource(BrookfieldTestContext.CONFIG_LINK_TYPES);
        toBeTested = context.request().adaptTo(UpdatedPropRequestModel.class);
        Assertions.assertNotNull(toBeTested);
    }
}
