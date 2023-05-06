FROM openjdk:17
ADD target/courseApp-0.0.1-SNAPSHOT.jar .
EXPOSE 8000
RUN apt-get update && \
    apt-get install -y mysql-client && \
    rm -rf /var/lib/apt/lists/*
CMD java -jar courseApp-0.0.1-SNAPSHOT.jar