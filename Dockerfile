FROM gradle:alpine
USER root
ADD . app
RUN chmod +x app/gradlew
RUN cd app && ./gradlew jarWithDependencies
ENV PORT 80
EXPOSE 80
CMD ["java","-jar","app/build/libs/jvalidator-all-1.0.jar"]