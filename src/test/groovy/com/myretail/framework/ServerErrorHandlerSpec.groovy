package com.myretail.framework

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.myretail.framework.exception.NotFound404Exception
import com.myretail.framework.handler.ServerErrorHandler
import spock.lang.Specification

class ServerErrorHandlerSpec extends Specification {


  def "handles exception"() {
    when:
    ServerErrorHandler.ErrorResponse response = ServerErrorHandler.forCondition(throwable)
    then:
    response.status == status
    response.message == expectedMessage
    where:
    throwable                                                       | status | expectedMessage
    new RuntimeException()                                          | 500    | "Internal Server Error"
    new NotFound404Exception()                                      | 404    | "Not Found"
    new UnrecognizedPropertyException(null, null, null, null, null) | 422    | "Invalid JSON body"
  }
}