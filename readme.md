## Instructions
depends on docker. runs in gradle. you can use the wrapper or ratpack needs gradle 2.6 or higher

1. `./restartCassandra.sh`
2. check your docker ip is the same as the one in `/src/main/resources/application.properties`
3. `./gradlew` run or make run config in intellij for  `ratpack.groovy.GroovyRatpackMain`
4. http://localhost:5050/product/13860428
