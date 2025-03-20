# Spring Boot Middleware Library

This repository contains a Spring Boot library for API monitoring.

## Tech stack

1. Java 8
2. Spring Boot 2.7.0

❗ **Compatible with Spring Boot version 2.x and 3.x and Java 8+**

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
    > 💡 **Note:** The dependency can be found on Maven Central. Visit [Maven Central Repository](https://central.sonatype.com/artifact/com.devzery/middleware) to get the latest version and dependency snippet.

     ```xml
         <dependency>
          <groupId>com.devzery</groupId>
          <artifactId>middleware</artifactId>
          <version>1.0.0</version>
        </dependency>
    
  3. Configure the library in your Spring Boot application:

     ```java
       import org.springframework.boot.SpringApplication;
       import org.springframework.boot.autoconfigure.SpringBootApplication;

       **`import com.devzery.middleware.MiddlewareEnabled;`**

       @SpringBootApplication
       @MiddlewareEnabled(apiKey = "YOUR_API_KEY", serverName = "YOUR_SERVER_URL")
       public class AppApplication {
	        public static void main(String[] args) {
		      SpringApplication.run(AppApplication.class, args);
	        }
       }
---------------------------------------------------------------------------------------
## Contributing Guidelines

> 💡 **Important Notes for Contributors:**

### Version Management
- Do not modify version numbers in `pom.xml`
- Versioning is automatically handled by GitHub Actions

### Commit Message Format
Follow these conventions for commit messages:

✅ **Acceptable Formats:**
```
patch: fix API response handling
minor: add new feature
```

❌ **Unacceptable Formats:**
```
Updated code
Bumped version in pom.xml
```

For detailed commit message rules, check `husky -> commit-msg` in the project.


