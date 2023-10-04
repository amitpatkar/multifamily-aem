package com.brookfieldpropertiesprogram.core.services.impl;


import com.brookfieldpropertiesprogram.core.services.PropertyListExternalService;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import com.google.gson.JsonArray;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;

@ExtendWith(AemContextExtension.class)
public class PropertyListExternalTest {

    private final String UNIT_EXAMPLE_JSON = "{\"data\":{\"SearchAvailabilitiesByOneSiteIds\":[{\n" +
            "        \"pk\":\"3031818:28:unitNumber:208\"\n" +
            "      },{" +
            "        \"pk\":\"3031818:28:unitNumber:209\"\n" +
            "      }]}}";

    EndPoint graphqlMock;

    PropertyListExternalService propertyListExternalServiceMock;

    @BeforeEach
    public void setup(AemContext context) throws Exception {
        graphqlMock = Mockito.mock(EndPoint.class);
        Map<String, Object> propsEndPointGraphql = new HashMap<>();
        propsEndPointGraphql.put("getEndPointName", "searchAvailabilityByOneSiteID");
        context.registerService(EndPoint.class, graphqlMock, propsEndPointGraphql);

        Map<String, Object> propsPropertyListExternalServiceMock = new HashMap<>();
        propsPropertyListExternalServiceMock.put("query", "query SearchAvailabilitiesByOneSiteIds($oneSiteIds: [String]) {       SearchAvailabilitiesByOneSiteIds(oneSiteIds: $oneSiteIds) {         ...CoreUnitFields         unitDetails {           ...UnitDetailsFields         }         availability {           ...UnitAvailabilityFields         }         address {           ...UnitAddressFields         }         floorPlan {           ...UnitFloorPlanFields         }         leaseOptions {           ...LeaseOptionsFields         }       }     }      fragment LeaseOptionsFields on LeaseOptions {   __typename neededByDate leaseTerm rent best }  fragment UnitDetailsFields on UnitDetails {   __typename bedrooms bathrooms grossSqFtCount rentSqFtCount floorNumber description noteDescription }  fragment UnitAvailabilityFields on UnitAvailability {   __typename unavailableCode madeReadyBit madeReadyDate availableDate availableBit lastActionCode lastActionDesc vacantDate vacantBit }  fragment UnitFloorPlanFields on UnitFloorPlan {   __typename floorPlanID floorPlanCode floorPlanName floorPlanGroupName floorPlanGroupID floorPlanImage image { description file { fileName url } } }  fragment UnitAddressFields on UnitAddress {   __typename address1 address2 buildingID buildingNumber cityName countryName countyName state unitID unitNumber zip }  fragment CoreUnitFields on Unit {   __typename city state pk propertyContentfulId propertyNumberID baseRentAmount floorPlanMarketRent unitMarketRent nonRevenueFlag nonRefundFee depositAmount oneSiteId minMoveIn leaseOptions { neededByDate rent } }");
        propertyListExternalServiceMock = new PropertyListExternalServiceImpl();
        context.registerInjectActivateService(propertyListExternalServiceMock,propsPropertyListExternalServiceMock);
    }

    @Test
    public void testSearchAvailabilityByProperty() throws IOException {
        Assertions.assertNotNull(graphqlMock);
        Assertions.assertNotNull(propertyListExternalServiceMock);

        Mockito.when(graphqlMock.callEndPoint(eq(HttpMethodType.POST), anyString())).thenReturn(UNIT_EXAMPLE_JSON);

        JsonArray result = propertyListExternalServiceMock.searchAvailabilityByOneSiteID(new String[]{"test-pk"});

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals("3031818:28:unitNumber:208", result.get(0).getAsJsonObject().get("pk").getAsString());
        Assertions.assertEquals("3031818:28:unitNumber:209", result.get(1).getAsJsonObject().get("pk").getAsString());
    }
}
