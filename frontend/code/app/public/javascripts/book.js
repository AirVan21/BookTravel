(function () {
  'use strict';

  var dataAccepted = "DATA_ACCEPTED";

  angular.module('MyApp').controller('ViewCtrl', ViewCtrl);


  function ViewCtrl ($http, $log, $scope, $location, $anchorScroll, $document) {
    var self = this;

    self.quotes = [{title: "title"}]
    self.data = {}

    self.cb = function(id) {
      var el = $document.find("#" + id);
      $log.info("#" + id);
    }

    self.reload = function(titleId) {
      $log.info("reload " + titleId);
      $http({
        method : "GET",
        url : "get/" + titleId,
      }).then(function mySucces(r) {
        var response = angular.fromJson(r);
        self.data = response.data;
        $log.info(self.data);
        self.updateMap();
      }, function myError(response) {
        $log.info(response);
      });
      
    }


    self.marked = -1
    self.markersMap = {}
    self.changeColor = function(id) {
      $log.info("set " + id);
      if (self.marked != -1) {
        self.mapObject.markers[self.marked].element.setStyle({fill: '#F8E23B'});  
      }
      self.marked = self.markersMap[id].ind;
      self.mapObject.markers[self.marked].element.setStyle({fill: '#FF0000'});
      // self.mapObject.setFocusLatLng(1, self.markersMap[id].lat, self.markersMap[id].lng);
    }

    self.updateMap = function() {
      self.markers = []
      angular.forEach(self.data.cities, function(value) {
        if ('lat' in value.city && 'lng' in value.city) {
          var obj = {
            latLng: [value.city.lat, value.city.lng],
            name: value.city.name,
            style: {
              fill: '#F8E23B',
              stroke: '#383f47'
            }
          }
          self.markers.push(obj);
          self.markersMap[value.city.name] = {
            ind: self.markers.length - 1,
            lat: value.city.lat,
            lng: value.city.lng,
          }
          $log.info(value.city.name);
        }
      });

      self.render();
    }

    self.render = function() {
      
      // $('#world-map').remove();
      $('#world-map').vectorMap({
        map: 'world_mill',
        markers: self.markers,
        onMarkerClick: function(event, index) {
          var newHash = self.markers[index].name
          $location.hash(newHash);
          self.changeColor(newHash);
          $anchorScroll();
        }
      });
      self.mapObject = $('#world-map').vectorMap('get', 'mapObject');
      self.mapObject.setFocusLatLng = function(scale, lat, lng){
        var point,
            proj = jvm.WorldMap.maps[this.params.map].projection,
            centralMeridian = proj.centralMeridian,
            width = this.width - this.baseTransX * 2 * this.baseScale,
            height = this.height - this.baseTransY * 2 * this.baseScale,
            inset,
            bbox,
            scaleFactor = this.scale / this.baseScale,
            centerX,
            centerY;

        if (lng < (-180 + centralMeridian)) {
            lng += 360;
        }

        point = jvm.Proj[proj.type](lat, lng, centralMeridian);

        inset = this.getInsetForPoint(point.x, point.y);
        if (inset) {
            bbox = inset.bbox;

            centerX = (point.x - bbox[0].x) / (bbox[1].x - bbox[0].x);
            centerY = (point.y - bbox[0].y) / (bbox[1].y - bbox[0].y);

            this.setFocus(scale, centerX, centerY);
        }
    }
    
    }

  }

})();
