package com.teddycrane.springpractice.components;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teddycrane.springpractice.error.HeaderFormatError;
import com.teddycrane.springpractice.error.HeaderNotFoundException;
import com.teddycrane.springpractice.helper.JwtHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

enum RequestHeaderName
{
    AUTHORIZATION("Authorization"), ID("X-id");

    private final String text;

    RequestHeaderName(final String text)
    {
        this.text = text;
    }

    @Override
    public String toString()
    {
        return this.text;
    }
}

@Component
public class AuthorizationInterceptor implements HandlerInterceptor
{
    private static final Logger logger = LogManager.getLogger(AuthorizationInterceptor.class);
    
    private final JwtHelper validator = new JwtHelper();
    

    private String getHeader(HttpServletRequest request, RequestHeaderName headerName) throws HeaderNotFoundException
    {
        logger.trace("Finding header {}", headerName);

        String result = request.getHeader(headerName.toString());

        if (result == null) throw new HeaderNotFoundException("No header found for the specified header name");

        return result;
    }

    private String getRequesterId(HttpServletRequest request) throws HeaderNotFoundException
    {
        return getHeader(request, RequestHeaderName.ID);
    }

    private String getAuthorizationToken(HttpServletRequest request) throws HeaderNotFoundException, HeaderFormatError
    {
        String[] result = this.getHeader(request, RequestHeaderName.AUTHORIZATION).split(" ");
        
        if(result.length == 2 && result[0].equals("Bearer"))
        {
            return result[1];
        } else
        {
            logger.error("Auth token provided in invalid format");
            throw new HeaderFormatError("Invalid authorization token provided");
        }
    }

    /**
     * Returns a Map<RequestHeaderName, String> of headers mapped to their values, or throws an exception if one header is not found
     * @param request The HttpServletRequest object representing the request
     * @return The map of headers to their values
     * @throws HeaderNotFoundException throws if one or more of the required headers is missing or null
     */
    private Map<RequestHeaderName, String> validateRequiredHeaders(HttpServletRequest request) throws HeaderNotFoundException
    {
        Map<RequestHeaderName, String> result = new HashMap<>();
        try 
        {
            result.put(RequestHeaderName.AUTHORIZATION, this.getAuthorizationToken(request));
            result.put(RequestHeaderName.ID, this.getRequesterId(request));
            return result;
        } catch(HeaderNotFoundException | HeaderFormatError e)
        {
            logger.error(e.getMessage());
            throw new HeaderNotFoundException(e.getMessage());
        }
    }

    private boolean validateAuthToken(String token)
    {
        logger.trace("Validating auth token");
        return this.validator.ensureTokenIsValid(token);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        logger.info("{} request made to {}", requestMethod, requestUri);

        // always pass requests if we're hitting the authentication endpoint
        if (requestUri.equals("/users/login") && requestMethod.equalsIgnoreCase("POST")) return true;

        try 
        {
            // get required headers
            Map<RequestHeaderName, String> headers = this.validateRequiredHeaders(request);
            logger.info("Request made by user {}", headers.get(RequestHeaderName.ID));
            
            // validate auth token
            boolean result = this.validateAuthToken(headers.get(RequestHeaderName.AUTHORIZATION));

            if (!result)
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return false;
            }

            return result;
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
