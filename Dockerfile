FROM openjdk:17
WORKDIR .
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY . .
ENTRYPOINT ["java","-jar","app.jar"]