FROM index.tinyvoice.net/basic_images/jdk:1.8
MAINTAINER siyan.chen@changhong.com
COPY ./target/scheduler-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar ./app.jar