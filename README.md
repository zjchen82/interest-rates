# Deposit Interest Rates Comparator

This project is for user to compare the fixed deposit and saving deposit rates between financial companies and banks.

[Sprint Boot](http://spring.io/projects/spring-boot) is chosen as the backend to consume the API provided by Monetary 
Authority of Singapore (MAS) which provides monthly interest rates of banks and financial companies. It provides the 
data to front-end so that front-end can focus on the presentation. By separating the data and presentation layer, it 
empowers the backend to focus on the business logic instead of mixing up with front-end logic. It also gives front-end
more flexibility on UI & UX.   

## Getting Started

Clone this repo to your local machine using https://github.com/zjchen82/interest-rates.git

### Prerequisites

Before running the program, please make sure following are installed:
* [Java SDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Installing

A step by step series of examples that tell you how to get a development env running

- Go to project directory (e.g. <path_to_workspace>/interest-rates), and run the following in Command Prompt (Windows) or Terminal (*nix):

> Windows
```
mvnw install
```

> *nix
```
sh mvnw install
```

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


