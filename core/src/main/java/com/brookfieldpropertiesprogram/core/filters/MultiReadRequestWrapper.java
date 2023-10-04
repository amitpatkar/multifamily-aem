/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.filters;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import org.apache.poi.util.BoundedInputStream;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author amitpatkar
 */
public class MultiReadRequestWrapper extends SlingHttpServletRequestWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(MultiReadRequestWrapper.class);
// We include a max byte size to protect against malicious requests, 
//since this all has to be read into memory
    public static final Integer MAX_BYTE_SIZE = 1_048_576; // 1 MB 
    private final StringBuilder body;

    public MultiReadRequestWrapper(SlingHttpServletRequest request) throws IOException {
        super(request);
        body = new StringBuilder("");
        try (
                InputStream bounded = new BoundedInputStream(request.getInputStream(), MAX_BYTE_SIZE);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bounded))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                body.append(line);
            }
        } catch (IOException e) {
            LOG.error(null,e);
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.toString().getBytes());
        return new ServletInputStream() {
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                //do nothing
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
