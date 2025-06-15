FROM eclipse-temurin:21-jdk-jammy

ENV MQTT_ADDR=tcp://mosquitto:1883
ENV MQTT_TOPIC=living-room

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew build

EXPOSE 8080

CMD ["./gradlew", "run"]
