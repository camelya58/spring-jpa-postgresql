# spring-jpa-postgresql
Simple project using Spring DATA JPA, PostgreSQL and Thymeleaf.

Stack: Spring DATA JPA, PostgreSQL, Thymeleaf, Lombok, Maven.

## Step 1
Install the PostgreSQL database.
```
brew install postgresql
```

## Step 2
Start the postgreSQL.
```
brew services start postgresql
```
Create the database.
```
createdb testdb
```

## Step 3
Create the user.
```
create user <userName> with password '<password>'
```
Set the role for the user.
```
alter role <userName> createrole, createdb
```

## Step 4
Add dependencies.
```
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
 <dependencies>
```

## Step 5
In the application.properties file we write various configuration settings of a Spring Boot application:
- with the spring.main.banner-mode property we turn off the Spring banner; 
- to load a database that is not embedded, in Spring Boot 2 we need to add spring.datasource.initialization-mode=always; 
- to avoid conflicts, we turn off automatic schema creation with spring.jpa.hibernate.ddl-auto=none;
- in the spring datasource properties we set up the PostgreSQL datasource;
- the spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation option is set avoid a recent issue.
Without this option, we get the following error:

java.sql.SQLFeatureNotSupportedException: Method org.postgresql.jdbc.PgConnection.createClob() 
is not yet implemented.

```properties
spring.main.banner-mode=off
logging.level.org.springframework=ERROR

spring.jpa.hibernate.ddl-auto=none

spring.datasource.initialization-mode=always
spring.datasource.platform=postgres
spring.datasource.url=jdbc:postgresql://localhost:5432/testdb
spring.datasource.username=username
spring.datasource.password=password

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

server.port=8099
```

## Step 6
Create the model.
```java
@Data
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int population;
}
```
- The @Entity annotation specifies that the class is an entity and is mapped to a database table. 
- The @Table annotation specifies the name of the database table to be used for mapping.
- The @Id annotation specifies the primary key of an entity.
- The @GeneratedValue provides for the specification of generation strategies for the values of primary keys.

## Step 7
Create the schema-postgres.sql file in resources.

When the application is started, the schema-postgres.sql script is executed provided 
that the automatic schema creation is turned off. The script creates a new database table.
```
DROP TABLE IF EXISTS cities;
CREATE TABLE cities(id serial PRIMARY KEY, name VARCHAR(255), population integer);
```

## Step 8
Create the data-postgres.sql file to fill the table with data.
```
INSERT INTO cities(name, population) VALUES('Bratislava', 432000);
INSERT INTO cities(name, population) VALUES('Budapest', 1759000);
INSERT INTO cities(name, population) VALUES('Prague', 1280000);
INSERT INTO cities(name, population) VALUES('Warsaw', 1748000);
INSERT INTO cities(name, population) VALUES('Los Angeles', 3971000);
INSERT INTO cities(name, population) VALUES('New York', 8550000);
INSERT INTO cities(name, population) VALUES('Edinburgh', 464000);
INSERT INTO cities(name, population) VALUES('Berlin', 3671000);
```

## Step 9
Create the CityRepository class. 
By extending from the Spring JpaRepository, we will have some methods for our data repository implemented, including findAll().
```java
@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    City findCityByName(String name);
}
```
This method allows to create an SQL query such as:
```
SELECT * FROM cities WHERE name = ?;
```

## Step 10
Create the CityService interface with 2 methods and their realizations in the CityServiceImpl class.
```java
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    /**
     * Provides a contract method to get all cities from the data source.
     * @return list of cities
     */
    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    /**
     * Provides a contract method to get the city by name from the data source.
     * @return city the object of class City
     */
    @Override
    public City findByName(String name) {
        return cityRepository.findCityByName(name);
    }
}
```

## Step 11
And then create a controller with 2 methods: to get the list with all cities and to get the city by name.
```java
@Controller
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/showCities")
    public String findCities (Model model) {
        var cities = cityService.findAll();
        model.addAttribute("cities", cities);
        return "showCities";
    }
    @GetMapping("/showCity/{name}")
    public String findCity(@PathVariable String name, Model model) {
        City city = cityService.findByName(name);
        model.addAttribute("city", city);
        return "showCity";
    }
}
```

## Step 12
From the first method the model gains a list of cities and the processing is sent to the showCities.html Thymeleaf template file.

Each city has a hyperlink to the showCity.html Thymeleaf template file. 
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Cities</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>
<h2>List of cities</h2>

<table>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Population</th>
    </tr>
    <tr th:each="city : ${cities}">
        <td th:text="${city.id}">Id</td>
        <td ><a th:href="@{'/showCity/'+ ${city.name}}" th:text="${city.name}" target="_blank" /></td>
        <td th:text="${city.population}">Population</td>

    </tr>
</table>

</body>
</html>
```

## Step 13
Create the showCity.html Thymeleaf template file. 
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>City</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>
<h2>Requested city</h2>

<table>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Population</th>
    </tr>

    <tr>
        <td th:text="${city.id}">Id</td>
        <td th:text="${city.name}">Name</td>
        <td th:text="${city.population}">Population</td>
    </tr>
</table>

</body>
</html>
```

## Step 14
Create the main page. In the index.html there is a link to show all cities.
```html
<!DOCTYPE html>
<html>
<head>
    <title>Home page</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<a href="showCities">Show cities</a>
</body>
</html>
```

## Step 15
The Application sets up the Spring Boot application. 
The @SpringBootApplication enables auto-configuration and component scanning.
```java
@SpringBootApplication
public class SpringJpaPostgresqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJpaPostgresqlApplication.class, args);
    }

}
```

After the application is run, we can navigate to localhost:8099.
