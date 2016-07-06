'use strict';

/**
 * Config for the router
 */
angular.module('app')
    .run(
    ['$rootScope', '$state', '$stateParams',
        function ($rootScope, $state, $stateParams) {
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
        }
    ]
)
    .config(
    ['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {

            $urlRouterProvider
                .otherwise('/access/signin');
            $stateProvider
                .state('app', {
                    abstract: true,
                    url: '/app',
                    templateUrl: 'views/app.html'
                })
                .state('access', {
                    url: '/access',
                    template: '<div ui-view class="fade-in-right-big smooth"></div>'
                })
                //signin
                .state('access.signin', {
                    url: '/signin',
                    templateUrl: 'views/page_signin.html'
                })
                //admin entries
                .state('app.admin_users', {
                    url: '/admin_users',
                    templateUrl: 'views/app_admin_users.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load('js/controllers/user.js');
                            }]
                    }
                })
                .state('app.permission', {
                    url: '/permission',
                    templateUrl: 'views/app_permission.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load('js/controllers/permission.js');
                            }]
                    }
                })
                .state('app.purchase_packages', {
                    url: 'purchase_packages/{id}',
                    templateUrl: 'views/app_purchase_packages.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load('js/controllers/devices.js');
                            }]
                    }
                })
                .state('app.summary_mod', {
                    url: '/summary/3',
                    templateUrl: 'views/app_summary_mod.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load('js/controllers/summary.js');
                            }]
                    }
                })
                .state('app.summary', {
                    url: '/summary/{id}',
                    templateUrl: 'views/app_summary.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load('js/controllers/summary.js');
                            }]
                    }
                })
                .state('app.refund', {
                    url: '/refund',
                    templateUrl: 'views/app_refund.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load('js/controllers/refund.js');
                            }]
                    }
                })
                .state('app.devices', {
                    url: '/devices',
                    templateUrl: 'views/app_devices.html',
                    params: {'data': null},
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load(['js/controllers/devices.js']);
                            }]
                    }
                })
                .state('app.devices1', {
                    url: '/devices1',
                    templateUrl: 'views/app_devices1.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load(['js/controllers/devices.js']);
                            }]
                    }
                })
                .state('app.devices_detail', {
                    url: '/devices/detail/{id}:{name}',
                    templateUrl: 'views/app_devices_detail.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load(['js/controllers/devices.js']);
                            }]
                    }
                })
                .state('app.devices_order', {
                    url: '/devices/order/{id}:{name}',
                    //params: {data: null},
                    templateUrl: 'views/app_devices_order.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load(['js/controllers/devices.js']);
                            }]
                    }
                })
                .state('app.account_query', {
                    url: '/account_query',
                    templateUrl: 'views/app_account_query.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load(['js/controllers/accountQuery.js']);
                            }]
                    }
                })
                .state('app.order_info', {
                    url: '/order_info',
                    templateUrl: 'views/app_order_info.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load(['js/controllers/order.js']);
                            }]
                    }
                })
                .state('app.order_detail', {
                    url: '/order_detail/{orderNo}',
                    templateUrl: 'views/app_order_detail.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load(['js/controllers/order.js']);
                            }]
                    }
                })
                .state('app.resource_list', {
                    url: '/resource_list',
                    templateUrl: 'views/app_resource_list.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load(['js/controllers/resource.js']);
                            }]
                    }
                })
                .state('app.user_info', {
                    url: '/user_info',
                    templateUrl: 'views/app_user_info.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load('js/controllers/user.js');
                            }]
                    }
                })
                .state('access.modifypasswd', {
                    url: '/modifypasswd',
                    templateUrl: 'views/page_password.html',
                    resolve: {
                        deps: ['$ocLazyLoad',
                            function ($ocLazyLoad) {
                                return $ocLazyLoad.load(['js/controllers/password.js']);
                            }]
                    }
                })
        }
    ]
);