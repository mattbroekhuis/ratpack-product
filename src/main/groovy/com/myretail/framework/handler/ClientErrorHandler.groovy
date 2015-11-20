package com.myretail.framework.handler

import com.google.inject.Singleton
import ratpack.handling.Context

@Singleton
class ClientErrorHandler implements ratpack.error.ClientErrorHandler {
  @Override
  void error(Context context, int statusCode) throws Exception {
    context.getResponse().status(statusCode).send()
  }
}
