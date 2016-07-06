// config
var app =
    angular.module('app')
        .config(
        ['$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
            function ($controllerProvider, $compileProvider, $filterProvider, $provide) {
                app.controller = $controllerProvider.register;
                app.directive = $compileProvider.directive;
                app.filter = $filterProvider.register;
                app.factory = $provide.factory;
                app.service = $provide.service;
                app.constant = $provide.constant;
                app.value = $provide.value;
            }
        ])
        .config(['$httpProvider', function ($httpProvider) {
            $httpProvider.interceptors.push('responseRejector');
        }])
        .run(['$rootScope', '$http', function ($rootScope, $http) {
            $rootScope.errCodeMap = {};
            $http.get('resource/json/DoorbellErrorCode.json').success(function (response) {
                angular.forEach(response, function (errCode) {
                    $rootScope.errCodeMap[errCode.name] = errCode.description;
                });
            });

            $http.get('resource/json/PaymentErrorCode.json').success(function (response) {
                angular.forEach(response, function (errCode) {
                    $rootScope.errCodeMap[errCode.name] = errCode.description;
                });
            });

            $http.get('resource/json/AliRefundErrorCode.json').success(function (response) {
                angular.forEach(response, function (errCode) {
                    $rootScope.errCodeMap[errCode.name] = errCode.description;
                });
            });
        }]);