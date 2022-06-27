FROM openjdk:11
ADD target/slaveDB-0.0.1-SNAPSHOT.jar slaveDB-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","slaveDB-0.0.1-SNAPSHOT.jar"]