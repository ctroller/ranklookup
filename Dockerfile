# fetch basic image
FROM maven:3.6-jdk-11

# application placed into /opt/app
RUN mkdir -p /opt/app
WORKDIR /opt/app

# selectively add the POM file and
# install dependencies
COPY pom.xml /opt/app/
COPY application.properties /opt/app/
RUN mvn install

# rest of the project
COPY src /opt/app/src
RUN mvn package

# local application port
EXPOSE 8080

# execute it
CMD mvn exec:java -Dexec.args="-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8080"