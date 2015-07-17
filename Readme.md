This solution is implemented using Java. Please take a look at the instructions below to get the code running.

# Running the code

## Prerequisites

* JDK 1.8 (My version - *1.8.0_45*)
* Apache Maven 3 (My version - *3.1.1*)

## Running from the current directory

* cd into the project directory (the directory which contains *pom.xml*)
* Run the command: __mvn clean package jetty:run-war__
* This command will first download the internet (of course!), compile, test, build war and deploy to a local Jetty instance
* You can now access the service at [localhost:8080](http://localhost:8080/runatic/report)

## Running in your own container

* cd into the project directory (the directory which contains *pom.xml*)
* Run the command: __mvn clean package__
* This command will first download the internet (no escape!), compile, test & build a war
* The war will be available at _target/reports-1.0.0.war_. You can copy this over to any servlet container of your choice. I have tested this with Tomcat and Jetty.
* After deployment, you can access the service at _http://HOSTNAME:PORT/WEB_CONTEXT/runatic/report_

# Tech Stack & Design

The project uses [Jersey (jax-rs)](https://jersey.java.net) a light weight REST framework to implement the service. There is a lot of use of the new JDK 8 streaming API which along with lambda expressions reduce code massively.

The data store files are read once & cached for the duration of the server run. Since the REST service class is a singleton, the ReportGeneratorService class is created once. This is where the cache resides.