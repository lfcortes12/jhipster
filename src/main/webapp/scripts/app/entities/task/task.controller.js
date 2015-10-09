'use strict';

angular.module('arbitramentosApp')
    .controller('TaskController', function ($scope, Task, TaskSearch) {
        $scope.tasks = [];
        $scope.loadAll = function() {
            Task.query(function(result) {
               $scope.tasks = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Task.get({id: id}, function(result) {
                $scope.task = result;
                $('#deleteTaskConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Task.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTaskConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TaskSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.tasks = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.task = {name: null, id: null};
        };
    });
