'use strict';

angular.module('arbitramentosApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('proceso', {
                parent: 'entity',
                url: '/procesos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'arbitramentosApp.proceso.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/proceso/procesos.html',
                        controller: 'ProcesoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('proceso');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('proceso.detail', {
                parent: 'entity',
                url: '/proceso/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'arbitramentosApp.proceso.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/proceso/proceso-detail.html',
                        controller: 'ProcesoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('proceso');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Proceso', function($stateParams, Proceso) {
                        return Proceso.get({id : $stateParams.id});
                    }]
                }
            })
            .state('proceso.new', {
                parent: 'proceso',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/proceso/proceso-dialog.html',
                        controller: 'ProcesoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {descripcionHechos: null, estadoActualProceso: null, fechaHechos: null, hechos: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('proceso', null, { reload: true });
                    }, function() {
                        $state.go('proceso');
                    })
                }]
            })
            .state('proceso.edit', {
                parent: 'proceso',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/proceso/proceso-dialog.html',
                        controller: 'ProcesoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Proceso', function(Proceso) {
                                return Proceso.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('proceso', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
