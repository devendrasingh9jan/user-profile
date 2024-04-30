FROM maven:3.9.6 as builder
COPY . /user-profile/app
WORKDIR /user-profile/app
RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /user-profile/app
COPY --from=builder /user-profile/app/target/*.jar app.jar
EXPOSE 8005
ENTRYPOINT ["java", "-jar", "app.jar"]
