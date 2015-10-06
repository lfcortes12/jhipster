'use strict';

angular.module('arbitramentosApp')
    .controller('UsuarioController', function ($scope, Usuario, UsuarioSearch, ParseLinks) {
        $scope.usuarios = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Usuario.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.usuarios = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Usuario.get({id: id}, function(result) {
                $scope.usuario = result;
                $('#deleteUsuarioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Usuario.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUsuarioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            UsuarioSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.usuarios = result;
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
            $scope.usuario = {email: null, password: null, username: null, id: null};
        };
    });
