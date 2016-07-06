var serverUrl = "doorbell";
app.factory('Users', ['$http', function ($http) {
    return {
        login: function (params) {
            return $http({
                method: 'POST',
                url: serverUrl + "/user/login",
                params: params
            });
        },
        logout: function () {
            return $http({
                method: 'POST',
                url: serverUrl + "/user/logout"
            });
        },
        current: function () {
            return $http({
                method: 'GET',
                url: serverUrl + "/user/current"
            });
        },
        changePassword: function (oldPassword, newPassword) {
            return $http({
                method: 'POST',
                url: serverUrl + "/user/changePassword",
                params: {oldPassword: oldPassword, newPassword: newPassword}
            });
        },
        listAll: function () {
            return $http({
                method: 'GET',
                url: serverUrl + "/user/listAll"
            });
        },
        delete: function (id) {
            return $http({
                method: 'POST',
                url: serverUrl + "/user/" + id + "/delete"
            });
        },
        create: function (name, password) {
            return $http({
                method: 'POST',
                url: serverUrl + "/user/add",
                params: {name: name, password: password}
            });
        },
        getPermissionListoUser: function (id) {
            return $http({
                method: 'GET',
                url: serverUrl + "/user/" + id + "/permission"
            });
        }
    };
}]);

app.factory('Profile', ['$http', function ($http) {
    return {
        listDevice: function () {
            return $http({
                method: 'GET',
                url: serverUrl + "/profile/listDevice"
            });
        },
        deviceInfos: function(data) {
            return $http({
                method: 'GET',
                url: serverUrl + "/profile/deviceInfos",
                params: data
            });
        },
        filterDevice: function(data) {
            return $http({
                method: 'GET',
                url: serverUrl + "/profile/filterDevice",
                params: data
            });
        },
        getCurrent: function (id) {
            return $http({
                method: 'GET',
                url: serverUrl + "/profile/current",
                params: {deviceId: id}
            });
        },
        getActionList: function (id) {
            return $http({
                method: 'GET',
                url: serverUrl + "/actions/list",
                params: {deviceId: id}
            });
        },
        cancel: function (profileId) {
            return $http({
                method: 'POST',
                url: serverUrl + "/profile/cancel",
                params: {profileId: profileId}
            });
        },
        revokeCancel: function (profileId) {
            return $http({
                method: 'POST',
                url: serverUrl + "/profile/revokeCancel",
                params: {profileId: profileId}
            });
        },
        syncResource: function (profileId) {
            return $http({
                method: 'GET',
                url: serverUrl + "/profile/syncResource",
                params: {deviceId: profileId}
            });
        }
    };
}]);

app.factory('Resource', ['$http', function ($http) {
    return {
        getList: function () {
            return $http({
                method: 'GET',
                url: serverUrl + "/resources/listAll"
            });
        },
        update: function (id, params) {
            return $http({
                method: 'POST',
                url: serverUrl + "/resources/" + id + "/change",
                params: params
            });
        },
        create: function (params) {
            return $http({
                method: 'POST',
                url: serverUrl + "/resources/add",
                params: params
            });
        },
        remove: function (id) {
            return $http({
                method: 'post',
                url: serverUrl + "/resources/" + id + "/delete"
            });
        }
    };
}]);

app.factory('Refund', ['$http', function ($http) {
    return {
        requestList: function (param) {
            return $http({
                method: 'GET',
                url: serverUrl + "/refund/request/list",
                params: param
            });
        },
        requestList2: function (param, pageInfo) {
            return $http({
                method: 'POST',
                url: serverUrl + "/refund/request/list2",
                params: param,
                data: pageInfo
            });
        },
        getByOrderNo: function (orderNo) {
            return $http({
                method: 'GET',
                url: serverUrl + "/refund/request/getByOrderNo",
                params: {orderNo: orderNo}
            });
        },
        approve: function (ids) {
            return $http({
                method: 'POST',
                url: serverUrl + "/refund/request/approve",
                params: {ids: ids}
            });
        },
        reject: function (requestIds, feedback) {
            return $http({
                method: 'POST',
                url: serverUrl + "/refund/request/reject",
                params: {feedback: feedback},
                data: requestIds
            });
        },
        postRequest: function (id, fee) {
            return $http({
                method: 'POST',
                url: serverUrl + "/refund/request/reApply",
                params: {requestId: id, fee: fee}
            });
        }
    };
}]);

