FROM frolvlad/alpine-oraclejdk8:slim
VOLUME target
ADD lab-9-gateway-solution-1.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
