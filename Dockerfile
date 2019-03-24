FROM openjdk:8-jdk

MAINTAINER cristofor.nogueira@gmail.com

COPY build/libs/trolley*.jar /opt/trolley.jar

ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/trolley.jar"]

EXPOSE 8080
