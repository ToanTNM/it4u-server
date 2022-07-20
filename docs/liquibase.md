# Liquibase - implement to project

> Author: MT  
Created date: 2022/06/03  
Updated date: 2022/06/03

## 1. Add to ```pom.xml``` ```dependencies```

```xml
<dependency>
  <groupId>org.liquibase</groupId>
  <artifactId>liquibase-maven-plugin</artifactId>
  <version>4.11.0</version>
</dependency>
<dependency>
  <groupId>org.liquibase</groupId>
  <artifactId>liquibase-core</artifactId>
</dependency>
```

Add to ```pom.xml``` ```plugins```

```xml
<plugin>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-maven-plugin</artifactId>
    <configuration>
     <propertyFile>src/main/resources/liquibase.properties</propertyFile>
    </configuration>
    <dependencies>
     <dependency>
      <groupId>org.liquibase.ext</groupId>
      <artifactId>liquibase-hibernate5</artifactId>
      <version>4.11.0</version>
     </dependency>
     <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-beans</artifactId>
      <version>5.3.20</version>
     </dependency>
     <dependency>
      <groupId>org.springframework.data</groupId>
      <artifactId>spring-data-jpa</artifactId>
      <version>2.7.0</version>
     </dependency>
    </dependencies>
```

## 2. Config liquibase

Enable ```liquibase``` in ```application.yml```

```yml
spring:
  liquibase:
    enabled: true #false when use with H2 database
    change-log: classpath:db/changelog/db.changelog-master.yml
```

Create ```liquibase.properties``` at the path config in ```pom.xml```

```ini
outputChangeLogFile=src/main/resources/db/changelog/db.changelog.yml
changeLogFile=src/main/resources/db/changelog/db.changelog-master.yml
diffChangeLogFile=src/main/resources/db/changelog/liquibase-diff-changeLog.yml
includeSchema=true
includeTablespace=true

url=jdbc:postgresql://localhost:5433/it4uPG
driver=org.postgresql.Driver
username=it4u
password=

; referenceUrl=hibernate:spring:vn.tpsc.it4u.model?dialect=org.hibernate.dialect.PostgreSQLDialect
referenceUrl=jdbc:postgresql://localhost:5433/it4uPG
referenceDriver=org.postgresql.Driver
referenceUsername=sa
referencePassword=
```

**Note**: ```liquibase``` not work with in-memory database (such as H2 mem)

Disable JPA auto create schema and run sql:

```yml
spring:
  jpa:
    show-sql: true
    generate-ddl: false
    defer-datasource-initialization: false #false when use with liquibase
    # true when use with H2 database

  sql:
    init:
      data-locations:
        - classpath:db\changelog\data.sql
      mode: never #always when use H2 database
      continue-on-error: true
```

## 3. liquibase command

- ```mvn liquibase:update```: update DB theo changelog của liquibase

- ```mvn liquibase:generateChangeLog```: tạo schema từ DB

- ```mvn liquibase:diff```: so sánh 2 DB và tạo ra file migrate
