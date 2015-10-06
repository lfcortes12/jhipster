'use strict';

angular.module('arbitramentosApp')
    .controller('UsuarioDetailController', function ($scope, $rootScope, $stateParams, entity, Usuario) {
        $scope.usuario = entity;
        $scope.load = function (id) {
            Usuario.get({id: id}, function(result) {
                $scope.usuario = result;
            });
        };
        $rootScope.$on('arbitramentosApp:usuarioUpdate', function(event, result) {
            $scope.usuario = result;
        });
    });
