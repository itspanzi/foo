This solution is implemented using Java. Please take a look at the instructions below to get the code running.

# Running the code

## Prerequisites

* JDK 1.8 (My version - *1.8.0_45*)
* Apache Maven 3 (My version - *3.1.1*)

## Running from the current directory

* cd into the project directory (the directory which contains *pom.xml*)
* Run the command 
    mvn clean package jetty:run-war
* This command will first download the internet (of course!), compile, test, build war and deploy to a local Jetty instance
* You can now access the service at [localhost:8080](http://localhost:8080/runatic/report)

## Running in your own container

* cd into the project directory (the directory which contains *pom.xml*)
* Run the command 
    mvn clean package
* This command will first download the internet (sadly!), compile, test & build a war
* The war will be available at _target/reports-1.0-SNAPSHOT.war_. You can copy this over to any servlet container of your choice
** I have tested this with Tomcat and Jetty.
* After deployment, you can access the service at HOSTNAME:PORT/WEB_CONTEXT/runatic/report

