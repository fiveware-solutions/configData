FROM frolvlad/alpine-oraclejdk8:slim
VOLUME target
ADD target/santander-rcb-api-0.0.1-SNAPSHOT.jar santander-rcb-api-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /santander-rcb-api-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/santander-rcb-api-0.0.1-SNAPSHOT.jar"]
