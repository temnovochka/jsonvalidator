FROM gradle:alpine
ADD . app
RUN app/gradlew jarWithDependencies
ENV PORT 80
EXPOSE 80
CMD ["java","-jar","/target/jsonValidatorGradle.jar"]