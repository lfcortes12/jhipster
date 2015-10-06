'use strict';

angular.module('arbitramentosApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('usuario', {
                parent: 'entity',
                url: '/usuarios',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'arbitramentosApp.usuario.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/usuario/usuarios.html',
                        controller: 'UsuarioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('usuario');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('usuario.detail', {
                parent: 'entity',
                url: '/usuario/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'arbitramentosApp.usuario.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/usuario/usuario-detail.html',
                        controller: 'UsuarioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('usuario');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Usuario', function($stateParams, Usuario) {
                        return Usuario.get({id : $stateParams.id});
                    }]
                }
            })
            .state('usuario.new', {
                parent: 'usuario',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/usuario/usuario-dialog.html',
                        controller: 'UsuarioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {email: null, password: null, username: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('usuario', null, { reload: true });
                    }, function() {
                        $state.go('usuario');
                    })
                }]
            })
            .state('usuario.edit', {
                parent: 'usuario',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/usuario/usuario-dialog.html',
                        controller: 'UsuarioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Usuario', function(Usuario) {
                                return Usuario.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('usuario', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
