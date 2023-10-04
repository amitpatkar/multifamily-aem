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
import com.brookfieldpropertiesprogram.core.services.PeekToursProvider;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import com.brookfieldpropertiesprogram.core.services.impl.PeekToursProviderImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import org.apache.http.HttpStatus;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.Mockito;

@ExtendWith(AemContextExtension.class)
class PeekToursServletTest {

    private final String TOURS_EXAMPLE_JSON_1 = "{\"success\":true,\"success_id\":11006,\"message\":\"Listings fetched successfully.\",\"data\":{\"listings\":[{\"Listing_id\":\"6125352a02ce9314c00ac945\",\"address\":\"1100 Webster St, Downtown Oakland\",\"Unit\":\"283\",\"Layout Type\":\"Residence H\",\"Postal Code\":\"94607\",\"Vimeo URL\":\"\",\"Bedrooms\":0,\"Bathrooms\":1,\"Status\":\"Public\",\"Is Model Unit\":true,\"Virtual Tour URL\":\"https://listings.peek.us/viewer?token=6127e6abf970e2547933aaab&display=f\",\"Date Uploaded\":\"08/24/2021\"},{\"Listing_id\":\"61252dfcabd26b0d97f7a323\",\"address\":\"1100 Webster St, Downtown Oakland\",\"Unit\":\"205\",\"Layout Type\":\"Residence H\",\"Postal Code\":\"94607\",\"Vimeo URL\":\"\",\"Bedrooms\":1,\"Bathrooms\":1,\"Status\":\"Public\",\"Is Model Unit\":false,\"Virtual Tour URL\":\"https://listings.peek.us/viewer?token=6127e6abf970e2547933aaac&display=f\",\"Date Uploaded\":\"08/24/2021\"}]}}";

    private final AemContext context = BrookfieldTestContext.newAemContext();

    private final PeekToursServlet fixture = new PeekToursServlet();

    EndPoint peekMock;
    PeekToursProvider peekToursProviderMock;

    @BeforeEach
    public void setup() throws Exception {
        peekMock = Mockito.mock(EndPoint.class);
        Map<String, Object> peekMockProps = new HashMap<>();
        peekMockProps.put("getEndPointName", "peekToursProvider");
        context.registerService(EndPoint.class, peekMock, peekMockProps);

        peekToursProviderMock = new PeekToursProviderImpl();
        context.registerInjectActivateService(peekToursProviderMock);
        
        context.registerInjectActivateService(fixture);
    }

    @Test
    void testDoGetWithoutUnit() throws IOException, ServletException {
        Mockito.when(peekMock.callEndPoint(eq(HttpMethodType.GET), any(), any(), any())).thenReturn(TOURS_EXAMPLE_JSON_1);

        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();
        
        fixture.doGet(request, response);
        Assertions.assertEquals(HttpStatus.SC_OK, response.getStatus());

        JsonArray list = new Gson().fromJson(response.getOutputAsString(), JsonArray.class);
        Assertions.assertEquals(2, list.size());


        Assertions.assertEquals("283", list.get(0).getAsJsonObject().get("unit_number").getAsString());
        Assertions.assertEquals("205", list.get(1).getAsJsonObject().get("unit_number").getAsString());
    }


    @Test
    void testDoGetWithUnit() throws IOException, ServletException {
        Mockito.when(peekMock.callEndPoint(eq(HttpMethodType.GET), any(), any(), any())).thenReturn(TOURS_EXAMPLE_JSON_1);

        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        Map<String, Object> params = new HashMap<>();
        params.put("unit_number", new String[]{"283"});
        request.setParameterMap(params);

        fixture.doGet(request, response);
        Assertions.assertEquals(HttpStatus.SC_OK, response.getStatus());

        JsonObject result = new Gson().fromJson(response.getOutputAsString(), JsonObject.class);

        Assertions.assertEquals("283", result.get("unit_number").getAsString());
        Assertions.assertEquals("https://listings.peek.us/viewer?token=6127e6abf970e2547933aaab&display=f", result.get("url").getAsString());
    }

    @Test
    void testDoGetWithBadUnit() throws IOException, ServletException {
        Mockito.when(peekMock.callEndPoint(eq(HttpMethodType.GET), any(), any(), any())).thenReturn(TOURS_EXAMPLE_JSON_1);

        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        Map<String, Object> params = new HashMap<>();
        params.put("unit_number", new String[]{"000"});
        request.setParameterMap(params);

        fixture.doGet(request, response);
        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
    }


    @Test
    void testDoGetWithFloorplan() throws IOException, ServletException {
        Mockito.when(peekMock.callEndPoint(eq(HttpMethodType.GET), any(), any(), any())).thenReturn(TOURS_EXAMPLE_JSON_1);

        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        Map<String, Object> params = new HashMap<>();
        params.put("floorplan", new String[]{"Residence H"});
        request.setParameterMap(params);

        fixture.doGet(request, response);
        Assertions.assertEquals(HttpStatus.SC_OK, response.getStatus());

        JsonObject result = new Gson().fromJson(response.getOutputAsString(), JsonObject.class);

        Assertions.assertEquals("283", result.get("unit_number").getAsString());
        Assertions.assertEquals("https://listings.peek.us/viewer?token=6127e6abf970e2547933aaab&display=f", result.get("url").getAsString());
    }

    @Test
    void testDoGetWithBadFloorplan() throws IOException, ServletException {
        Mockito.when(peekMock.callEndPoint(eq(HttpMethodType.GET), any(), any(), any())).thenReturn(TOURS_EXAMPLE_JSON_1);

        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        Map<String, Object> params = new HashMap<>();
        params.put("floorplan", new String[]{"000"});
        request.setParameterMap(params);

        fixture.doGet(request, response);
        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatus());
    }

    
}
