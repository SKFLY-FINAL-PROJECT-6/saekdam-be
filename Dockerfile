# 1. 기본 OpenJDK 17 사용
FROM openjdk:17

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 프로젝트 파일 복사
COPY . .

# 4. 실행 권한 부여 및 Gradle 빌드 수행
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# 5. 빌드된 JAR 파일 실행
CMD ["java", "-jar", "build/libs/demo-0.0.1-SNAPSHOT.jar"]
