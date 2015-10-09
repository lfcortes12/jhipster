'use strict';

angular.module('arbitramentosApp')
    .controller('ExecutionController', function ($scope, Execution, ExecutionSearch) {
        $scope.executions = [];
        $scope.loadAll = function() {
            Execution.query(function(result) {
               $scope.executions = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Execution.get({id: id}, function(result) {
                $scope.execution = result;
                $('#deleteExecutionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Execution.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExecutionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ExecutionSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.executions = result;
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
            $scope.execution = {id: null};
        };
    });
