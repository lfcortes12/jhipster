'use strict';

angular.module('arbitramentosApp')
    .factory('Usuario', function ($resource, DateUtils) {
        return $resource('api/usuarios/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
