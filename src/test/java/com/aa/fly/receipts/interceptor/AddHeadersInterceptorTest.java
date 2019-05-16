package com.aa.fly.receipts.interceptor;


import com.google.common.collect.FluentIterable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by 629874 on 5/16/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class AddHeadersInterceptorTest
{

   @Test
   public void preHandle() throws Exception
   {
      AddHeadersInterceptor interceptor = new AddHeadersInterceptor();
      MockHttpServletRequest request = new MockHttpServletRequest();
      request.addHeader("X-Transaction-ID", "1234-1234-1234-1234");

      HttpServletResponse response = new MockHttpServletResponse();
      interceptor.preHandle(request, response, null);
      assertTrue(response.containsHeader("X-Transaction-ID"));
      assertTrue(request.getHeader("X-Transaction-ID").equals(response.getHeader("X-Transaction-ID")));
   }

}
