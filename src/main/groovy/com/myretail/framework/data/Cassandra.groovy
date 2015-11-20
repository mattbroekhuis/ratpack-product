package com.myretail.framework.data

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.inject.Inject
import com.google.inject.Singleton
import ratpack.config.ConfigData
import ratpack.exec.Promise

@Singleton
class Cassandra<T> {
  Session session
  MappingManager mappingManager

  @Inject
  public Cassandra(ConfigData configData) {
    println "building cassandra"
    String cassandraAddress = configData.get("/CASSANDRA_PORT_9042_TCP_ADDR", String)
    int cassandraPort = configData.get("/CASSANDRA_PORT_9042_TCP_PORT", Integer)
    String keyspace = configData.get("/CASSANDRA_KEYSPACE", String)
    Cluster cluster = Cluster.builder().addContactPoint(cassandraAddress).withPort(cassandraPort).build()
    session = cluster.connect(keyspace)
    mappingManager = new MappingManager(session)
  }
  //this can probably be combined and shortened groovy style
  Promise<T> get(Class<T> type, Object... id) {
    Mapper<T> mapper = mappingManager.mapper(type)
    Promise.of({ f ->
      Futures.addCallback(mapper.getAsync(id), new FutureCallback<T>() {
        @Override
        void onSuccess(T result) {
          f.success(result)
        }

        @Override
        void onFailure(Throwable t) {
          f.error(t)
        }
      })
    })
  }

  Promise<Void> save(T object) {
    Mapper<T> mapper = mappingManager.mapper(object.getClass()) as Mapper<T>
    Promise.of({ f ->
      Futures.addCallback(mapper.saveAsync(object), new FutureCallback<Void>() {
        @Override
        void onSuccess(Void result) {
          f.success(result)
        }

        @Override
        void onFailure(Throwable t) {
          f.error(t)
        }
      })
    })
  }

  Promise<Void> delete(Class<T> type, Object... id) {

    Mapper<T> mapper = mappingManager.mapper(type) as Mapper<T>
    Promise.of({ f ->
      Futures.addCallback(mapper.deleteAsync(id), new FutureCallback<Void>() {
        @Override
        void onSuccess(Void result) {
          f.success(result)
        }

        @Override
        void onFailure(Throwable t) {
          f.error(t)
        }
      })
    })
  }
}
