app.controller('OrderController', ['$scope', 'Order', '$state', '$filter', function ($scope, Order, $state, $filter) {
    $scope.searchTypes = ['订单号', '设备名', '购买人'];
    $scope.selectSearchType = $scope.searchTypes[0];
    $scope.orderStatesMap = {
        'ORDER_PAID': '已付款',
        'ORDER_WAIT_PAY': '待付款'
    };
    $scope.orderStates = ['ORDER_PAID', 'ORDER_WAIT_PAY'];
    $scope.selectedOrderState = "";
    $scope.showedOrders = [];

    $scope.pageInfo = {
        page: 0,
        limit: 12,
        sortBy: 'id',
        order: 'desc'
    };

    $scope.loadingHint = '<span class="font-bold">正在加载...</span>';

    $scope.changeSearchType = function (searchType) {
        $scope.selectSearchType = searchType;
    };

    $scope.searchOrders = function (query, startTime, endTime, orderState) {
        $scope.loadingHint = '<span class="font-bold">正在搜索...</span>';
        $scope.showedOrders = [];

        if (startTime && endTime) {
            query = query || {};
            query['startTime'] = "\'" + $filter('date')(startTime.setHours(0, 0, 0), 'yyyy-MM-dd HH:mm:ss') + "\'";
            query['endTime'] = "\'" + $filter('date')(endTime.setHours(23, 59, 59), 'yyyy-MM-dd HH:mm:ss') + "\'";
        }

        query = query || {};
        angular.element("#search").attr('disabled', true);
        angular.forEach(query, function (value, key) {
            if (!value || value == "") {
                delete query[key];
            }
        });

        query['order_state'] = "\'" + $scope.orderStates[orderState] + "\'";
        $scope.queryState = query['order_state'].replace(new RegExp('\'','gm'),'');
        $scope.pageInfo = {
            page: 0,
            limit: 12,
            sortBy: 'id',
            order: 'desc'
        };
        $scope.query = query;

            Order.query(query, $scope.pageInfo).then(function (response) {
                if (response.data.data.length <= 0) {
                    $scope.loadingHint = '<span>暂无 ';
                    if (query['order_state']) {
                        $scope.loadingHint += '<span class="font-bold">' + '状态' + ' 为 ' + $scope.orderStatesMap[$scope.queryState] + '</span>';
                    }
                    if (query['order_no']) {
                        $scope.loadingHint += '<span class="font-bold">' + '订单号' + ' 为 ' + query['order_no'] + '</span>';
                    }
                    if (query['user_id']) {
                        $scope.loadingHint += '<span class="font-bold">' + '设备号' + ' 为 ' + query['user_id'] + '</span>';
                    }
                    if (query['create_by']) {
                        $scope.loadingHint += '<span class="font-bold">' + '购买人' + ' 为 ' + query['create_by'] + '</span>';
                    }
                    if (query['startTime']) {
                        $scope.loadingHint += '<span class="font-bold">' + '从' + query['startTime']
                        + ' 到 ' + query['endTime'] ? query['startTime'] : '至今' + '</span>';
                    }
                    $scope.loadingHint += ' 的订单</span>';
                } else {
                    $scope.showedOrders = response.data.data;
                    $scope.selectedOrderState = $scope.showedOrders[0].orderState;
                }

                angular.element("#search").attr('disabled', false);
            });
    };

    $scope.sortBy = function (sortBy) {
        if (sortBy != $scope.pageInfo.sortBy) {
            $scope.pageInfo.sortBy = sortBy;
            $scope.pageInfo.order = 'desc';
        } else {
            $scope.pageInfo.order = $scope.pageInfo.order == 'desc' ? 'asc' : 'desc';
        }
        init($scope.selectedOrderState);
    };

    $scope.showSortIcon = function (sortInfo) {
        return sortInfo != $scope.pageInfo.sortBy ? 'fa-sort' : 'fa-sort-' + $scope.pageInfo.order;
    };

    $scope.getOrderListByPage = function (action) {
        angular.element("#" + action).attr('disabled', true);
        var pageNo = 0;
        if (action === 'prev') {
            pageNo = $scope.pageInfo.page - 1;
        } else if (action === 'next') {
            pageNo = $scope.pageInfo.page + 1;
        }
        var query = $scope.query || {};
        Order.query(query, {
            orderState: $scope.selectedOrderState,
            page: pageNo,
            limit: $scope.pageInfo.limit,
            sortBy: $scope.pageInfo.sortBy,
            order: $scope.pageInfo.order
        }).then(function (response) {
            if (response.data.data.length > 0) {
                $scope.pageInfo.page = pageNo;
                $scope.showedOrders = response.data.data;
            }
            angular.element("#" + action).attr('disabled', false);
        });
    };

    $scope.showDeviceDetail = function (deviceId) {
        $scope.app.selectDeviceId = deviceId;
        console.log(deviceId)
        $state.go('app.devices',{data:deviceId});
    };

    var init = $scope.selectOrderState = function (orderState) {
        Order.listAll({
            orderState: orderState,
            page: $scope.pageInfo.page,
            limit: $scope.pageInfo.limit,
            sortBy: $scope.pageInfo.sortBy,
            order: $scope.pageInfo.order
        }).success(function (response) {
            if (response.data.length <= 0) {
                $scope.loadingHint = '<span>暂无 <span class="font-bold">'+ $scope.orderStatesMap[orderState] +'</span> 的订单</span>';
            }
            $scope.showedOrders = response.data;
            $scope.selectedOrderState = orderState;
        });
    };

    init($scope.orderStates[0]);
}]);

