'use strict';

angular.module('arbitramentosApp').controller('ExecutionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Execution',
        function($scope, $stateParams, $modalInstance, entity, Execution) {

        $scope.execution = entity;
        $scope.load = function(id) {
            Execution.get({id : id}, function(result) {
                $scope.execution = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('arbitramentosApp:executionUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.execution.id != null) {
                Execution.update($scope.execution, onSaveFinished);
            } else {
                Execution.save($scope.execution, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
