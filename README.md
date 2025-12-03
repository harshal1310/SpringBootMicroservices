# SpringBootMicroservices

This repository is a small Spring Boot microservices demo composed of multiple Maven modules:
- Service Registry (Eureka): service discovery implemented by [`com.springProject.EurekaServer`](ServiceRegistry/src/main/java/com/springProject/EurekaServer.java)
- Inventory service: manages products and stock — see [`com.springProject.Inventory`](inventory/src/main/java/com/springProject/Inventory.java) and [`com.springProject.Controller.ProductController`](inventory/src/main/java/com/springProject/Controller/ProductController.java)
- Order service: places orders and calls Inventory via Feign — see [`com.springProject.OrderMicrosService`](order/src/main/java/com/springProject/OrderMicrosService.java) and [`com.springProject.Controller.OrderController`](order/src/main/java/com/springProject/Controller/OrderController.java)
- Example demo application: [`src/main/java/com/springProject/demo/DemoApplication.java`](src/main/java/com/springProject/demo/DemoApplication.java)

Quick start
1. Start the Service Registry:
   mvn -pl ServiceRegistry spring-boot:run
2. Start the Inventory service:
   mvn -pl inventory spring-boot:run
3. Start the Order service:
   mvn -pl order spring-boot:run

Notes
- Services use Eureka for discovery and OpenFeign for inter-service calls.
- Database settings (PostgreSQL) and ports are configured in each module's application.properties (see [inventory/src/main/resources/application.properties](inventory/src/main/resources/application.properties) and [order/src/main/resources/application.properties](order/src/main/resources/application.properties)).
- Root build: use the project POM [pom.xml](pom.xml) to build all modules.

