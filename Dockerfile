# 1. OpenJDK 17 (Alpine 기반)
FROM openjdk:17-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. xargs 설치 (findutils 포함)
RUN apk add --no-cache findutils

# 4. 프로젝트 파일 복사
COPY . .

# 5. 실행 권한 부여 및 Gradle 빌드 수행
RUN chmod +x gradlew
RUN ./gradlew clean build -x test && ls -al build/libs && cp build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# 6. JAR 실행
CMD ["java", "-jar", "app.jar"]