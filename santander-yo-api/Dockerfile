FROM frolvlad/alpine-oraclejdk8:slim
VOLUME target
ADD target/santander-oy-api-0.0.1-SNAPSHOT.jar santander-oy-api-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /santander-oy-api-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=oy","/santander-oy-api-0.0.1-SNAPSHOT.jar"]
