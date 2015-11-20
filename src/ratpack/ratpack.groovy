import com.myretail.framework.data.Cassandra
import com.myretail.framework.handler.ClientErrorHandler
import com.myretail.framework.handler.ServerErrorHandler
import com.myretail.product.model.Product
import com.myretail.product.remote.RemoteProductApi
import com.myretail.product.service.ProductService
import ratpack.config.ConfigData

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.fromJson
import static ratpack.jackson.Jackson.json

ratpack {
  serverConfig {
    props("application.properties")
  }
  bindings {
    ConfigData configData = ConfigData.of { c ->
      c.props("$serverConfig.baseDir.file/application.properties")
      c.sysProps()
      c.env()
      c.build()
    }
    bindInstance(ConfigData, configData)
    bind(ProductService)
    bind(RemoteProductApi)
    bind(Cassandra)
    bind(ServerErrorHandler)
    bind(ClientErrorHandler)

  }
  handlers {
    path("") {response.send("Product api. You can do GET and PUT to /product/{id}")}
    path("product/:id") { ProductService ps ->
      byMethod {
        get {
          ps.one(context.getPathTokens().get("id")).then { render(json(it)) }
        }
        delete {
          ps.delete(context.getPathTokens().get("id")).then { response.send() }
        }
        put {
          context.parse(fromJson(Product)).then {
            ps.put(context.getPathTokens().get("id"), it)
                    .then { response.send() }
          }
        }
      }
    }
  }
}

