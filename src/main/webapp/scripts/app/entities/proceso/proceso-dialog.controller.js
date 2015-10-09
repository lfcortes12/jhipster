'use strict';

angular.module('arbitramentosApp').controller('ProcesoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Proceso',
        function($scope, $stateParams, $modalInstance, entity, Proceso) {

        $scope.proceso = entity;
        $scope.load = function(id) {
            Proceso.get({id : id}, function(result) {
                $scope.proceso = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('arbitramentosApp:procesoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.proceso.id != null) {
                Proceso.update($scope.proceso, onSaveFinished);
            } else {
                Proceso.save($scope.proceso, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
