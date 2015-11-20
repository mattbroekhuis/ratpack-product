package com.myretail.product.model

import com.datastax.driver.mapping.annotations.Frozen
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import com.datastax.driver.mapping.annotations.Transient

@Table(keyspace = "product", name = "product")
class Product {
  @PartitionKey
  String productId

  @Frozen
  Price currentPrice

  @Transient
  String title

}
