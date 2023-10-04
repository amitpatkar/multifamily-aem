/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.services.PropertyListExternalService;
import com.brookfieldpropertiesprogram.core.services.PropertyListExternalServiceImplConfig;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

/**
 * @author amitpatkar
 */
@Component(service = PropertyListExternalService.class,configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = PropertyListExternalServiceImplConfig.class)
public class PropertyListExternalServiceImpl implements PropertyListExternalService {
    private final Gson gson = new Gson();

    @Reference(target = "(getEndPointName=searchAvailabilityByOneSiteID)")
    EndPoint queryGraphql;
    
    PropertyListExternalServiceImplConfig config;
    
    @Activate
    @Modified
    protected void activate(PropertyListExternalServiceImplConfig config) {
        this.config = config;
    }


    @Override
    public JsonArray searchAvailabilityByOneSiteID(String[] oneSiteId) throws IOException {
        SearchAvailabilityByOneSiteIDRequest request = new SearchAvailabilityByOneSiteIDRequest(oneSiteId,this.config.query());
        String response = queryGraphql.callEndPoint(HttpMethodType.POST, gson.toJson(request));

        JsonObject obj = gson.fromJson(response, JsonObject.class);
        return obj.getAsJsonObject("data").getAsJsonArray("SearchAvailabilitiesByOneSiteIds");
    }


    static class SearchAvailabilityByOneSiteIDRequest {

        private final String query;
                /*
                "query SearchAvailabilitiesByOneSiteIds($oneSiteIds: [String]) {\n" +
                "      SearchAvailabilitiesByOneSiteIds(oneSiteIds: $oneSiteIds) {\n" +
                "        ...CoreUnitFields\n" +
                "        unitDetails {\n" +
                "          ...UnitDetailsFields\n" +
                "        }\n" +
                "        availability {\n" +
                "          ...UnitAvailabilityFields\n" +
                "        }\n" +
                "        address {\n" +
                "          ...UnitAddressFields\n" +
                "        }\n" +
                "        floorPlan {\n" +
                "          ...UnitFloorPlanFields\n" +
                "        }\n" +
                "        leaseOptions {\n" +
                "          ...LeaseOptionsFields\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "    \n" +
                "fragment LeaseOptionsFields on LeaseOptions {\n" +
                "  __typename\n" +
                "neededByDate\n" +
                "leaseTerm\n" +
                "rent\n" +
                "best\n" +
                "}\n" +
                "\n" +
                "fragment UnitDetailsFields on UnitDetails {\n" +
                "  __typename\n" +
                "bedrooms\n" +
                "bathrooms\n" +
                "grossSqFtCount\n" +
                "rentSqFtCount\n" +
                "floorNumber\n" +
                "description\n" +
                "noteDescription\n" +
                "}\n" +
                "\n" +
                "fragment UnitAvailabilityFields on UnitAvailability {\n" +
                "  __typename\n" +
                "unavailableCode\n" +
                "madeReadyBit\n" +
                "madeReadyDate\n" +
                "availableDate\n" +
                "availableBit\n" +
                "lastActionCode\n" +
                "lastActionDesc\n" +
                "vacantDate\n" +
                "vacantBit\n" +
                "}\n" +
                "\n" +
                "fragment UnitFloorPlanFields on UnitFloorPlan {\n" +
                "  __typename\n" +
                "floorPlanID\n" +
                "floorPlanCode\n" +
                "floorPlanName\n" +
                "floorPlanGroupName\n" +
                "floorPlanGroupID\n" +
                //"floorPlanImage\n" + //change as suggested by Toka
                "image {\n" +
                "description\n" +
                "file {\n" +
                "fileName\n" +
                "url\n" +
                "}\n" +
                "}\n" +
                "}\n" +
                "\n" +
                "fragment UnitAddressFields on UnitAddress {\n" +
                "  __typename\n" +
                "address1\n" +
                "address2\n" +
                "buildingID\n" +
                "buildingNumber\n" +
                "cityName\n" +
                "countryName\n" +
                "countyName\n" +
                "state\n" +
                "unitID\n" +
                "unitNumber\n" +
                "zip\n" +
                "}\n" +
                "\n" +
                "fragment CoreUnitFields on Unit {\n" +
                "  __typename\n" +
                "city\n" +
                "state\n" +
                "pk\n" +
                "propertyContentfulId\n" +
                "propertyNumberID\n" +
                "baseRentAmount\n" +
                "floorPlanMarketRent\n" +
                "unitMarketRent\n" +
                "nonRevenueFlag\n" +
                "nonRefundFee\n" +
                "depositAmount\n" +
                "oneSiteId\n" +
                "minMoveIn\n" +
                "leaseOptions {\n" +
                "neededByDate\n" +
                "rent\n" +
                "}\n" +
                "}";
*/
        private Variables variables;

        public SearchAvailabilityByOneSiteIDRequest(String[] oneSiteIds,String query) {
            this.variables = new Variables(oneSiteIds);
            this.query = query;
        }

        public String getQuery() {
            return query;
        }

        public Variables getVariables() {
            return variables;
        }

        public void setVariables(Variables variables) {
            this.variables = variables;
        }

        static class Variables {

            private String[] oneSiteIds;

            public Variables(String[] oneSiteIds) {
                this.oneSiteIds = ( (oneSiteIds == null) ? null : oneSiteIds.clone());
            }

            public String[] getOneSiteIds() {
                return  ( (oneSiteIds == null) ? null : oneSiteIds.clone());
            }

            public void setOneSiteIds(String[] oneSiteId) {
                this.oneSiteIds = ( (oneSiteId == null) ? null : oneSiteId.clone());
            }
        }
    }
}
