'use strict';

angular.module('arbitramentosApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
