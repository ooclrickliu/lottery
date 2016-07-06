'use strict';

angular.module('app')
    .controller('AppCtrl', ['$scope', '$window', '$state', 'Users',
        function ($scope, $window, $state, Users) {
            // add 'ie' classes to html
            var isIE = !!navigator.userAgent.match(/MSIE/i);
            isIE && angular.element($window.document.body).addClass('ie');
            isSmartDevice($window) && angular.element($window.document.body).addClass('smart');

            $scope.app = {
                name: 'DoorBell支付平台登录',
                version: '1.0.0',
                settings: {
                    headerFixed: true,
                    asideFixed: true,
                    asideFolded: false,
                    asideDock: false,
                    container: false
                }
            }

            $scope.app.devices = [];
            $scope.app.selectDeviceId = 0;
            $scope.app.selectRefundStateId = 0;
            $scope.app.orderDetail =[];
            $scope.app.refundDetail =[];
            $scope.app.userInfo = null;
            $scope.app.userId  = null;
            $scope.showNavList = [0, 0, 0, 0, 0, 0];
            $scope.app.clientWidth = $window.document.body.clientWidth;
            $scope.permissions = null;

            if ($window.location.hash.indexOf("app") != -1) {
                if ($scope.app.userInfo == null) {
                    Users.current().then(function (response) {
                        initProcess(response.data.data);
                    });
                }
            }

            $scope.login = function (params) {
                $scope.authError = null;
                Users.login(params).then(function (response) {
                    if (response.data.stateCode == "SUCCESS") {
                        initProcess(response.data.data);
                        $state.go('app.user_info');
                    }
                });
            };

            $scope.logout = function () {
                Users.logout().then(function (response) {
                    if (response.data.stateCode == 'SUCCESS') {
                        $window.location.href = '/index.html';
                    }
                });
            }

            var initProcess = function (userInfo) {
                $scope.app.userInfo = userInfo;
                $scope.permissions = userInfo.permission;
                $scope.permissionList = new Array();
                for (var i = 0; i < $scope.permissions.length; i++) {
                    $scope.permissionList.push($scope.permissions[i].permissionCode);
                }
                processPermission($scope.permissionList);
            }

            var processPermission = function (permissionList) {
                $scope.showNavList = [0, 0, 0, 0, 0, 0];
                if ($scope.permissionList.indexOf('SA') >= 0) {
                    $scope.showNavList = [1, 1, 1, 1, 1, 1];
                }

                if ($scope.permissionList.indexOf('REFUND') >= 0) {
                    $scope.showNavList[2] = 1;
                }

                if ($scope.permissionList.indexOf('TRANSFER') >= 0) {
                    $scope.showNavList[3] = 1;
                }

                if ($scope.permissionList.indexOf('RESOURCE') >= 0) {
                    $scope.showNavList[4] = 1;
                }

                if ($scope.permissionList.indexOf('PROFILE') >= 0) {
                    $scope.showNavList[5] = 1;
                }
            }

            $scope.getTradeType = function (type) {
                switch(type) {
                    case 'PURCHASE' :
                        return '购买';
                    case 'CANCEL' :
                        return '退订';
                    case 'REFUSE_CANCEL' :
                        return '拒绝退订';
                }
            }

            $scope.getOrderState = function (type) {
                switch(type) {
                    case 'ORDER_PAID' :
                        return '已支付';
                    case 'ORDER_CANCELED':
                        return '已取消';
                    case 'REFUND_APPLYING' :
                        return '申请中';
                    case 'REFUND_DOING' :
                        return '处理中';
                    case 'REFUND_REFUSED' :
                        return '已拒绝';
                    case 'REFUND_DONE' :
                        return '已成功';
                }
            }

            function isSmartDevice($window) {
                var ua = $window['navigator']['userAgent'] || $window['navigator']['js'] || $window['opera'];
                return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
            }
        }]);