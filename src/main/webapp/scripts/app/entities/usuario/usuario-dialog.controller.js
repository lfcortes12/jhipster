'use strict';

angular.module('arbitramentosApp').controller('UsuarioDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Usuario',
        function($scope, $stateParams, $modalInstance, entity, Usuario) {

        $scope.usuario = entity;
        $scope.load = function(id) {
            Usuario.get({id : id}, function(result) {
                $scope.usuario = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('arbitramentosApp:usuarioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.usuario.id != null) {
                Usuario.update($scope.usuario, onSaveFinished);
            } else {
                Usuario.save($scope.usuario, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
