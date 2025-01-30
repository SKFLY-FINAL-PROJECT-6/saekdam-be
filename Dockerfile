FROM openjdk:17-slim

WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar -x test    # 테스트 스킵

EXPOSE 8080
CMD java -jar $(find build/libs/ -name '*.jar')