# tokigames-challange

This API is to search over available flights.

There are two available flight’s providers:

----
- include::https://obscure-caverns-79008.herokuapp.com/cheap[]
- include::https://obscure-caverns-79008.herokuapp.com/business[]
----


# What required ?

  * install maven.
  * JDK 12.0.1 installed.


# Build the project

  * in CMD in the root path run the command
  ----
  mvnw clean verify package
  ----
  
  * open `\target` and in CMD run the command
  ----
  java -jar tokigames-challange-1.0.0 --spring.profiles.active=dev
  ----

  NOTE: `--spring.profiles.active=dev` is to let spring boot use the `application-dev.yml` property file.
  
  
  * open swagger for viewing the available end points
  ----
  http://localhost:8090/swagger-ui.html
  ----
  
  * you can run unit test using the command
  ----
  mvn test
  ----