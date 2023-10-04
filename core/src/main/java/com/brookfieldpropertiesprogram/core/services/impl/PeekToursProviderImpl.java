package com.brookfieldpropertiesprogram.core.services.impl;

import com.brookfieldpropertiesprogram.core.services.PeekToursProvider;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = PeekToursProvider.class)
public class PeekToursProviderImpl implements PeekToursProvider {

    private final Gson gson = new Gson();

    @Reference(target = "(getEndPointName=peekToursProvider)")
    EndPoint peekToursProvider;

    @Override
    public JsonArray getPeekTours(Credentials creds) throws IOException {
        List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(new BasicNameValuePair("key", creds.getKey()));
        queryParams.add(new BasicNameValuePair("agent_id", creds.getAgentId()));
        queryParams.add(new BasicNameValuePair("page", "all"));

        String responseStr = peekToursProvider.callEndPoint(HttpMethodType.GET, queryParams, null, null);
        JsonObject response = gson.fromJson(responseStr, JsonObject.class);
        JsonObject data = response.get("data").getAsJsonObject();
        JsonArray listings = data.has("listings") ? data.get("listings").getAsJsonArray() : (data.has("result") ? data.get("result").getAsJsonArray() : null); //change by AP for null pointer exception
        return listings == null ? null : transformArray(listings);
    }

    @Override
    public JsonObject getTourByNumber(Credentials creds, String unitNumber) throws IOException {
        JsonArray all = this.getPeekTours(creds);
        if (all == null) return null;
        for (int i=0; i<all.size(); i++) {
            JsonObject tour = all.get(i).getAsJsonObject();
            if (StringUtils.equals(tour.get("unit_number").getAsString(), unitNumber)) {
                return tour;
            }
        }
        return null;
    }

    @Override
    public JsonObject getTourByFloorplan(Credentials creds, String floorplanName) throws IOException {
        JsonArray all = this.getPeekTours(creds);
        if (all == null) return null;
        JsonObject result = null;
        for (int i=0; i<all.size(); i++) {
            JsonObject tour = all.get(i).getAsJsonObject();
            if (StringUtils.equals(tour.get("layout_type").getAsString(), floorplanName)) {
                if (tour.has("is_model_unit") && tour.get("is_model_unit").getAsBoolean()) {
                    // if is_model_unit then we can return immediately
                    return tour;
                }
                result = tour;
            }
        }
        return result;
    }

    private JsonObject transformTour(JsonObject tour) {
        JsonObject transformed = new JsonObject();
        transformed.addProperty("unit_number", tour.get("Unit").getAsString());
        transformed.addProperty("status", tour.get("Status").getAsString());
        transformed.addProperty("url", tour.get("Virtual Tour URL").getAsString());
        if (tour.has("Is Model Unit")) {
            transformed.addProperty("is_model_unit", tour.get("Is Model Unit").getAsBoolean());
        }
        if (tour.has("Layout Type")) {
            transformed.addProperty("layout_type", tour.get("Layout Type").getAsString());
        }
        return transformed;
    }

    private JsonArray transformArray(JsonArray arr) {
        JsonArray result = new JsonArray();
        for (int i=0; i<arr.size(); i++) {
            result.add(transformTour(arr.get(i).getAsJsonObject()));
        }
        return result;
    }
}
