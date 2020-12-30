package com.aa.fly.receipts.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.WebRequest;


import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class RestResponseEntityExceptionHandlerTest {

    @Spy
    private RestResponseEntityExceptionHandler restResponseEntityExceptionHandler;

    @Test
    public void test2(){
        WebRequest rq = mock(WebRequest.class);

        ResponseEntity<Object> responseEntity = restResponseEntityExceptionHandler.handleRuntimeException(new RuntimeException(),rq);

        Assert.assertNotNull(responseEntity);
        Assert.assertEquals("MS internal error occured",responseEntity.getBody());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

}
