package com.brookfieldpropertiesprogram.core.services.http.impl;

import com.brookfieldpropertiesprogram.core.services.http.Constants;
import com.brookfieldpropertiesprogram.core.services.http.EndPoint;
import com.brookfieldpropertiesprogram.core.services.http.EndPointFactoryConfig;
import com.brookfieldpropertiesprogram.core.services.http.HttpMethodType;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author prajamah
 */
@Component(service = EndPoint.class, property = {
        "name" + "=" + Constants.COMPONENT_NAME_PREFIX + " EndPointAuthProviderHandlerImpl"
}, immediate = true, configurationPid = "com.brookfieldpropertiesprogram.core.services.http.impl.EndPointImpl")
@Designate(ocd = EndPointFactoryConfig.class, factory = true)
public class EndPointImpl implements EndPoint {

    private EndPointFactoryConfig endPointFactoryConfig;
    static final Logger LOG = LoggerFactory.getLogger(EndPointImpl.class);

    @Activate
    protected void activate(EndPointFactoryConfig config) {
        this.endPointFactoryConfig = config;
    }

    @Override
    public String callEndPoint(final HttpMethodType method, final String stringEntity) throws IOException {        
        StringEntity httpEntity = null;
        if (StringUtils.isNotEmpty(stringEntity)) {
            LOG.debug("StringEntity: {}", stringEntity);
            httpEntity = new StringEntity(stringEntity);            
        }
        return endpointServiceInternal(method,null, httpEntity, null);
    }
    
    @Override
    public String callEndPoint(final HttpMethodType method,List<NameValuePair> queryParams , final StringEntity httpEntity,Map<String,String> headers) throws IOException {
        return endpointServiceInternal(method,queryParams, httpEntity, headers);
    }
    
    
    protected String endpointServiceInternal(final HttpMethodType method, List<NameValuePair> queryParams ,final StringEntity httpEntity, Map<String,String> headers ) throws IOException {
        LOG.debug("EndPointImpl.callEndPoint called for {}", this.endPointFactoryConfig.getEndPointURL());
        String responseBody = null;
        try (CloseableHttpClient httpClient = getCloseableHttpClient()) {
            switch (method) {
                case POST:
                    HttpPost httpPost = new HttpPost(this.endPointFactoryConfig.getEndPointURL());
                    if (this.endPointFactoryConfig.headerAuthorization() != null) {
                        LOG.debug("this.endPointFactoryConfig.headerAuthorization():{}", this.endPointFactoryConfig.headerAuthorization());
                        httpPost.addHeader(HttpHeaders.AUTHORIZATION, this.endPointFactoryConfig.headerAuthorization());
                    }
                    if (this.endPointFactoryConfig.headerContentType() != null) {
                        LOG.debug("this.endPointFactoryConfig.headerContentType():{}", this.endPointFactoryConfig.headerContentType());
                        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, this.endPointFactoryConfig.headerContentType());
                    }               
                    if (queryParams != null && !queryParams.isEmpty()) {
                       try {
                           URI newURI = new URIBuilder(this.endPointFactoryConfig.getEndPointURL()).addParameters(queryParams).build();
                       }catch (URISyntaxException e) {
                           throw new IOException(e);
                       }
                    }
                    if (httpEntity != null) httpPost.setEntity(httpEntity);                    
                    try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
                        LOG.info("POST Response Status:: {}", httpResponse.getStatusLine().getStatusCode());
                        responseBody = String.join("", IOUtils.readLines(httpResponse.getEntity().getContent(), "UTF-8"));
                        LOG.debug("Response Body:: {}", responseBody);
                    }
                    break;    
                case GET:
                    HttpGet httpGet = new HttpGet(this.endPointFactoryConfig.getEndPointURL());
                    if (this.endPointFactoryConfig.headerAuthorization() != null) {
                        LOG.debug("this.endPointFactoryConfig.headerAuthorization():{}", this.endPointFactoryConfig.headerAuthorization());
                        httpGet.addHeader(HttpHeaders.AUTHORIZATION, this.endPointFactoryConfig.headerAuthorization());
                    }
                    if (this.endPointFactoryConfig.headerContentType() != null) {
                        LOG.debug("this.endPointFactoryConfig.headerContentType():{}", this.endPointFactoryConfig.headerContentType());
                        httpGet.addHeader(HttpHeaders.CONTENT_TYPE, this.endPointFactoryConfig.headerContentType());
                    }               
                    if (queryParams != null && !queryParams.isEmpty()) {
                       try {
                           URI newURI = new URIBuilder(this.endPointFactoryConfig.getEndPointURL()).addParameters(queryParams).build();
                           httpGet.setURI(newURI);
                           LOG.info("Setting URI for GET Call:{}",newURI);
                       }catch (URISyntaxException e) {
                           throw new IOException(e);
                       }
                    }
                    //if (httpEntity != null) httpGet.setEntity(httpEntity);                    
                    try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                        LOG.info("POST Response Status:: {}", httpResponse.getStatusLine().getStatusCode());
                        responseBody = String.join("", IOUtils.readLines(httpResponse.getEntity().getContent(), "UTF-8"));
                        LOG.info("Response Body:: {}", responseBody);
                    }
                    break;    
                default:
                    LOG.warn("Method not supported");
            }
        }
        return responseBody;
    }
    
    protected CloseableHttpClient getCloseableHttpClient() {
        CloseableHttpClient httpClient = null;
        try {
            int timeout = 25;
            RequestConfig config = RequestConfig.custom()
              .setConnectTimeout(timeout * 1000)
              .setConnectionRequestTimeout(timeout * 1000)
              .setSocketTimeout(timeout * 1000).build();
            httpClient = HttpClients.custom().setDefaultRequestConfig(config).
                    setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).
                    setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, (X509Certificate[] arg0, String arg1) -> true).build()).build();
        } catch (KeyManagementException e) {
            LOG.error("KeyManagementException in creating http client instance", e);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("NoSuchAlgorithmException in creating http client instance", e);
        } catch (KeyStoreException e) {
            LOG.error("KeyStoreException in creating http client instance", e);
        }
        return httpClient;
    }
}
