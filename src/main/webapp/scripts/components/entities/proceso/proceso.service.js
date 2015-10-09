'use strict';

angular.module('arbitramentosApp')
    .factory('Proceso', function ($resource, DateUtils) {
        return $resource('api/procesos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.fechaHechos = DateUtils.convertLocaleDateFromServer(data.fechaHechos);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.fechaHechos = DateUtils.convertLocaleDateToServer(data.fechaHechos);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.fechaHechos = DateUtils.convertLocaleDateToServer(data.fechaHechos);
                    return angular.toJson(data);
                }
            }
        });
    });
