FROM eclipse-temurin:21-jre
WORKDIR /app
COPY app.jar /app/app.jar
ENV TZ=Asia/Seoul JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
