var fairlightApp = angular.module('fairlightApp', []);

fairlightApp.controller('fairlightControl', function ($scope, $http) {
    $http.get('http://localhost:8080/_ah/api/fairlight/v1/users').success(function (data) {
        $scope.users = data.results;
    })
});