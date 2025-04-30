FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ADD https://dtdg.co/java-tracer-v1 dd-java-agent.jar

ENTRYPOINT ["java",
  "-javaagent:dd-java-agent.jar",
  "-Ddd.service=bookhive",
  "-Ddd.env=production",
  "-Ddd.version=1.0",
  "-jar", "app.jar"]