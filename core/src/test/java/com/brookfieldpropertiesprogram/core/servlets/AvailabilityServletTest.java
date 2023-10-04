/*
 *  Copyright 2018 Adobe Systems Incorporated
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
package com.brookfieldpropertiesprogram.core.servlets;

import com.brookfieldpropertiesprogram.core.context.BrookfieldTestContext;
import com.brookfieldpropertiesprogram.core.services.PropertyListExternalService;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import com.brookfieldpropertiesprogram.core.services.impl.PropertyListExternalServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;

@ExtendWith(AemContextExtension.class)
class AvailabilityServletTest {
    private final String UNIT_EXAMPLE_JSON = "{\"data\":{\"SearchAvailabilitiesByOneSiteIds\":[{\n" +
            "        \"pk\":\"3031818:28:unitNumber:208\"\n" +
            "      },{" +
            "        \"pk\":\"3031818:28:unitNumber:209\"\n" +
            "      }]}}";

    private final AemContext context = BrookfieldTestContext.newAemContext();

    private final AvailabilityServlet fixture = new AvailabilityServlet();

    EndPoint graphqlMock;
    PropertyListExternalService propertyListExternalServiceMock;

    @BeforeEach
    public void setup() throws Exception {
        graphqlMock = Mockito.mock(EndPoint.class);
        Map<String, Object> propsEndPointGraphql = new HashMap<>();
        propsEndPointGraphql.put("getEndPointName", "searchAvailabilityByOneSiteID");
        context.registerService(EndPoint.class, graphqlMock, propsEndPointGraphql);

        propertyListExternalServiceMock = new PropertyListExternalServiceImpl();
        context.registerInjectActivateService(propertyListExternalServiceMock);
        
        context.registerInjectActivateService(fixture);

    }

    @Test
    void testDoGetWithoutPK() throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();
        
        fixture.doGet(request, response);
        Assertions.assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatus());
    }

    @Test
    void testDoGetWithCorrectValue() throws IOException {
        Mockito.when(graphqlMock.callEndPoint(eq(HttpMethodType.POST), anyString())).thenReturn(UNIT_EXAMPLE_JSON);

        MockSlingHttpServletRequest request = context.request();
        Map<String, Object> params = new HashMap<>();
        params.put("oneSiteID", new String[]{"test-pk"});
        request.setParameterMap(params);
        MockSlingHttpServletResponse response = context.response();

        fixture.doGet(request, response);

        Assertions.assertEquals(HttpStatus.SC_OK, response.getStatus());
        Assertions.assertEquals("application/json", response.getContentType());

        JsonArray list = new Gson().fromJson(response.getOutputAsString(), JsonArray.class);
        Assertions.assertEquals(2, list.size());

        Assertions.assertEquals("3031818:28:unitNumber:208", list.get(0).getAsJsonObject().get("pk").getAsString());
        Assertions.assertEquals("3031818:28:unitNumber:209", list.get(1).getAsJsonObject().get("pk").getAsString());
    }

    
}
