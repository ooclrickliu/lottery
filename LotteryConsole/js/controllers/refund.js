app.controller('RefundController', ['$scope', '$filter', 'Refund', function ($scope, $filter, Refund) {
    $scope.requestStatesMap = {
        'REFUND_APPLYING': '待退款',
        'REFUND_DOING': '处理中',
        'REFUND_DONE': '已退款',
        'REFUND_REFUSED': '已拒绝',
        'REFUND_FAILED': '退款失败'
    };
    $scope.requestStates = ['REFUND_APPLYING', 'REFUND_DOING', 'REFUND_DONE', 'REFUND_REFUSED', 'REFUND_FAILED'];
    $scope.selectedReqeustState = "";
    $scope.showedRefundRequests = [];
    $scope.havaSelected = false;

    $scope.querySaved = {};

    var initPageInfo = {
        pageNo: 0,
        pageSize: 12,
        sortBy: 'id',
        order: 'DESC'
    };

    $scope.pageInfo = angular.copy(initPageInfo);

    var init = $scope.queryRefundRequest = function (query) {
        $scope.pageInfo = angular.copy(initPageInfo);
        var param = {};
        if (query.orderNo !== undefined && query.orderNo !== "") {
            param.orderNo = query.orderNo;
        } else {
            param.requestState = query.requestState || $scope.selectedReqeustState;
            param.requestState = "\'" + param.requestState + "\'";
            if (query.startTime instanceof Date && query.startTime != "Invalid Date"
                && query.endTime instanceof Date && query.endTime != "Invalid Date") {
                param.startTime = "\'" + $filter('date')(query.startTime.setHours(0, 0, 0), 'yyyy-MM-dd HH:mm:ss') + "\'";
                param.endTime = "\'" + $filter('date')(query.endTime.setHours(23, 59, 59), 'yyyy-MM-dd HH:mm:ss') + "\'";
            }
        }
        $scope.querySaved = angular.copy(param);
        Refund.requestList2(param, $scope.pageInfo)
            .success(function (response) {
                $scope.showedRefundRequests = response.data;
                $scope.selectedReqeustState = ($scope.querySaved.requestState).replace(new RegExp('\'','gm'),'');
                // $scope.selectedReqeustState = response.data[0].refundState;
                $scope.app.selectRefundStateId = $scope.requestStates.indexOf($scope.selectedReqeustState);
                $scope.havaSelected = $scope.selectAll = false;
                angular.forEach($scope.showedRefundRequests, function (request) {
                    request.selected = false;
                });
            });
    };

    init({requestState: $scope.requestStates[$scope.app.selectRefundStateId]});

    $scope.showRefundDialog = function () {
        if ($scope.selectedReqeustState == $scope.requestStates[0]) {
            var refundRequestIds = [];
            $scope.refundRequestIds = "";
            $scope.refundRequestOrderNos = [];
            angular.forEach($scope.showedRefundRequests, function (request) {
                if (request.selected == true) {
                    refundRequestIds.push(request.id);
                    $scope.refundRequestOrderNos.push(request.orderNo);
                }
            });

            for (var i = 0; i < refundRequestIds.length; i++) {
                if (i != refundRequestIds.length - 1) {
                    $scope.refundRequestIds += refundRequestIds[i] + ",";
                } else {
                    $scope.refundRequestIds += refundRequestIds[i];
                }
            }
            if ($scope.refundRequestIds != "") {
                jQuery('#approveModal').modal();
            }

        } else if ($scope.selectedReqeustState == $scope.requestStates[1]) {
            var batchNo = "";
            var refundRequestIds = [];
            $scope.refundRequestIds = "";
            $scope.refundRequestOrderNos = [];
            angular.forEach($scope.showedRefundRequests, function (request) {
                //获取选中的退款请求
                if (request.selected == true) {
                    //如果选中的退款请求中有batchNo没有值，则取消此次操作
                    if (request.batchNo == null || request.batchNo == "") {
                        return;
                    }
                    //bacthNo获取第一个退款请求的batchNo值
                    if (batchNo == "") {
                        batchNo = request.batchNo;
                    } else {
                        //检测其他选中的退款请求如果与第一个退款请求不一致，则取消操作
                        if (batchNo != request.batchNo) {
                            toast('error', '', "无法一次操作不同批次号退款请求", null);
                            request.selected = false;
                            batchNo = "";
                            return;
                        }
                    }
                    refundRequestIds.push(request.id);
                    $scope.refundRequestOrderNos.push(request.orderNo);
                }
            });

            for (var i = 0; i < refundRequestIds.length; i++) {
                if (i != refundRequestIds.length - 1) {
                    $scope.refundRequestIds += refundRequestIds[i] + ",";
                } else {
                    $scope.refundRequestIds += refundRequestIds[i];
                }
            }

            if ($scope.refundRequestIds != "") {
                jQuery('#approveRepostModal').modal();
            }
        }
    };

    $scope.postApproveRefund = function () {
        jQuery("form[name='alipayment']")[0].action = "/doorbell/refund/request/approve?clientType=web";
        jQuery("form[name='alipayment']")[0].submit();
        init($scope.requestStates[2]);
        jQuery('#approveModal').modal('hide');
    };

    $scope.repostRefund = function () {
        jQuery("form[name='alipayment2']")[0].action = "/doorbell/refund/request/approve/repost?clientType=web";
        jQuery("form[name='alipayment2']")[0].submit();
        init($scope.requestStates[2]);
        jQuery('#approveRepostModal').modal('hide');
    }

    $scope.rejectRefund = function (feedback, valid) {
        if (valid && $scope.selectedReqeustState == $scope.requestStates[0]) {
            var refundRequestIds = [];
            angular.forEach($scope.showedRefundRequests, function (request) {
                if (request.selected) {
                    refundRequestIds.push(request.id);
                }
            });
            jQuery('#rejectModal').modal('hide');
            Refund.reject(refundRequestIds, feedback).success(function () {
                init($scope.requestStates[0]);
            });
        }
    };

    $scope.modifyRequest = function (request) {
        $scope.selectedModifyRequest = request;
    };

    $scope.postRefundRequest = function (newRefundFee) {
        if ($scope.selectedModifyRequest.refundFee >= newRefundFee) {
            Refund.postRequest($scope.selectedModifyRequest.id, newRefundFee).success(function () {
                init($scope.requestStates[0]);
                jQuery('#refundRequestModal').modal('hide');
            });
        } else {
            toast('error', '', "退款金额无法超过原退款金额", null);
        }
    };

    $scope.changeSelection = function (refundRequest) {
        if (refundRequest != null) {
            refundRequest.selected = !refundRequest.selected;
        }
        var allState = false;
        angular.forEach($scope.showedRefundRequests, function (request) {
            allState |= request.selected;
        });
        $scope.havaSelected = allState;
        $scope.selectAll = $scope.havaSelected ? $scope.selectAll : false;
    };
    $scope.changeSelectionAll = function (requests) {
        angular.forEach(requests.data, function (request) {
            request.selected = $scope.selectAll;
        });
        $scope.havaSelected = $scope.selectAll;
    };


    $scope.getRefundRequestListByPage = function (action) {
        var pageInfo = angular.copy($scope.pageInfo);
        if (action === 'prev') {
            pageInfo.pageNo -= 1;
        } else if (action === 'next') {
            pageInfo.pageNo += 1;
        }

        Refund.requestList2($scope.querySaved, pageInfo)
            .success(function (response) {
                if (response.data.length > 0) {
                    $scope.pageInfo.pageNo = pageInfo.pageNo;
                    $scope.havaSelected = $scope.selectAll = false;
                    $scope.showedRefundRequests = response.data;
                }
            });
    };
}]);
