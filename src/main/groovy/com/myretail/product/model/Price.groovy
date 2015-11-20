package com.myretail.product.model

import com.datastax.driver.mapping.annotations.UDT

@UDT(keyspace = "product", name = "price")
class Price {
  Double amount
  String currencyCode
}
