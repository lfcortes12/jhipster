'use strict';

angular.module('arbitramentosApp')
    .factory('UsuarioSearch', function ($resource) {
        return $resource('api/_search/usuarios/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
