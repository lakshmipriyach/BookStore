FROM eclipse-temurin:17

LABEL mentainer="laxmipriya@natsoft.us"

WORKDIR /app

COPY target/BookStore-0.0.1-SNAPSHOT.jar /app/bookstore.jar

ENTRYPOINT ["java", "-jar", "bookstore.jar"]