package com.myretail.framework.handler

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.google.inject.Singleton
import com.myretail.framework.exception.NotFound404Exception
import io.netty.handler.codec.http.HttpResponseStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ratpack.handling.Context

import static ratpack.jackson.Jackson.json

@Singleton
class ServerErrorHandler implements ratpack.error.ServerErrorHandler {
  static Logger logger = LoggerFactory.getLogger(ServerErrorHandler)

  public static class ErrorResponse {
    int status
    String message
  }

  @Override
  void error(Context context, Throwable throwable) throws Exception {
    ErrorResponse response = forCondition(throwable)
    context.response.status(response.status)
    context.render(json(response))
  }

  static ErrorResponse forCondition(Throwable throwable) {
    switch (throwable.class) {
      case NotFound404Exception:
        new ErrorResponse(status: HttpResponseStatus.NOT_FOUND.code(), message: "Not Found")
        break
      case UnrecognizedPropertyException:
        new ErrorResponse(status: HttpResponseStatus.UNPROCESSABLE_ENTITY.code(), message: "Invalid JSON body")
        break
      default:
        logger.error("Internal Server Error", throwable)
        new ErrorResponse(status: HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), message: "Internal Server Error")
    }
  }
}