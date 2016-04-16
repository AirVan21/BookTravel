(function () {
  'use strict';


  angular.module('MyApp').controller('ViewCtrl', ViewCtrl);

  function ViewCtrl ($http, $log, $scope, $location, $anchorScroll) {
    var self = this;

    self.data = []
    self.host = ""

    self.reload = function(pageId) {
      self.host = $location.host() + ":" + $location.port();
      $log.info(self.host);
      $log.info("reload " + pageId);
      $http({
        method : "GET",
        url : "get/" + pageId,
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
