'use strict';

angular.module('arbitramentosApp')
    .factory('ProcesoAprobar', function ($resource) {
        return $resource('api/procesos/aprobar/:id', {}, {
            'aprobar': { method: 'GET', isArray: true}
        });
    });
