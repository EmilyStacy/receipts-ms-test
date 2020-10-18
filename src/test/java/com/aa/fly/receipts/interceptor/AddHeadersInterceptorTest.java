package com.aa.fly.receipts.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 629874 on 5/16/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class AddHeadersInterceptorTest {

    @Test
    public void preHandleAllNumeric() throws Exception {
        AddHeadersInterceptor interceptor = new AddHeadersInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Transaction-ID", "1234-1234-1234-1234");

        HttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, null);
        assertTrue(response.containsHeader("X-Transaction-ID"));
        assertEquals(request.getHeader("X-Transaction-ID"), response.getHeader("X-Transaction-ID"));
    }
    
    @Test
    public void preHandleAlphaNumeric() throws Exception {
        AddHeadersInterceptor interceptor = new AddHeadersInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Transaction-ID", "B93B9D08-0215-4C78-AA94-90EAC8BB0085");

        HttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, null);
        assertTrue(response.containsHeader("X-Transaction-ID"));
        assertEquals(request.getHeader("X-Transaction-ID"), response.getHeader("X-Transaction-ID"));
    }
    
    @Test (expected = IOException.class)
    public void preHandleExpectedIOException() throws Exception {
        AddHeadersInterceptor interceptor = new AddHeadersInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Transaction-ID", "1234@1234-1234-1234");

        HttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, null);
    }
}
