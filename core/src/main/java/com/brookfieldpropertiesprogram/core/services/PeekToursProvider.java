package com.brookfieldpropertiesprogram.core.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;

public interface PeekToursProvider {
    JsonArray getPeekTours(Credentials creds) throws IOException;
    JsonObject getTourByNumber(Credentials creds, String unitNumber) throws IOException;
    JsonObject getTourByFloorplan(Credentials creds, String floorplanName) throws IOException;

    class Credentials {
        private String key;
        private String agentId;

        public Credentials(String key, String agentId) {
            this.key = key;
            this.agentId = agentId;
        }

        public String getKey() {
            return key;
        }

        public String getAgentId() {
            return agentId;
        }
    }
}
