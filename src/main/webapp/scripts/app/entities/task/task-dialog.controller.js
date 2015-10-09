'use strict';

angular.module('arbitramentosApp').controller('TaskDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Task',
        function($scope, $stateParams, $modalInstance, entity, Task) {

        $scope.task = entity;
        $scope.load = function(id) {
            Task.get({id : id}, function(result) {
                $scope.task = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('arbitramentosApp:taskUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.task.id != null) {
                Task.update($scope.task, onSaveFinished);
            } else {
                Task.save($scope.task, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
