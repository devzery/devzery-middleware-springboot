# Spring Boot Logging Library

This repository contains a Spring Boot library for API monitoring.

## Tech stack

1. Java 17
2. Spring Boot 2.7.0

❗ Compatible with Spring boot version 2.x

## Building the Library

To build the library locally, run the following command:

```bash
mvn clean package -Drevision='1.0.0' -f pom.xml
```

To deploy the library to a central repository, use the following command:

```bash
mvn clean deploy -Drevision='1.0.0' -f pom.xml
```

# Using the Library in Another Spring Boot Project

  To use the library in another Spring Boot project, follow these steps:

  1. Add the library as a dependency in your `pom.xml` file:
     > [!NOTE]
     > Copy this part by clicking on the `Packages` present on right side pane 

     ```xml
         <dependency>
            <groupId>com.devzery</groupId>
            <artifactId>logging</artifactId>
            <version>1.0.10-Javax</version>
        </dependency>
    
  3. Configure the library in your Spring Boot application:

     ```java
       import org.springframework.boot.SpringApplication;
       import org.springframework.boot.autoconfigure.SpringBootApplication;

       import com.devzery.logging.LoggingEnabled;

       @SpringBootApplication
       @LoggingEnabled(apiKey = "YOUR_API_KEY", sourceName = "YOUR_SOURCE_URL")
       public class AppApplication {
	        public static void main(String[] args) {
		      SpringApplication.run(AppApplication.class, args);
	        }
       }
---------------------------------------------------------------------------------------
## Add dependency
To point maven to the repo, we also have to specify the `repositoriy` in the `pom.xml`.
```xml
<repositories>
    <repository>
        <id>com.devzery</id>
        <name>logging</name>
        <url>https://maven.pkg.github.com/devzery-services/springboot_logging</url>
        <releases><enabled>true</enabled></releases>
        <snapshots><enabled>true</enabled></snapshots>
    </repository>
</repositories>
```
## Enable authentication
The registry access is available through the GitHub api which is protected by an authorisation.
So you have ro add the credentials to the Package Registry to your global `settings.xml`:  
`Users\.m2\settings.xml`

``` xml
<settings>
  <servers>
    <server>
      <id>com.devzery</id>
      <username>YOUR_USERNAME</username>
      <password>YOUR_AUTH_TOKEN</password>
    </server>
  </servers>
</settings>
```
Replace the `YOUR_USERNAME` with your GitHub login name.  
Replace the `YOUR_AUTH_TOKEN` with a generated GitHub personal access token:  
_GitHub_ > _Settings_ > _Developer Settings_ > _Personal access tokens_ > _Generate new token_:   
The token needs at least the `read:packages` scope.
Otherwise you will get a `Not authorized` exception.

