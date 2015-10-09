'use strict';

angular.module('arbitramentosApp')
    .controller('ProcesoController', function ($scope, Proceso, ProcesoSearch, ParseLinks, ProcesoAprobar) {
        $scope.procesos = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Proceso.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.procesos = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Proceso.get({id: id}, function(result) {
                $scope.proceso = result;
                $('#deleteProcesoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Proceso.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteProcesoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ProcesoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.procesos = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };
        
        $scope.aprobar = function (id) {
        	ProcesoAprobar.aprobar({id: id}, function(result) {
                $scope.procesos = result;
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
            $scope.proceso = {descripcionHechos: null, estadoActualProceso: null, fechaHechos: null, hechos: null, id: null};
        };
    });
