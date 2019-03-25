FROM openjdk:8-jdk as builder
RUN mkdir /app
WORKDIR /app
COPY . /app
RUN /app/gradlew bootJar

FROM openjdk:8-jdk
MAINTAINER cristofor.nogueira@gmail.com
RUN mkdir /app
COPY --from=builder /app/build/libs/trolley-*.jar /app/trolley.jar
EXPOSE 8080
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/app/trolley.jar"]
