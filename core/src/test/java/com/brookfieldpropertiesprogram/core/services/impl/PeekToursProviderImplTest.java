package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.services.PeekToursProvider;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.Mockito;

@ExtendWith(AemContextExtension.class)
public class PeekToursProviderImplTest {

    private final String TOURS_EXAMPLE_JSON_1 = "{\"success\":true,\"success_id\":11006,\"message\":\"Listings fetched successfully.\",\"data\":{\"listings\":[{\"Listing_id\":\"6125352a02ce9314c00ac945\",\"address\":\"1100 Webster St, Downtown Oakland\",\"Unit\":\"283\",\"Layout Type\":\"Residence H\",\"Postal Code\":\"94607\",\"Vimeo URL\":\"\",\"Bedrooms\":0,\"Bathrooms\":1,\"Status\":\"Public\",\"Is Model Unit\":true,\"Virtual Tour URL\":\"https://listings.peek.us/viewer?token=6127e6abf970e2547933aaab&display=f\",\"Date Uploaded\":\"08/24/2021\"},{\"Listing_id\":\"61252dfcabd26b0d97f7a323\",\"address\":\"1100 Webster St, Downtown Oakland\",\"Unit\":\"205\",\"Layout Type\":\"Residence H\",\"Postal Code\":\"94607\",\"Vimeo URL\":\"\",\"Bedrooms\":1,\"Bathrooms\":1,\"Status\":\"Public\",\"Is Model Unit\":false,\"Virtual Tour URL\":\"https://listings.peek.us/viewer?token=6127e6abf970e2547933aaac&display=f\",\"Date Uploaded\":\"08/24/2021\"}]}}";

    EndPoint peekMock;
    PeekToursProvider peekToursProvider;


    @BeforeEach
    public void setup(AemContext context) throws Exception {
        peekMock = Mockito.mock(EndPoint.class);
        Map<String, Object> peekMockProps = new HashMap<>();
        peekMockProps.put("getEndPointName", "peekToursProvider");

        context.registerService(EndPoint.class, peekMock, peekMockProps);

        peekToursProvider = new PeekToursProviderImpl();
        context.registerInjectActivateService(peekToursProvider);

    }

    @Test
    public void testGetPeekTours() throws IOException {
        Assertions.assertNotNull(peekMock);
        Assertions.assertNotNull(peekToursProvider);

        Mockito.when(peekMock.callEndPoint(eq(HttpMethodType.GET), any(), any(), any())).thenReturn(TOURS_EXAMPLE_JSON_1);

        JsonArray result = peekToursProvider.getPeekTours(new PeekToursProvider.Credentials("", ""));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals("283", result.get(0).getAsJsonObject().get("unit_number").getAsString());
        Assertions.assertEquals("205", result.get(1).getAsJsonObject().get("unit_number").getAsString());
    }


    @Test
    public void testGetTourByNumber_success() throws IOException {
        Assertions.assertNotNull(peekMock);
        Assertions.assertNotNull(peekToursProvider);

        Mockito.when(peekMock.callEndPoint(eq(HttpMethodType.GET), any(), any(), any())).thenReturn(TOURS_EXAMPLE_JSON_1);

        JsonObject result = peekToursProvider.getTourByNumber(new PeekToursProvider.Credentials("", ""), "283");

        Assertions.assertNotNull(result);

        Assertions.assertEquals("283", result.get("unit_number").getAsString());
        Assertions.assertEquals("https://listings.peek.us/viewer?token=6127e6abf970e2547933aaab&display=f", result.get("url").getAsString());
    }

    @Test
    public void testGetTourByNumber_notFound() throws IOException {
        Assertions.assertNotNull(peekMock);
        Assertions.assertNotNull(peekToursProvider);

        Mockito.when(peekMock.callEndPoint(eq(HttpMethodType.GET), any(), any(), any())).thenReturn(TOURS_EXAMPLE_JSON_1);

        JsonObject result = peekToursProvider.getTourByNumber(new PeekToursProvider.Credentials("", ""), "000");

        Assertions.assertNull(result);
    }

    @Test
    public void testGetTourByFloorplan_success() throws IOException {
        Assertions.assertNotNull(peekMock);
        Assertions.assertNotNull(peekToursProvider);

        Mockito.when(peekMock.callEndPoint(eq(HttpMethodType.GET), any(), any(), any())).thenReturn(TOURS_EXAMPLE_JSON_1);

        JsonObject result = peekToursProvider.getTourByFloorplan(new PeekToursProvider.Credentials("", ""), "Residence H");

        Assertions.assertNotNull(result);

        Assertions.assertEquals("283", result.get("unit_number").getAsString());
        Assertions.assertEquals("https://listings.peek.us/viewer?token=6127e6abf970e2547933aaab&display=f", result.get("url").getAsString());
    }

    @Test
    public void testGetTourByFloorplan_notFound() throws IOException {
        Assertions.assertNotNull(peekMock);
        Assertions.assertNotNull(peekToursProvider);

        Mockito.when(peekMock.callEndPoint(eq(HttpMethodType.GET), any(), any(), any())).thenReturn(TOURS_EXAMPLE_JSON_1);

        JsonObject result = peekToursProvider.getTourByFloorplan(new PeekToursProvider.Credentials("", ""), "000");

        Assertions.assertNull(result);
    }
}
