# 1. 기본 OpenJDK 17 사용
FROM openjdk:17

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 시스템 패키지 업데이트 및 xargs 설치
RUN apt-get update && apt-get install -y findutils

# 4. 프로젝트 파일 복사
COPY . .

# 5. 실행 권한 부여 및 Gradle 빌드 수행
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# 6. 빌드된 JAR 파일 실행
CMD ["java", "-jar", "build/libs/demo-0.0.1-SNAPSHOT.jar"]