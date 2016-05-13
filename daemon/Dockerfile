FROM java:8
MAINTAINER Vasilev Roman <nizshee@gmail.com>

RUN mkdir code
ADD . /code
RUN cd code && ./gradlew build

RUN mkdir uploaded
RUN touch /uploaded/test

WORKDIR /code


# docker build -t nizshee/geobook_daemon:0.2 .
# docker run -p 4567:4567 nizshee/geobook_daemon:0.2 ./gradlew run
# docker run -p 4567:4567 -v /home/user/uploaded:/code/uploaded -d nizshee/geobook_daemon:0.2 ./gradlew run
# docker run -p 4567:4567 -v /home/user/uploaded:/code/uploaded nizshee/geobook_daemon:0.2 ./gradlew run
