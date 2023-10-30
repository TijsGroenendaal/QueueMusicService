FROM maven:3.9.0 AS maven
ARG CI_GITHUB_USERNAME
ARG CI_GITHUB_PASSWORD
ARG CI_GITHUB_REPOSITORY

ENV CI_GITHUB_USERNAME=$CI_GITHUB_USERNAME
ENV CI_GITHUB_PASSWORD=$CI_GITHUB_PASSWORD
ENV CI_GITHUB_REPOSITORY=$CI_GITHUB_REPOSITORY

WORKDIR /service
COPY . .
RUN mvn package -DskipTests -s ci-settings.xml -q

FROM openjdk:22 AS openjdk
COPY --from=maven /service/target/*.jar /service.jar
COPY entrypoint.sh /opt/entrypoint.sh
RUN chmod +x /opt/entrypoint.sh

CMD /opt/entrypoint.sh