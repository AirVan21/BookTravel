(function () {
  'use strict';
  
  angular.module('MyApp').controller('FindBookCtrl', FindBookCtrl);
  
  var requestChanged = "requestChanged";

  function FindBookCtrl ($q, $log, $http, $location, $window) {
    var self = this;

    self.host = $location.host() + ":" + $location.port();

    self.simulateQuery = false;
    self.isDisabled    = false;

    self.querySearch   = getHttpResponse;
    self.selectedItemChange = selectedItemChange;
    self.searchTextChange   = searchTextChange;

    self.books = []
    self.input = "";

    
    self.reload = function() {
      var name = $location.hash();
      $log.info(name);
      self.input = name;
      self.update(name);
    }

    self.onInputChanged = function() {
      $log.info(self.input);
      $location.hash(self.input);
      self.update(self.input); 
    }

    function searchTextChange(text) {
      $log.info('Text changed to ' + text);
      $location.hash(text)
    }

    function selectedItemChange(item) {
      $log.info('Item changed to ' + JSON.stringify(item));
      var id = item === undefined ? "search" : item.id;
      window.location.href = "http://" + self.host + "/book/" + id;
    }

    function getHttpResponse(query) {
      var deferred = $q.defer();
      var lowercaseQuery = angular.lowercase(query);
      $log.info(lowercaseQuery)

      $http({
        method : "GET",
        url : "search/" + lowercaseQuery,
      }).then(function mySucces(r) {
        var response = angular.fromJson(r);
        deferred.resolve(response.data);
        $log.info(response.data);
      }, function myError(r) {
        var response = angular.fromJson(r)
        deferred.resolve([]);
        $log.info(response);
      });

      return deferred.promise;
    }

    self.update = function(name) {
      if (name == "") {
        self.books = []
        return;
      }

      $http({
        method : "GET",
        url : "/book/search/" + name,
      }).then(function mySucces(r) {
        var response = angular.fromJson(r);
        self.books = angular.fromJson(response.data);
        $log.info(response.data);
      }, function myError(response) {
        $log.info(response);
      });
    }

  }

})();
