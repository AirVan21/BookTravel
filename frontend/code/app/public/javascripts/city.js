(function () {
  'use strict';

  var dataAccepted = "DATA_ACCEPTED";

  angular.module('MyApp').controller('CityCtrl', CityCtrl);


  function CityCtrl ($http, $log, $scope, $location, $anchorScroll, $document) {
    var self = this;

    self.host = $location.protocol() + "://" + $location.host() + ":" + $location.port();

    self.data = [];

    self.reload = function(cityName) {
      $log.info("reload " + cityName);
      $http({
        method : "GET",
        url : self.host + "/book/searchByCity/" + cityName,
      }).then(function mySucces(r) {
        var response = angular.fromJson(r);
        self.data = response.data;
        $log.info(self.data);
      }, function myError(response) {
        $log.info(response);
      });
      
    }


    
  }

})();
