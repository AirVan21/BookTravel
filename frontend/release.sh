#! /bin/bash

cd ./code/app
./activator dist
cd ../..
docker build -t nizshee/geobook:0.2 ./code