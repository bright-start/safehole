FROM openjdk:8

MAINTAINER white_xiaosheng

ADD ./safehole-0.0.1-SNAPSHOT.jar safehole/

WORKDIR safehole/


EXPOSE 8082

RUN chmod 755 safehole-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "safehole-0.0.1-SNAPSHOT.jar"]