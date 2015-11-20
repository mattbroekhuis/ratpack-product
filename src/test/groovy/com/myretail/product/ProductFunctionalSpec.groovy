package com.myretail.product

import groovy.json.JsonSlurper
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.test.ServerBackedApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.Specification

class ProductFunctionalSpec extends Specification {

  ServerBackedApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest()
  @Delegate
  TestHttpClient client = testHttpClient(aut)

  def "Check Site Index"() {
    when:
    get("/")

    then:
    response.statusCode == 200
    response.body.text == 'Product api. You can do GET and PUT to /product/{id}'
  }

  def "Delete, Read, Create, Delete Product"() {
    when:
    delete("/product/$productId")
    then:
    response.statusCode == 200
    and:
    get("/product/$productId")
    then:
    !new JsonSlurper().parseText(response.body.text)["currentPrice"]
    and:
    requestSpec {
      it.body {
        it.type("application/json").text(json)
      }
    }.put("/product/$productId")
    then:
    response.statusCode == 200
    and:
    get("/product/$productId")
    then:
    response.statusCode == 200
    response.body.text == json

    where:
    productId  | amount | json
    '13860428' | 20.00  | '''{"productId":"13860428","currentPrice":{"amount":3.5,"currencyCode":"CAN"},"title":"The Big Lebowski [Blu-ray]"}'''
  }
}