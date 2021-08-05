# Java Reactive Microservice Chassis
Microservice Chassis for Reactive Restful API with Spring Boot.

## Table of Contents
- [Getting Started](#getting-started)
    - [Requirements](#requirements)
    - [Usage](#usage)
- [JVM Performance Analysis](#jvm-performance-analysis)
  - [Comparing HotSpot vs OpenJ9](#comparing-hotspot-vs-openj9)
  - [Pre-Load testing](#pre-load-testing)
  - [Load testing](#load-testing)
  - [Conclusion](#conclusion)
- [Dependencies](#dependencies)
  - [Web Framework](#web-framework)
  - [Persistence](#persistence)
  - [Monitoring](#monitoring)
  - [Data Migration](#data-migration)
  - [Development Aid](#development-aid)
  - [API Documentation](#api-documentation)
  - [Testing](#testing)
  - [Containerization](#containerization)

- [Contributing](#contributing)
- [License](#license)

## Getting Started

### Requirements
- :whale: [Docker Engine](https://docs.docker.com/engine/install/)

### Usage
Run the restful application server:
```shell
$ docker compose up --build -d
```

Stop the restful application server:
```shell
$ docker compose stop chassis
```

Stop and remove containers and networks:
```shell
$ docker compose down
```

## JVM Performance Analysis

### Comparing HotSpot vs OpenJ9
OpenJ9 is a JVM implementation originally developed by IBM. Built to run in devices with limited memory. Some states
that OpenJ9 is a good fit to microservices running in cloud solutions such as Kubernetes. OpenJ9 is an open-source
project and its source code can be found on the GitHub page https://github.com/eclipse/openj9.

To confirm which implementation of JVM would be more efficient for this current project, I build two docker images, one
using `adoptopenjdk:16-jre-hotspot` and the other `adoptopenjdk:16-jre-openj9`. For the performance test had being used
the _Apache HTTP server benchmarking tool_ (ab), which shows how many requests per second your application is capable to
serve.

### Pre-Load testing

**Container Resources Usage:**

|NAME                 |CPU %     |MEM USAGE / LIMIT     |MEM %     |NET I/O          |BLOCK I/O
|---------------------|----------|----------------------|----------|-----------------|--------------
|chassis-openj9       |0.00%     |165.1MiB / 3.844GiB   |4.20%     |209kB / 8.1kB    |42.1MB / 123kB
|chassis-hotspot      |0.51%     |307.9MiB / 3.844GiB   |7.82%     |210kB / 8.1kB    |42.8MB / 119kB

### Load testing

**Loading test command:**

`ab -c 50 -n 1000000 -r http://<container-url>:<container-port>/v1/chassis`

---
**OpenJ9 JVM load testing:**

```
Document Path:          /v1/chassis  
Document Length:        100 bytes

Concurrency Level:      50  
Time taken for tests:   2493.535 seconds  
Complete requests:      1000000  
Failed requests:        0  
Total transferred:      198000000 bytes  
HTML transferred:       100000000 bytes  
Requests per second:    401.04 [#/sec] (mean)  
Time per request:       124.677 [ms] (mean)  
Time per request:       2.494 [ms] (mean, across all concurrent requests)  
Transfer rate:          77.54 [Kbytes/sec] received

Connection Times (ms)  
              min  mean[+/-sd] median   max  
Connect:        0    0   4.2      0     606  
Processing:     3  124 134.9     67    1491  
Waiting:        3  113 126.2     63    1455  
Total:          3  125 134.9     68    1491  

Percentage of the requests served within a certain time (ms)  
50%     68  
66%    101  
75%    152  
80%    205  
90%    331  
95%    419  
98%    520  
99%    594  
100%   1491 (longest request)
```

**Resource usage at its peak:**

|NAME                 |CPU %     |MEM USAGE / LIMIT     |MEM %     |NET I/O          |BLOCK I/O
|---------------------|----------|----------------------|----------|-----------------|--------------
|chassis-openj9       |120.94%   |266MiB / 3.844GiB     |6.76%     |575MB / 676MB    |49.7MB / 12MB


---
**HotSpot JVM load testing:**

```
Document Path:          /v1/chassis
Document Length:        100 bytes

Concurrency Level:      50
Time taken for tests:   2383.039 seconds
Complete requests:      1000000
Failed requests:        0
Total transferred:      198000000 bytes
HTML transferred:       100000000 bytes
Requests per second:    419.63 [#/sec] (mean)
Time per request:       119.152 [ms] (mean)
Time per request:       2.383 [ms] (mean, across all concurrent requests)
Transfer rate:          81.14 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1  23.3      0    1298
Processing:     3  118 132.0     63    1819
Waiting:        3  107 123.3     58    1817
Total:          3  119 133.8     64    1819

Percentage of the requests served within a certain time (ms)
50%     64
66%     96
75%    142
80%    190
90%    319
95%    415
98%    517
99%    587
100%   1819 (longest request)
```

**Resource usage at its peak:**

|NAME                 |CPU %     |MEM USAGE / LIMIT     |MEM %     |NET I/O          |BLOCK I/O
|---------------------|----------|----------------------|----------|-----------------|-----------------
|chassis-hotspot      |136.09%   |488.5MiB / 3.844GiB   |12.41%    |572MB / 673MB    |49.8MB / 7.61MB

### Conclusion
Based on the testing results, it can be observed a smaller consumption of CPU of about 15% on the OpenJ9 JVM
implementation while its throughput is about 18% higher than the Hotspot one. However, where the OpenJ9 really
standouts is its performance regarding the memory usage. OpenJ9 consumes about 45% less memory than HotSpot
implementation.

Every project has its own requirements and demands hence the JVM implementation chosen must align with its needs.
For this one in particular which is using the Spring Boot Webflux (a reactive-stack web application built on a
[Reactive Streams API](https://www.reactive-streams.org/) and running on the non-blocking [Netty](https://netty.io)
server) the OpenJ9 seems to be the best fit.

## Dependencies
#### Web Framework
- [Spring WebFlux](https://docs.spring.io/spring-framework/docs/5.3.9/reference/html/web-reactive.html#spring-webflux)
#### Persistence
- [Spring Data R2DBC](https://spring.io/projects/spring-data-r2dbc)
#### Monitoring
- [Spring Actuator](https://docs.spring.io/spring-boot/docs/2.5.3/reference/html/actuator.html)
- [Spring Cloud Sleuth](https://spring.io/projects/spring-cloud-sleuth)
#### Data Migration
- [Liquibase](https://docs.liquibase.com/home.html)
#### Development Aid
- [Lombok](https://projectlombok.org/features/all)
- [Spring Boot Dev Tools](https://docs.spring.io/spring-boot/docs/2.5.3/reference/html/using.html#using.devtools)
#### API Documentation
- [SpringDoc OpenAPI](https://springdoc.org)
- [Spring Rest Docs](https://spring.io/projects/spring-restdocs)
#### Testing
- [Spring Boot Test](https://docs.spring.io/spring-framework/docs/5.3.9/reference/html/testing.html)
- [Reactor Test](https://projectreactor.io/docs/core/release/reference/#testing)
- [Testcontainers](https://www.testcontainers.org)
- [Spring Cloud Contract Testing](https://cloud.spring.io/spring-cloud-contract/reference/html/getting-started.html#getting-started)
#### Containerization
- [Layered Jar](https://docs.spring.io/spring-boot/docs/2.5.3/maven-plugin/reference/htmlsingle/#packaging.layers)

## Contributing
Bugs, feature requests and more, in [GitHub Issues](https://github.com/neueda/java-microservices-chassis/issues).

## License
[MIT License](https://github.com/neueda/java-microservices-chassis/blob/main/LICENSE).
