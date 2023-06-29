FROM maven:latest AS maven
ARG GITHUB_USERNAME
ARG GITHUB_PASSWORD
ARG GITHUB_REPOSITORY

ENV GITHUB_USERNAME=$GITHUB_USERNAME
ENV GITHUB_PASSWORD=$GITHUB_PASSWORD
ENV GITHUB_REPOSITORY=$GITHUB_REPOSITORY

WORKDIR /service
COPY . .
RUN mvn package -DskipTests -s ci-settings.xml

FROM openjdk:22 AS openjdk
COPY --from=maven /service/target/*.jar /service.jar
COPY entrypoint.sh /opt/entrypoint.sh
RUN chmod +x /opt/entrypoint.sh

CMD /opt/entrypoint.sh