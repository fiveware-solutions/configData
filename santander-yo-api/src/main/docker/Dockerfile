FROM frolvlad/alpine-oraclejdk8:slim
VOLUME target
ADD santander-oy-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=oy","/app.jar"]
