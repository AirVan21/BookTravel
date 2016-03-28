(function () {
  'use strict';

  var dataAccepted = "DATA_ACCEPTED";

  angular.module('MyApp',['ngMaterial', 'ngMessages']);
  angular.module('MyApp').controller('ViewCtrl', ViewCtrl);


  function ViewCtrl ($http, $log, $scope, $location, $anchorScroll) {
    var self = this;

    self.quotes = [{title: "title"}]
    self.data = {}

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

    self.updateMap = function() {
      self.markers = []
      angular.forEach(self.data.cities, function(value) {
        if ('lat' in value.city && 'lng' in value.city) {
          self.markers.push({
            latLng: [value.city.lat, value.city.lng],
            name: value.city.name
          });
          $log.info(value.city.name);
        }
      });

      $(function(){
        $('#world-map').vectorMap({
          map: 'world_mill',
          markerStyle: {
            initial: {
              fill: '#F8E23B',
              stroke: '#383f47'
            }
          },
          markers: self.markers,
          onMarkerClick: function(event, index) {
            var newHash = self.markers[index].name
            $location.hash(newHash);
            $anchorScroll();
          }
        });
      });
    }

  }

})();
