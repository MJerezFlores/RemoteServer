angular.module('myApp', ["ngMessages", 'ngMaterial'])

    //////////////CONTROLLERS////////////////////

.controller('ConfigurationController', ["$scope","$http", "$mdToast", function($scope, $http, $mdToast) {

    $scope.addConfiguration = function($event) {
        $event.preventDefault();

        $http.post("set/configuration", $scope.configuration);

        $mdToast.show(
            $mdToast.simple()
                .textContent('Configuration sent!')
                .position("top right")
                .hideDelay(3000)
        );
    }

}]);