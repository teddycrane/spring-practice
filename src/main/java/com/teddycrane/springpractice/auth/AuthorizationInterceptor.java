package com.teddycrane.springpractice.auth;

import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.HeaderFormatError;
import com.teddycrane.springpractice.error.HeaderNotFoundException;
import com.teddycrane.springpractice.error.UserNotFoundError;
import com.teddycrane.springpractice.helper.IJwtHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

enum RequestHeaderName {
  AUTHORIZATION("Authorization"),
  ID("X-id");

  private final String text;

  RequestHeaderName(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return this.text;
  }
}

@Component
@Order(1)
public class AuthorizationInterceptor implements HandlerInterceptor {
  private static final Logger logger = LogManager.getLogger(AuthorizationInterceptor.class);

  private static final String REQUEST_START = "--------- REQUEST START ---------";
  private static final String REQUEST_END = "--------- REQUEST END ---------";

  private final IJwtHelper validator;

  @Autowired private IAuthService authService;

  public AuthorizationInterceptor(IJwtHelper jwtHelper) {
    this.validator = jwtHelper;
  }

  private String getHeader(HttpServletRequest request, RequestHeaderName headerName)
      throws HeaderNotFoundException {
    logger.trace("Finding header {}", headerName);

    String result = request.getHeader(headerName.toString());

    if (result == null)
      throw new HeaderNotFoundException("No header found for the specified header name");

    return result;
  }

  private String getAuthorizationToken(HttpServletRequest request)
      throws HeaderNotFoundException, HeaderFormatError {
    String[] result = this.getHeader(request, RequestHeaderName.AUTHORIZATION).split(" ");

    if (result.length == 2 && result[0].equals("Bearer")) {
      return result[1];
    } else {
      logger.error("Auth token provided in invalid format");
      throw new HeaderFormatError("Invalid authorization token provided");
    }
  }

  /**
   * Returns a Map<RequestHeaderName, String> of headers mapped to their values, or throws an
   * exception if one header is not found
   *
   * @param request The HttpServletRequest object representing the request
   * @return The map of headers to their values
   * @throws HeaderNotFoundException throws if one or more of the required headers is missing or
   *     null
   */
  private Map<RequestHeaderName, String> validateRequiredHeaders(HttpServletRequest request)
      throws HeaderNotFoundException {
    Map<RequestHeaderName, String> result = new HashMap<>();
    try {
      result.put(RequestHeaderName.AUTHORIZATION, this.getAuthorizationToken(request));
      return result;
    } catch (HeaderNotFoundException | HeaderFormatError e) {
      logger.error(e.getMessage());
      throw new HeaderNotFoundException(e.getMessage());
    }
  }

  private boolean validateAuthToken(String token) {
    logger.trace("Validating auth token");
    return this.validator.ensureTokenIsValid(token);
  }

  private HttpServletResponse generateResponse(
      HttpServletResponse response, int status, String body) {
    response.setStatus(status);

    try {
      // set body message
      PrintWriter bodyWriter = response.getWriter();
      bodyWriter.println(body);
      // commit the response
      bodyWriter.flush();
    } catch (IOException e) {
      logger.error("Fatal error. Unable to write to response body");
    }

    return response;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    logger.trace(REQUEST_START);
    String requestUri = request.getRequestURI();
    String requestMethod = request.getMethod();
    logger.info("{} request made to {}", requestMethod, requestUri);

    try {
      // get required headers
      Map<RequestHeaderName, String> headers = this.validateRequiredHeaders(request);

      // validate auth token
      boolean authResult = this.validateAuthToken(headers.get(RequestHeaderName.AUTHORIZATION));

      if (!authResult) {
        response =
            generateResponse(
                response, HttpServletResponse.SC_UNAUTHORIZED, "The provided token is not valid");
        logger.trace(REQUEST_END);
        return false;
      }

      // check resource permissions
      String userId = this.validator.getIdFromToken(headers.get(RequestHeaderName.AUTHORIZATION));
      boolean allowed =
          this.authService.isResourceAllowedForUser(userId, requestUri, requestMethod);

      if (!allowed) {
        response =
            generateResponse(
                response, HttpServletResponse.SC_FORBIDDEN, "Insufficient permissions.");
        logger.trace(REQUEST_END);
        return false;
      }

      request.setAttribute("X-Requester", userId);
      return true;
    } catch (HeaderNotFoundException e) {
      logger.trace("Throwing custom bad request error");
      response =
          generateResponse(
              response,
              HttpServletResponse.SC_UNAUTHORIZED,
              "One or more of the required headers was not provided");
      return false;
    } catch (UserNotFoundError | BadRequestException e) {
      response =
          generateResponse(
              response, HttpServletResponse.SC_BAD_REQUEST, "Unable to process request headers.");
      return false;
    }
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {}

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    logger.trace(REQUEST_END);
  }
}
