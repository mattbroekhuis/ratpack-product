package com.myretail.product.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.myretail.framework.data.Cassandra
import com.myretail.product.model.Product
import com.myretail.product.remote.RemoteProductApi
import ratpack.exec.Promise

@Singleton
class ProductService {

  RemoteProductApi remoteProductApi
  Cassandra<Product> cassandra

  @Inject
  ProductService(RemoteProductApi remoteProductApi, Cassandra cassandra) {
    this.remoteProductApi = remoteProductApi
    this.cassandra = cassandra
  }


  Promise<Product> one(String productId) {
    cassandra.get(Product, productId).right(remoteProductApi.productTitle(productId)).map({
      Product p = it.left ?: new Product(productId: productId)
      p.title = it.right
      p
    })
  }

  Promise<Void> put(String productId, Product body) {
    body.productId = productId
    cassandra.save(body)
  }

  Promise<Void> delete(String productId) {
    cassandra.delete(Product, productId)
  }
}
