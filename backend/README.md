# API
Project : **Cash manager**
> Backend API using JAVA

# Usage
### build:
```
mvn compile
```
### test:
```
mvn test` # code coverage in ./target/site/jacoco/
```
### package:
```
mvn package # output ./target/cashmanager-0.0.1-jar-with-dependencies.jar
```
### exec (devlocal):
```
mvn exec:java -Dcashmanager.config.localfile=$(pwd)/CashManagerConfig.json
```
### exec (prod):
```
java -Dcashmanager.config.localfile=/CashManagerConfig.json -jar ./target/cashmanager-0.0.1-jar-with-dependencies.jar
```
### debug tests:
```
mvn -Dmaven.surefire.debug test
```
### debug main:
```
mvnDebug exec:java -Dcashmanager.config.localfile=$(pwd)/CashManagerConfig.json
```