package com.teddycrane.springpractice.components;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teddycrane.springpractice.error.HeaderNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor
{
    private static final Logger logger = LogManager.getLogger(AuthorizationInterceptor.class);

    private String getRequesterId(HttpServletRequest request) throws HeaderNotFoundException
    {
        logger.trace("Pulling requester id from request");
        String result = request.getHeader("X-id");

        if (result == null) 
        {
            logger.error("No user id provided");
            throw new HeaderNotFoundException("No user id provided");
        } else
        {
            return result;
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        logger.trace("preHandle called");

        // always pass requests if we're hitting the authentication endpoint
        if (request.getRequestURI().equals("/users/login")) return true;

        try {
            String requesterId = this.getRequesterId(request);
            return true;
        } catch(HeaderNotFoundException e)
        {
            logger.trace("Throwing custom bad request error");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unable to authenticate with the provided access token and id");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, 
                            HttpServletResponse response, 
                            Object handler, 
                            ModelAndView modelAndView) throws Exception
    {
        logger.trace("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) 
      throws Exception 
    {
        logger.trace("afterCompletion");
    }


}