app.factory('Order', ['$http', function ($http) {
    return {
        detail: function (orderNo) {
            return $http({
                method: 'GET',
                url: serverUrl + "/payment/order/detail",
                params: {orderNo: orderNo}
            });
        },
        list: function (deviceId) {
            return $http({
                method: 'GET',
                url: serverUrl + "/payment/order",
                params: {deviceId: deviceId}
            });
        },
        query: function (queryInfo,page) {
            return $http({
                method: 'POST',
                url: serverUrl + "/payment/order/query",
                params: page,
                data: queryInfo
            });
        },
        listAll: function (param) {
            return $http({
                method: 'GET',
                url: serverUrl + "/payment/order/list",
                params: param
            });
        },
        payDetail: function (orderNo) {
            return $http({
                method: 'GET',
                url: serverUrl + "/payment/detail",
                params: {orderNo: orderNo}
            });
        },
        aliPaymentLogDetail: function (logId) {
            return $http({
                method: 'GET',
                url: serverUrl + "/payment/detail/alipayLog",
                params: {logId: logId}
            });
        },
        aliRefundLogDetail: function (logId) {
            return $http({
                method: 'GET',
                url: serverUrl + "/refund/detail/alipayLog",
                params: {logId: logId}
            });
        },
        refundDetail: function (orderNo) {
            return $http({
                method: 'GET',
                url: serverUrl + "/refund/detail",
                params: {orderNo: orderNo}
            });
        },
        creat: function(params) {
            return $http({
                method: 'POST',
                url: serverUrl + "/orders/create",
                params: params
            });
        },
        buy: function(data) {
            return $http({
                method: 'POST',
                url: serverUrl + "/orders/console/buy",
                data: data
            });
        },
        signup: function(data) {
            return $http({
                method: 'POST',
                url: serverUrl + "/contracts/signup",
                data: data,
                params: {sign: '123456'}
            });
        },
        payList: function () {
            return $http({
                method: 'GET',
                url: serverUrl + "/payment/list"
            });
        },
        refundList: function () {
            return $http({
                method: 'GET',
                url: serverUrl + "/refund/list"
            });
        },
        listPaymentByTime: function (params) {
            return $http({
                method: 'POST',
                url: serverUrl + "/payment/list/byTime",
                params: params
            });
        },
        listRefundByTime: function (params) {
            return $http({
                method: 'POST',
                url: serverUrl + "/refund/list/byTime",
                params: params
            });
        },
    };
}]);

app.factory('AccountQuery', ['$http', function ($http) {
    return {
        query: function (pageNo, startTime, endTime) {
            return $http({
                method: 'POST',
                url: serverUrl + "/payment/accountquery",
                params: {pageNo: pageNo, startTime: startTime, endTime:endTime}
            });
        },
        queryByOrder: function (pageNo, orderNo) {
            return $http({
                method: 'POST',
                url: serverUrl + "/payment/accountquery/byOrder",
                params: {pageNo: pageNo, orderNo:orderNo}
            });
        },
    };
}]);

app.factory('Device', ['$http', function ($http) {
    return {
        detail: function (id) {
            return $http({
                method: 'GET',
                url: serverUrl + "/actions/detail",
                params: {actionId: id}
            });
        }
    };
}]);

app.factory('Permission', ['$http', function ($http) {
    return {
        grant: function (permissionId, instanceInfo) {
            return $http({
                method: 'POST',
                url: serverUrl + "/permission/grant",
                params: {
                    id: permissionId,
                    userId: instanceInfo
                }
            });
        },
        revoke: function (permId, userId) {
            return $http({
                method: 'POST',
                url: serverUrl + "/permission/revoke",
                params: {
                    userId: userId,
                    id: permId
                }
            });
        },
        listAll: function () {
            return $http({
                method: 'GET',
                url: serverUrl + "/permission/listAll"
            });
        },
        getUserByPermissionId: function (id) {
            return $http({
                method: 'GET',
                url: serverUrl + "/permission/" + id + "/user"
            });
        }
    };
}]);

app.factory('Summary', ['$http','$window','$filter', function ($http, $window,$filter) {
    var summaryQuery = function(param, url) {
        console.log(param);
        return $http({
            method: 'GET',
            url: serverUrl + "/report/" + url,
            params: param
        });
    };
    return {
        getSummaryResult: function (param) {
            var params = {
                startTime: $filter('date')(param.startTime, 'yyyy-MM-dd'),
                endTime: $filter('date')(param.endTime, 'yyyy-MM-dd'),
                timeUnit: param.timeUnit,
                topNum: param.topNum,
                type: param.type,
                commodityType: param.commodityType
            };
            var url = '';
            if (param.type === 'INOUT') {
                url = 'inout';
            } else if (param.type === 'ORDER') {
                url = 'order';
            } else if (param.type === 'byProduct') {
                url = 'commodity/byProduct';
            } else if (param.type === 'byTime') {
                url = 'commodity/byTime';
            }else if (param.type === 'CONSUMPTION') {
                url = 'consumption';
            }
            return summaryQuery(params, url);
        }
    };
}]);

app.filter('to_trusted', function($sce) {
    return function(text) {
        return $sce.trustAsHtml(text);
    }
});