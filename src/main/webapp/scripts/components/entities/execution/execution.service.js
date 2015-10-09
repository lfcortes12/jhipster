'use strict';

angular.module('arbitramentosApp')
    .factory('Execution', function ($resource, DateUtils) {
        return $resource('api/executions/:id', {}, {
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
