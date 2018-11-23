FROM openjdk:11
WORKDIR /
COPY rankscraper.jar /app.jar
CMD ["/usr/bin/java", "-jar", "/app.jar" ]