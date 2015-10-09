'use strict';

angular.module('arbitramentosApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('execution', {
                parent: 'entity',
                url: '/executions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'arbitramentosApp.execution.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/execution/executions.html',
                        controller: 'ExecutionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('execution');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('execution.detail', {
                parent: 'entity',
                url: '/execution/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'arbitramentosApp.execution.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/execution/execution-detail.html',
                        controller: 'ExecutionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('execution');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Execution', function($stateParams, Execution) {
                        return Execution.get({id : $stateParams.id});
                    }]
                }
            })
            .state('execution.new', {
                parent: 'execution',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/execution/execution-dialog.html',
                        controller: 'ExecutionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('execution', null, { reload: true });
                    }, function() {
                        $state.go('execution');
                    })
                }]
            })
            .state('execution.edit', {
                parent: 'execution',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/execution/execution-dialog.html',
                        controller: 'ExecutionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Execution', function(Execution) {
                                return Execution.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('execution', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
