#########################################
##          Fibonacci Service          ##
#########################################

# The Project Assignment

1. The project should provide a web service.
   a. The web service accepts a number, n, as input and returns the first n Fibonacci 
      numbers, starting from 0. I.e. given n = 5, appropriate output would represent the 
      sequence "0 1 1 2 3".
   b. Given a negative number, it will respond with an appropriate error.
2. Include whatever instructions are necessary to build and deploy/run the project, where 
   "deploy/run" means the web service is accepting requests and responding to them as appropriate.
3. Include some tests
4. Implement as if it were to be supported for 5 year

# The Implemented Project

1. The project should provide a web service.
   a. The web service accepts a number, n, as input and returns the first n Fibonacci 
      numbers, starting from 0. I.e. given n = 5, appropriate output would represent the 
      sequence "0 1 1 2 3".
   b. Given a negative number, it will respond with an appropriate error.
-> c. An optional start parameter was added allowing the client to specify the starting
      Fibonacci index, e.g., start=10,length=5 = [55, 89, 144, 233, 377]
2. Include whatever instructions are necessary to build and deploy/run the project, where 
   "deploy/run" means the web service is accepting requests and responding to them as appropriate.
3. Include some tests
4. Implement as if it were to be supported for 5 year

# Work Schedule Breakdown

12 hours were used to research the best platform for implementation.
10 hours was spent evaluating platform options and selecting the most appropriate platform.
20 hours was spent in initial development, including troubleshooting platform issues due
   to unfamiliarity with platform.
15 hours was spent troubleshooting/debugging issues and refactoring design/implementation
   for performance.
 3 hours spent researching CXF GZIP implementation to ensure most optimal method used.
 8 hours researching and refining build process.

Total Time ~60 man hours.


######################################
##        Project Information       ##
######################################

# Platform

The Apache Karaf OSGi container using OSGi Blueprint bundle deployment was chosen because 
OSGi w/ Blueprint support is a widely supported standard, supported by standalone OSGi 
containers, such as Apache Felix & Eclipse Equinox. OSGi Blueprint bundles are also 
supported in many application servers, such as JBoss AS, Apache Geronimo, Glassfish, and 
WebLogic. While these application servers support J2EE Enterprise Beans, OSGi also provides
the ability for the service to be deployed in a minimal, lightweight environment running as
a Microservice. The project instructions provide the installation and deployment details
to run and debug the Fibonacci service as a Microservice within Apache Karaf using Apache 
Felix as the OSGi container. Apache Karaf was chosen over the base Felix as it provides 
support to use Eclipse Equinox as its OSGi container and provides a command line tool that
greatly simplifies the setup and management of the OSGi environment.

Apache CXF was chosen as the services framework to expose the Fibonacci service as a JAX-RS
Web Service due to its OSGi support, easy integration with Apache Karaf and its wide
adoption.  Please note that the Fibonacci service is not bound to Apache CXF and the 
service framework can be swapped out without negatively impacting the Fibonacci service.
To change the service framework the OSGi Blueprint file should be changed in accordance to
the new service framework.

# Fibonacci Service Design

The Fibonacci service has been designed as a series of Java POJO beans stitched together 
with the OSGi Blueprint. If needed, the Blueprint file can be discarded and the Bundle
Activator can be used to instantiate the beans and set references/configuration values.

The Fibonacci service is divided in X pluggable layers
1. A Fibonacci singleton bean that acts as the in-process entry point to the API.
2. A Fibonacci generation layer that facilitates pluggable Fibonacci generation services.
   Currently 2 Fibonacci generation services are implemented, SequentialFibonacciGenerator
   and MatrixFibonacciGenerator. Which generator is used is configured in the OSGi 
   Blueprint. The Fibonacci generation layer is designed to work with the optional 
   Fibonacci storage implementation.
4. An optional Fibonacci storage layer that is used to pre-generate a Fibonacci sequence
   that is used to return results without recalculating the Fibonacci sequence.
3. Network exposure. The Fibonacci RESTful Web Service and any other network interface
   implementation exposes the Fibonacci singleton bean as a remotely accessible API. The
   Network exposure layer can support nearly any for of network protocol/exposure, e.g., 
   JAX-RS, REST via Servlet, JAX-WS, XML over Socket, WebSocket and Corba exposures could
   all be built to utilize the same thread-safe Fibonacci singleton bean.


###############################################
## Running and Testing the Fibonacci Service ##
###############################################

The Apache Karaf and Fibonacci Service installation instructions are in INSTALL.txt
The Apache Karaf and Fibonacci Service Microservice start instructions are in STARTUP.txt
The Apache Karaf and Fibonacci Service Java debugger instructions are in DEBUG.txt
The Fibonacci Service test client instructions are in TESTS.txt
