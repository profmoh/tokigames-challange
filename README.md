# tokigames-challange!

This API is to **search over available flights**.

There are two available flight’s providers:

    https://obscure-caverns-79008.herokuapp.com/cheap
    
    https://obscure-caverns-79008.herokuapp.com/cheap

# What required ?

 - install maven.
 - install JDK 12.0.1.

## Build the project

 -  Go to the project root path. Run the following command in CMD, to build the spring boot project. this will export an executable Jar file.

	`mvnw clean verify package`

 - Open `\target` and in CMD, and run the following command.

	`java -jar tokigames-challange-1.0.0 --spring.profiles.active=dev`

| NOTE: | `--spring.profiles.active=dev` is to let spring boot use the `application-dev.yml` property file. |
|--|--|

 - Run unit testing using the following command.

	`mvn test`

 - Open swagger ui page in browser. Swagger is a documentation for available APIs.

	`http://localhost:8090/swagger-ui.html`
