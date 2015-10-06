 'use strict';

angular.module('arbitramentosApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-arbitramentosApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-arbitramentosApp-params')});
                }
                return response;
            },
        };
    });