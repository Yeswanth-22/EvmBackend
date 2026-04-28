FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# copy everything
COPY . .

# ✅ run maven where pom.xml exists (ROOT)
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# copy built jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java","-jar","app.jar"]