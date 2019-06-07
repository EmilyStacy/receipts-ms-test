package com.aa.fly.receipts;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;

@SpringBootTest( classes = ReceiptsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@ActiveProfiles("integration-tests")
@ContextConfiguration
public class SpringIntegrationTest {
    protected static ResponseResults latestResponse = null;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected void executeGet(String url) throws IOException {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Content-Type", "application/json");
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.getRestTemplate().setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.GET, requestCallback, new ResponseExtractor<ResponseResults>() {
            @Override
            public ResponseResults extractData(ClientHttpResponse response) throws IOException {
                if (errorHandler.hadError) {
                    return (errorHandler.getResults());
                } else {
                    return (new ResponseResults(response));
                }
            }
        });

    }

    protected void executePost(String url, Object request) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Client-ID", "Receipts-BFF");
        httpHeaders.set("X-Transaction-ID", String.valueOf(UUID.randomUUID()));

        HttpEntity<Object> httpEntity = new HttpEntity<Object>(request, httpHeaders);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.getRestTemplate().setErrorHandler(errorHandler);
        ResponseEntity<String> result = restTemplate.postForEntity(url, httpEntity, String.class);
        latestResponse = new ResponseResults(result);

    }

    private class ResponseResultErrorHandler implements ResponseErrorHandler {
        private ResponseResults results = null;
        private Boolean hadError = false;

        private ResponseResults getResults() {
            return results;
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            hadError = response.getRawStatusCode() >= 400;
            return hadError;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            results = new ResponseResults(response);
        }
    }
}
