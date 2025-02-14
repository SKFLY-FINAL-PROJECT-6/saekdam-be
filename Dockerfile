# 1. OpenJDK 17 (Alpine 기반)
FROM openjdk:17-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle Wrapper 및 설정 파일만 먼저 복사하여 캐싱
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

# 4. 실행 권한 부여 및 Gradle 종속성 다운로드 (캐시 활용)
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

# 5. 전체 프로젝트 파일 복사
COPY . .

# 6. Gradle 빌드 (app.jar로 빌드 결과물 생성)
RUN ./gradlew clean build -x test && \
    mv build/libs/*.jar app.jar

# 7. JAR 실행
CMD ["java", "-jar", "app.jar"]
