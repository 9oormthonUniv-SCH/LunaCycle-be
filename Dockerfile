# 최소 실행용 (JAR는 미리 빌드되어 있어야 함)
FROM eclipse-temurin:21-jre
WORKDIR /app

# 파일명이 고정돼 있다면 그 이름으로, 아니면 패턴 1개만 매칭되게 관리
COPY build/libs/*-SNAPSHOT.jar app.jar

# (선택) 운영 편의
ENV TZ=Asia/Seoul \
    JAVA_OPTS=""

# (선택) H2 파일 보존하려면 컨테이너 경로 사용 + compose에서 /app/data 볼륨 마운트
VOLUME ["/app/data", "/app/logs"]

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
