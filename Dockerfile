FROM maven:3.3-jdk-8-onbuild
MAINTAINER Nastya <temnovastudy@gmail.com>
ENV PORT 80
CMD java -jar /target/jsonValidatorGradle.jar