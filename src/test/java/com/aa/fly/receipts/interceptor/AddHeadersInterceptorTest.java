package com.aa.fly.receipts.interceptor;

import static org.junit.Assert.assertTrue;

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
    public void preHandle() throws Exception {
        AddHeadersInterceptor interceptor = new AddHeadersInterceptor();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Transaction-ID", "1234-1234-1234-1234");

        HttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, null);
        assertTrue(response.containsHeader("X-Transaction-ID"));
        assertTrue(request.getHeader("X-Transaction-ID").equals(response.getHeader("X-Transaction-ID")));
    }

}
