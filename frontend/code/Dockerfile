FROM java:8
MAINTAINER Vasilev Roman <nizshee@gmail.com>

# RUN curl -O https://downloads.typesafe.com/typesafe-activator/1.3.7/typesafe-activator-1.3.7-minimal.zip
# RUN unzip typesafe-activator-1.3.7-minimal.zip -d / && rm typesafe-activator-1.3.7-minimal.zip && chmod a+x /activator-1.3.7-minimal/activator
# ENV PATH $PATH:/activator-1.3.7-minimal


RUN mkdir code
ADD ./app/target/universal/app-1.0-SNAPSHOT.zip /code
RUN cd /code && unzip app-1.0-SNAPSHOT.zip

# RUN cd /code/app && activator compile
# RUN cd /code/app && activator run



# RUN cd /code && activator new app play-scala 
# RUN apt-get update && apt-get install apt-transport-https
# RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
# RUN apt-get update && apt-get -y install scala sbt

# WORKDIR /app
# RUN activator compile

# RUN mkdir /myvol
# RUN echo "hello world" > /myvol/greeting
# VOLUME /myvol

# CMD ["activator", "run"]
# EXPOSE 9000
# RUN yum update -y && yum install -y unzip
# RUN curl -O http://downloads.typesafe.com/typesafe-activator/1.3.6/typesafe-activator-1.3.6.zip 
# RUN unzip typesafe-activator-1.3.6.zip -d / && rm typesafe-activator-1.3.6.zip && chmod a+x /activator-1.3.6/activator
# ENV PATH $PATH:/activator-1.3.6

