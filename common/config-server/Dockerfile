FROM frolvlad/alpine-oraclejdk8:slim
VOLUME target
ADD target/common-config-server-1.jar common-config-server-1.jar
RUN sh -c 'touch /common-config-server-1.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/common-config-server-1.jar"]
