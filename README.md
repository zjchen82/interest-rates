# Deposit Interest Rates Comparator

This project is for user to compare the fixed deposit and saving deposit rates between financial companies and banks.

It uses APIs provided by Monetary Authority of Singapore (MAS) which provides monthly interest rates of banks and financial companies.

[Sprint Boot](http://spring.io/projects/spring-boot) is chosen to consume the API and provide the data to front-end which is focus on presenting the data.   

## Getting Started

Clone this repo to your local machine using https://github.com/zjchen82/interest-rates.git

### Prerequisites

Before running the program, please make sure following are installed:
* [Java SDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven 3](https://maven.apache.org/)

### Installing

A step by step series of examples that tell you how to get a development env running

- Go to project directory (e.g. <path_to_workspace>/interest-rates), and run the following:

```
mvn install
```
Note that you need to use the full path to mvn if it cannot be found.

- Run the application
```
java -jar target/interest-rates-0.0.1-SNAPSHOT.jar
```
Once you see the following, the application is up and running
> Tomcat started on port(s): 8080 (http) with context path ''

Open [http://localhost:8080](http://localhost:8080) in your browser and start using the system. 

## Running the tests

You can run the unit tests via the following:

```
mvn test
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

We use [SemVer](http://semver.org/) for versioning. 


