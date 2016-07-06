/**
 * Created by leo.liu on 2016/5/16.
 */
app.controller('AccountQueryController', ['$scope', '$filter', '$document', 'AccountQuery', 'Order',
    function ($scope, $filter, $document, AccountQuery, Order) {
        var vm = this;

        var pageNo = 1;
        var savedQueryDate = "";

        var load = vm.queryAccountPage = function (selectDate) {
            vm.errorOrders = [];
            vm.accountChecker = {payment: [], refund: []};
            vm.paymentChecker = {};
            vm.refundChecker = {};
            vm.loadingHint = '<span class="font-bold">正在搜索...</span>';
            savedQueryDate = selectDate;
            var date = new Date(selectDate);
            if (date instanceof Date && date != "Invalid Date") {
                var startTime = $filter('date')(date.setHours(0, 0, 0), 'yyyy-MM-dd HH:mm:ss');
                var endTime = $filter('date')(date.setHours(23, 59, 59), 'yyyy-MM-dd HH:mm:ss');

                vm.accountPage = [];
                AccountQuery.query(pageNo, startTime, endTime).success(function (response) {
                    vm.accountPage = response.data;
                    if (vm.accountPage.accountLogList.length <= 0) {
                        vm.loadingHint = '<span class="font-bold">今日暂无交易记录</span>';
                    }
                    angular.forEach(vm.accountPage.accountLogList, function (accountLog) {
                        var checker = accountLog.subTransCodeMsg.indexOf('快速支付') >= 0 ? vm.accountChecker.payment : vm.accountChecker.refund;
                        var num = accountLog.subTransCodeMsg.indexOf('快速支付') >= 0 ? accountLog.income : accountLog.outcome;
                        checker[accountLog.merchantOutOrderNo] = num;
                    });
                });

                vm.orders = [];
                Order.listRefundByTime({
                    startTime: startTime,
                    endTime: endTime,
                    page: 0,
                    limit: 5000,
                    sortBy: 'id',
                    order: 'asc'
                }).success(function (response) {
                    angular.forEach(response.data, function (refund) {
                        vm.orders.push({
                            orderNo: refund.orderNo,
                            title: '退款',
                            totalFee: refund.refundFee,
                            tradeNo: refund.tradeNo,
                            createTime: refund.createTime
                        });

                        vm.refundChecker[refund.orderNo] = refund.refundFee;

                    });
                });

                startTime = $filter('date')(date.setHours(0, 0, 0), 'yyyy-MM-dd');
                endTime = $filter('date')(date.setDate(date.getDate() + 1), 'yyyy-MM-dd');
                Order.listPaymentByTime({
                    startTime: startTime,
                    endTime: endTime,
                    page: 0,
                    limit: 5000,
                    sortBy: 'id',
                    order: 'asc'
                }).success(function (response) {
                    angular.forEach(response.data, function (payment) {
                        vm.orders.push({
                            orderNo: payment.orderNo,
                            title: '购买',
                            totalFee: payment.payFee,
                            tradeNo: payment.payNo,
                            createTime: payment.payTime
                        });

                        vm.paymentChecker[payment.orderNo] = payment.payFee;
                    });
                });
            }
        };

        vm.selectDate = new Date();
        load(vm.selectDate);

        vm.selectOrderNo = "";
        vm.changeSelection = function (orderNo) {
            if (orderNo != vm.selectOrderNo) {
                vm.selectOrderNo = orderNo;
            } else {
                vm.selectOrderNo = "";
            }
        };

        var scrollCbf = function (event) {
            if (event.target.id == "tableAccount") {
                if (angular.element("#tableOrder").scrollTop() != event.target.scrollTop) {
                    angular.element("#tableOrder").scrollTop(event.target.scrollTop);
                }
            } else if (event.target.id == "tableOrder") {
                if (angular.element("#tableAccount").scrollTop() != event.target.scrollTop) {
                    angular.element("#tableAccount").scrollTop(event.target.scrollTop);
                }
            }
        };

        angular.element("#tableAccount").scroll(scrollCbf);
        angular.element("#tableOrder").scroll(scrollCbf);

        vm.getPrevDay = function () {
            var date = new Date(savedQueryDate);
            load(date.setDate(date.getDate() - 1));
            vm.selectDate = date;
        };

        vm.getNextDay = function () {
            var date = new Date(savedQueryDate);
            load(date.setDate(date.getDate() + 1));
            vm.selectDate = date;
        };

        vm.checkDoorbellData = function (orderNo) {
            if (vm.accountPage.accountLogList == null) {
                return true;
            } else {
                if (vm.paymentChecker[orderNo] == vm.accountChecker.payment[orderNo]
                    && vm.refundChecker[orderNo] == vm.accountChecker.refund[orderNo]) {
                    return true;
                }
                data.isError = true;
                return false;
            }
        };

        vm.checkAliData = function (orderNo) {
            if (vm.orders.length <= 0) {
                return true;
            } else {
                if (vm.paymentChecker[orderNo] == vm.accountChecker.payment[orderNo]
                    && vm.refundChecker[orderNo] == vm.accountChecker.refund[orderNo]) {
                    return true;
                }
                data.isError = true;
                return false;
            }
        };

        vm.viewDetail = function (orderNo) {
            vm.selectOrderDetails = [];
            Order.payDetail(orderNo).success(function (response) {
                var payment = response.data;
                vm.selectOrderDetails.push({
                    orderNo: payment.orderNo,
                    title: '购买',
                    totalFee: payment.payFee,
                    tradeNo: payment.payNo,
                    createTime: payment.payTime
                });
            });
            Order.refundDetail(orderNo).success(function (response) {
                var refund = response.data;
                vm.selectOrderDetails.push({
                    orderNo: orderNo,
                    title: '退款',
                    totalFee: refund.refundFee,
                    tradeNo: refund.tradeNo,
                    createTime: refund.createTime
                });
            });
            AccountQuery.queryByOrder(1, orderNo).success(function (response) {
                vm.selectAccountPage = response.data;
            });
        };
    }]);