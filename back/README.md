![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
# Yoga Back-End

This project was generate with [Spring Boot](https://spring.io/projects/spring-boot).

## Build and Launch

### Build Spring Boot Project with Maven
To be able to run your Spring Boot app, you will need to first build it. To build and package a Spring Boot app into a single executable Jar file with a [Maven](https://maven.apache.org/), use the below command. You will need to run it from the project folder containing the pom.xml file.
```shell
maven package
```
or you can also use
```shell
mvn install
```

#### Run Spring boot app with java -jar command
To run your Spring Boot app from a command line in a Terminal window, you can you the java -jar command. This is provided your Spring Boot app was packaged as an executable jar file.
```shell
java -jar back/target/yoga-app-0.0.1-SNAPSHOT.jar
```

#### Run Spring boot app using Maven
You can also use the Maven plugin to run your Spring Boot app. Use the below example to run your Spring Boot app with the Maven plugin:
```shell
mvn spring-boot:run
```

---

## Launch test and report

For launch and generate the jacoco code coverage:
> mvn clean test

You will find HTML report here:

> ./target/site/jacoco/index.html
