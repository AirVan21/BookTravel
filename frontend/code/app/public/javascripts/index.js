(function () {
  'use strict';

  // angular.module('MyApp').controller('ResultCtrl', ResultCtrl);
  angular.module('MyApp').controller('AutocompleteCtrl', AutocompleteCtrl);
  
  var requestChanged = "requestChanged";

  function AutocompleteCtrl ($timeout, $q, $log, $http, $location) {
    var self = this;

    self.topCities = ["New York", "London", "Paris", "Rome", "Boston", "Detroit", "Edinburgh", "Moscow"];

    self.simulateQuery = false;
    self.isDisabled    = false;

    self.querySearch   = getHttpResponse;
    self.selectedItemChange = selectedItemChange;
    self.searchTextChange   = searchTextChange;

    self.books = []

    
    self.reload = function() {
      var name = $location.hash();
      self.searchText = name;
      self.update(name);
      // $rootScope.$broadcast(requestChanged, { name: text });
      $log.info(name);
    }

    function searchTextChange(text) {
      $log.info('Text changed to ' + text);
    }

    function selectedItemChange(item) {
      $log.info('Item changed to ' + JSON.stringify(item));
      var name = item === undefined ? "" : item.name;
      $location.hash(name);
      self.update(name);
    }

    function getHttpResponse(query) {
      var deferred = $q.defer();
      var lowercaseQuery = angular.lowercase(query);
      $log.info(lowercaseQuery)

      $http({
        method : "GET",
        url : "city/search/" + lowercaseQuery,
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
        url : "book/searchByCity/" + name,
      }).then(function mySucces(r) {
        var response = angular.fromJson(r);
        self.books = angular.fromJson(response.data);
        $log.info(response.data);
      }, function myError(response) {
        $log.info(response);
      });
    }

  }


  // function ResultCtrl ($http, $log, $rootScope, shared) {
  //   var self = this;


    // $rootScope.$on(requestChanged, function (event, data) {
    //     self.update(data.name);
    // });

  //   shared.update = self.update;
  // }

})();
