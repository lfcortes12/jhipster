'use strict';

angular.module('arbitramentosApp')
    .controller('TaskDetailController', function ($scope, $rootScope, $stateParams, entity, Task) {
        $scope.task = entity;
        $scope.load = function (id) {
            Task.get({id: id}, function(result) {
                $scope.task = result;
            });
        };
        $rootScope.$on('arbitramentosApp:taskUpdate', function(event, result) {
            $scope.task = result;
        });
    });