app.controller('OrderDetailController', ['$scope', 'Order', 'Refund', '$stateParams', '$filter',
    function ($scope, Order, Refund, $stateParams, $filter) {
        $scope.orderStates = ['已付款'];
        $scope.selectedOrderState = $scope.orderStates[0];

        Order.detail($stateParams.orderNo).success(function (response) {
            $scope.orderDetail = response.data;
            console.log($scope.orderDetail);
            $scope.orderItems = $scope.orderDetail.orderItemList;
            $scope.paidGoodsInfo = getGoodsInfo($scope.orderDetail.orderRemark);
            //console.log($scope.paidGoodsInfo.TIME.start);
            //$scope.paidGoodsInfo.start = new Date(parseInt($scope.paidGoodsInfo.TIME.start)*1000);
            //console.log(new Date().getTime());
            //console.log($scope.paidGoodsInfo.total);
            if ($scope.orderDetail.orderState == 'ORDER_PAID') {
                Order.payDetail($stateParams.orderNo).success(function (response) {
                    $scope.payDetail = response.data;
                });
            }

            if ($scope.orderDetail.refundedFee > 0) {
                Refund.getByOrderNo($stateParams.orderNo).success(function (response) {
                    console.log(response.data[0]);
                    $scope.refundGoodsInfo = getGoodsInfo(response.data[0]);
                });
                Order.refundDetail($stateParams.orderNo).success(function (response) {
                    $scope.refundDetail = response.data;
                });
            }
        });

        $scope.getPaymentLogDetail = function (payLogId) {
            Order.aliPaymentLogDetail(payLogId).success(function (response) {
                $scope.paymentLogDetail = response.data;
            });
        };

        $scope.getRefundLogDetail = function (refundLogId) {
            Order.aliRefundLogDetail(refundLogId).success(function (response) {
                $scope.refundLogDetail = response.data;
            });
        };

        var getGoodsInfo = function (requestDetail) {
            console.log(requestDetail);
            var goodsInfo = {};
            // && requestDetail.refundDesc != null
            if (requestDetail != null) {
                var params = requestDetail.split(';');
                for (var index in params) {
                    var values = params[index].split(' ');
                    goodsInfo[values[0]] = values[1];
                }
                if (goodsInfo['TIME'] != null) {
                    var values = goodsInfo['TIME'].split('~');
                    goodsInfo['TIME'] = {
                        start: values[0],
                        end: values[1]
                    }
                }
            }
            return goodsInfo;
        }
    }]);
