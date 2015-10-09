'use strict';

angular.module('arbitramentosApp')
    .controller('ProcesoDetailController', function ($scope, $rootScope, $stateParams, entity, Proceso) {
        $scope.proceso = entity;
        $scope.load = function (id) {
            Proceso.get({id: id}, function(result) {
                $scope.proceso = result;
            });
        };
        $rootScope.$on('arbitramentosApp:procesoUpdate', function(event, result) {
            $scope.proceso = result;
        });
    });
