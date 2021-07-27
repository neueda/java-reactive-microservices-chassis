## Comparing Hotspot vs OpenJ9
OpenJ9 is a JVM implementation originally developed by IBM. Built to run in devices with limited memory some states
OpenJ9 is a good fit to microservices running solutions such as Kubernetes. OpenJ9 is an open-source project and its
GitHub page is https://github.com/eclipse/openj9 where it can be found the source code.

To confirm which implementation of JVM is really more efficient I have built for this project one docker image using
**adoptopenjdk:16-jre-hotspot** and the another using **adoptopenjdk:16-jre-openj9**. It can be found below the memory and CPU
usage for each of then, as well as, the result of their performance test with Apache HTTP server benchmarking tool (ab).

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
**Hotspot JVM load testing:**

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
