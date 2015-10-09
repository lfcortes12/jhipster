'use strict';

angular.module('arbitramentosApp')
    .controller('ExecutionDetailController', function ($scope, $rootScope, $stateParams, entity, Execution) {
        $scope.execution = entity;
        $scope.load = function (id) {
            Execution.get({id: id}, function(result) {
                $scope.execution = result;
            });
        };
        $rootScope.$on('arbitramentosApp:executionUpdate', function(event, result) {
            $scope.execution = result;
        });
    });
