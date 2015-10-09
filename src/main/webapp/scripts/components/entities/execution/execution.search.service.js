'use strict';

angular.module('arbitramentosApp')
    .factory('ExecutionSearch', function ($resource) {
        return $resource('api/_search/executions/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
