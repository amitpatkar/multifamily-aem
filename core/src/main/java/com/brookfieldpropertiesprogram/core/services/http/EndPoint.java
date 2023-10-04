package com.brookfieldpropertiesprogram.core.services.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;

public interface EndPoint {
    String callEndPoint(final HttpMethodType method,final String stringEntity ) throws IOException;
    
   String callEndPoint(final HttpMethodType method, List<NameValuePair> queryParams, final StringEntity httpEntity, Map<String, String> headers) throws IOException;

}
