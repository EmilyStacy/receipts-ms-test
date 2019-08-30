package com.aa.fly.receipts.steps;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;

public class ResponseResults {
    private ClientHttpResponse theResponse;
    private final String body;
    private ResponseEntity<String> theResponseEntity;

    public ResponseResults(final ClientHttpResponse response) throws IOException {
        this.theResponse = response;
        final InputStream bodyInputStream = response.getBody();
        if (null == bodyInputStream) {
            this.body = "{}";
        } else {
            final StringWriter stringWriter = new StringWriter();
            IOUtils.copy(bodyInputStream, stringWriter, "UTF-8");
            this.body = stringWriter.toString();
        }
    }

    public ResponseResults(final ResponseEntity<String> response) {
        this.theResponseEntity = response;
        this.body = response.getBody();
    }

    protected ClientHttpResponse getTheResponse() {
        return theResponse;
    }

    protected String getBody() {
        return body;
    }

    protected ResponseEntity<String> getTheResponseEntity() {
        return theResponseEntity;
    }
}
