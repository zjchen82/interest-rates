package com.ufinity.assignments.interestrates;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handle the exceptions globally.
 *
 * Ajax and Non-Ajax request will be differentiated by X_REQUESTED_WITH.
 *
 * Frontend (js) should handle the HttpStatus code first and then the response body.
 *
 */
@ControllerAdvice
public class ControllerExceptionHandler {
  private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);
  public static final String X_REQUESTED_WITH = "X-Requested-With";
  public static final String XMLHTTP_REQUEST = "XMLHttpRequest";

  /**
   * Handle the Entity is not found exception
   */
  @ExceptionHandler({NoHandlerFoundException.class})
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public void handleNotFoundException(HttpServletRequest req, HttpServletResponse httpServletResponse, Exception ex)
      throws IOException {
    String requestedUrl = req.getRequestURI();
    LOG.warn("404 Exception, url={}, ex={}", requestedUrl, ex.getMessage());

    if (XMLHTTP_REQUEST.equalsIgnoreCase(req.getHeader(X_REQUESTED_WITH))) {
      return ;
    }

    httpServletResponse.sendRedirect("/error");
  }

  /**
   * Handle those exceptions which we have not handled
   */
  @ExceptionHandler({Exception.class})
  public String handleGeneralException(HttpServletRequest req, HttpServletResponse httpServletResponse, RedirectAttributes redirectAttributes, Exception ex)
      throws IOException {
    String errorCode = getErrorCode();

    LOG.error("Generic Exception, error code = {}, url={}", errorCode, req.getRequestURL(), ex);

    if (XMLHTTP_REQUEST.equalsIgnoreCase(req.getHeader(X_REQUESTED_WITH))) {
      httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      httpServletResponse.getWriter().write("{\"errors\": [{ \"message\": \"" + errorCode + "\"}]}");

      return null;
    }

    redirectAttributes.addFlashAttribute("code", errorCode);
    return "redirect:/error";
  }

  /**
   * get error code for the global exception handler, this code will be used to trace the log.
   *
   * @return the error code
   */
  public static String getErrorCode() {
    return "API-" + RandomStringUtils.randomNumeric(4);
  }
}