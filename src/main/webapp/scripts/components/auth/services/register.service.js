'use strict';

angular.module('arbitramentosApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


