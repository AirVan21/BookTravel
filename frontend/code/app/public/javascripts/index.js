(function () {
  'use strict';

  // angular.module('MyApp').controller('ResultCtrl', ResultCtrl);
  angular.module('MyApp').controller('AutocompleteCtrl', AutocompleteCtrl);
  
  var requestChanged = "requestChanged";

  function AutocompleteCtrl ($log) {
    var self = this;

    self.topCities = ["New York", "London", "Paris", "Rome", "Boston", 
    "Detroit", "Edinburgh", "Moscow", "Madrid", "Berlin", "Venice", "St. Petersburg"];

    self.getCities = []

    // self.simulateQuery = false;
    // self.isDisabled    = false;

    // self.querySearch   = getHttpResponse;
    // self.selectedItemChange = selectedItemChange;
    // self.searchTextChange   = searchTextChange;

    // self.books = []
    self.maxCities = 5;
    self.firstCity = "";
    
    self.reload = function() {
      var array = self.topCities;
      var m = array.length, t, i;
      
      while (m) {
        i = Math.floor(Math.random() * m--);

        t = array[m];
        array[m] = array[i];
        array[i] = t;
      }

      self.getCities = array.slice(0, self.maxCities);
      $log.info(self.getCities);

      self.firstCity = array[self.maxCities];
    }

    // function searchTextChange(text) {
    //   $log.info('Text changed to ' + text);
    // }

    // function selectedItemChange(item) {
    //   $log.info('Item changed to ' + JSON.stringify(item));
    //   var name = item === undefined ? "" : item.name;
    //   $location.hash(name);
    //   self.update(name);
    // }

    // function getHttpResponse(query) {
    //   var deferred = $q.defer();
    //   var lowercaseQuery = angular.lowercase(query);
    //   $log.info(lowercaseQuery)

    //   $http({
    //     method : "GET",
    //     url : "city/search/" + lowercaseQuery,
    //   }).then(function mySucces(r) {
    //     var response = angular.fromJson(r);
    //     deferred.resolve(response.data);
    //     $log.info(response.data);
    //   }, function myError(r) {
    //     var response = angular.fromJson(r)
    //     deferred.resolve([]);
    //     $log.info(response);
    //   });

    //   return deferred.promise;
    // }

    // self.update = function(name) {
    //   if (name == "") {
    //     self.books = []
    //     return;
    //   }

    //   $http({
    //     method : "GET",
    //     url : "book/searchByCity/" + name,
    //   }).then(function mySucces(r) {
    //     var response = angular.fromJson(r);
    //     self.books = angular.fromJson(response.data);
    //     $log.info(response.data);
    //   }, function myError(response) {
    //     $log.info(response);
    //   });
    // }

  }


  // function ResultCtrl ($http, $log, $rootScope, shared) {
  //   var self = this;


    // $rootScope.$on(requestChanged, function (event, data) {
    //     self.update(data.name);
    // });

  //   shared.update = self.update;
  // }

})();
