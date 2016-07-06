/**
 * Created by jinzhong.zheng on 2016/2/23.
 */
testCase.controller('testController', ['$scope', 'testService',
    function ($scope, testService) {
        $scope.deviceId = '10010';
        $scope.clientType = 'ios';
        $scope.token = '2983kdjfjdf94jjklfsladkf';
        $scope.revoke = false;
        var paramUrl;
        testService.getApiList().then(
            function (response) {
                $scope.Apis = response.data;
            }
        );

        $scope.runFlow = function () {
            $scope.status = null;
            $scope.createOrder = null;
            $scope.pay = null;
            $scope.refund = null;
            $scope.revokeRefund = null;
            $scope.reRefund = null;
            $scope.refundSuccess = null;
            $scope.actionList = null;
            $scope.actionDetail = null;
            var deviceId = $scope.deviceId;
            var clientType = $scope.clientType;
            var token = $scope.token;
            paramUrl = '?clientType=' + clientType
                + '&token=' + token
                + '&deviceId=' + deviceId;

            getDeviceStatus();
        };

        function statusAfterRefundSucc(){
            var url = "doorbell/profile/current" + paramUrl;
            var promise = testService.runTest('GET', url);
            promise.then(function (response) {
                var data = response.data.items.PURCHASE;
                if(data[0].actionStatus == 'NONCANCEABLE'){
                    $scope.refundSuccess = 'SUCCESS';
                    getActionList();
                }

            });
        };

        function statusAfterRefund(){
            var url = "doorbell/profile/current" + paramUrl;
            var promise = testService.runTest('GET', url);
            promise.then(function (response) {
                var data = response.data.items.PURCHASE;
                if(data[0].actionStatus == 'REVOCABLE'){

                    if($scope.revoke){
                        $scope.reRefund = 'SUCCESS';
                        getRefundRequestList();
                        $scope.revoke = false;
                    }else{
                        $scope.refund = 'SUCCESS';
                        revokeRefund();
                    }
                }

            });
        };

        function statusAfterRevokeRefund(){
            var url = "doorbell/profile/current" + paramUrl;
            var promise = testService.runTest('GET', url);
            promise.then(function (response) {
                var data = response.data.items.PURCHASE;

                if(data[0].actionStatus == 'CANCELABLE'){
                    $scope.revokeRefund = 'SUCCESS';
                    refund();
                }
            });
        };

        function deviceStatusAfterPay(){
            var url = "doorbell/profile/current" + paramUrl;
            var promise = testService.runTest('GET', url);
            promise.then(function (response) {
                var data = response.data;
                var purchase = data.items.PURCHASE;

                if(purchase.length == 1){
                    $scope.pay = 'SUCCESS';
                    $scope.profileId = purchase[(purchase.length - 1)].id;
                    refund();
                }
            });
        };

        function getDeviceStatus() {
            var url = "doorbell/profile/current" + paramUrl;
            var promise = testService.runTest('GET', url);
            promise.then(function (response) {
                var data = response.data;

                if(data.items.PURCHASE == null){
                    $scope.status = response.stateCode;
                    createOrder();
                }else{
                    $scope.status = 'FAILED';
                }

            });
        };

        function createOrder() {
            var param = {orderNo: '', deviceId: $scope.deviceId, month: '3', totalFee: '0',
                space: 1*1024*1024, flow: 4*60*60, activateType: 'now'};
            var url = "doorbell/orders/create" + paramUrl;
            var promise = testService.runTest('POST', url, param);
            promise.then(function (response) {
                $scope.orderNo = response.data['orderNo'];
                $scope.totalFee = 0.01;//response.data['totalFee'];
                //$scope.createOrder = response.stateCode;
                getUnpaidOrder();
            });
        };

        function getUnpaidOrder(){
            var url = "doorbell/orders/listUnpaid" + paramUrl;
            var promise = testService.runTest('GET', url);
            promise.then(function(response){
                var data = response.data;
                for(var i=0; i<data.length;i++){
                    if(data[i].orderNo == $scope.orderNo){
                        $scope.createOrder = 'SUCCESS';
                        payOrder();
                    }else{
                        $scope.createOrder = 'FAILED';
                    }
                }
                $scope.unpaid = response.stateCode;
            });
        };

        function alipayNotify(){
            var url = "doorbell/orders/test/alinotify" + paramUrl
                + "&orderNo=" + $scope.orderNo + "&totalFee=" + $scope.totalFee;
            var promise = testService.runTest('POST', url);
            promise.then(function(response){
                deviceStatusAfterPay();
            });
        };

        function payOrder(){
            var url = "doorbell/orders/callback/paid" + paramUrl + "&orderNo=" + $scope.orderNo;
            var promise = testService.runTest('POST', url);
            promise.then(function(response){
                $scope.pay = response.stateCode;
                alipayNotify();
            });
        };

        function refund(){
            var url = "doorbell/profile/cancel" + paramUrl + "&profileId=" + $scope.profileId;
            var promise = testService.runTest('POST', url);
            promise.then(function(response){
                statusAfterRefund();
                //if($scope.revoke){
                //    $scope.reRefund = response.stateCode;
                //    getRefundRequestList();
                //    $scope.revoke = false;
                //}else{
                //    $scope.refund = response.stateCode;
                //
                //}
            });
        };

        function revokeRefund(){
            var url = "doorbell/profile/revokeCancel" + paramUrl + "&profileId=" + $scope.profileId;
            var promise = testService.runTest('POST', url);
            promise.then(function(response){
                statusAfterRevokeRefund();
                $scope.revokeRefund = response.stateCode;
                $scope.revoke = true;
            });
        };

        function getRefundRequestList(){
            var url = "doorbell/refund/request/getByOrderNo" + paramUrl + "&orderNo=" + $scope.orderNo;
            var promise = testService.runTest('GET', url);
            promise.then(function(response){
                var ids = '';
                var data = response.data;
                for(var i = 0; i < (data.length - 1); i++){
                    ids = ids + data[i].id + ',';
                }
                if(data.length != 0){
                    ids = ids + data[data.length - 1].id;
                }

                $scope.ids = ids;
                refundSuccess();
            });
        }

        function refundSuccess(){
            var url = "doorbell/orders/callback/refund" + paramUrl + "&refundRequestIds=" + $scope.ids;
            var promise = testService.runTest('POST', url);
            promise.then(function(response){
                $scope.refundSuccess = response.stateCode;
                statusAfterRefundSucc();
            });
        };

        function getActionList(){
            var url = "doorbell/actions/list" + paramUrl;
            var promise = testService.runTest('GET', url);
            promise.then(function(response){
                var data = response.data;
                $scope.actionId = data[data.length - 1].id;
                $scope.actionList = response.stateCode;
                getActionDetail();
            });
        };

        function getActionDetail(){
            var url = "doorbell/actions/detail" + paramUrl + "&actionId=" + $scope.actionId;
            var promise = testService.runTest('GET', url);
            promise.then(function(response){
                $scope.actionDetail = response.stateCode;
            });
        };

        $scope.runTest = function (api) {

            var deviceId = $scope.deviceId;
            var clientType = $scope.clientType;
            var token = $scope.token;
            paramUrl = '?clientType=' + clientType
                + '&token=' + token
                + '&deviceId=' + deviceId;
            var params = api.parameters;
            var apiUrl = 'doorbell' + api.url;
            if (params != undefined) {
                for (var i = 0; i < params.length; i++) {
                    if (apiUrl.contains('{id}') && params[i].name == 'id') {
                        apiUrl = apiUrl.replace('{id}', params[i].paramValue);
                    } else {
                        paramUrl = paramUrl + '&' + params[i].name + '=' + params[i].paramValue;
                    }
                }
            }
            var url = apiUrl + paramUrl;
            var param;
            if(api.url.contains('/orders/create')){
                var space, month, flow, startTime;
                for (var i = 0; i < params.length; i++) {
                    if(params[i].name == 'space'){
                        space = params[i].paramValue;
                    }else if(params[i].name == 'flow'){
                        flow = params[i].paramValue;
                    }
                    else if(params[i].name == 'month'){
                        month = params[i].paramValue;
                    }
                    else if(params[i].name == 'activateType'){
                        startTime = params[i].paramValue;
                    }
                }
                var param = {orderNo: "", deviceId: $scope.deviceId, month: month, totalFee: 0,
                    space: space*1024*1024, flow: flow*60*60, activateType: startTime};
                testService.runTest(api.method, url, param).then(
                    function (response) {
                        api.result = response.stateCode + '\n\r';
                        var data = response.data;
                        var show = api.show;
                        for (var i = 0; i < show.length; i++) {
                            api.result = api.result + show[i] + '=' + data[show[i]] + '  ';
                        }

                    });

            }else if(api.url.contains('/profile/current')){
                testService.runTest(api.method, url).then(
                    function (response) {
                        console.log(response.data);
                        api.result = response.stateCode + '\n\r';
                        var arr = response.data.items.PURCHASE;
                        var data = arr[0];
                        for(var j = 0; j < arr.length; j++){
                            if(data.id < arr[j].id){
                                data = arr[j];
                            }
                        }
                        var show = api.show;
                        for (var i = 0; i < show.length; i++) {
                            api.result = api.result + 'newProfileId=' + data[show[i]] + '  ';
                        }

                    })
            }else if(api.url.contains('/orders/listUnpaid') ||
                api.url.contains('/refund/request/getByOrderNo') ||
                api.url.contains('/actions/list') ){
                testService.runTest(api.method, url).then(
                    function (response) {
                        api.result = response.stateCode + '\n\r';
                        var arr = response.data;
                        var data = arr[0];
                        for(var j = 0; j < arr.length; j++){
                            if(data.createTime < arr[j].createTime){
                                data = arr[j];
                            }
                        }
                        var show = api.show;
                        for (var i = 0; i < show.length; i++) {
                            api.result = api.result + show[i] + '=' + data[show[i]] + '  ';
                        }
                    }
                );
            }else if(api.url.contains('/orders/callback/paid')){
                testService.runTest(api.method, url).then(
                    function (response) {
                        var url = "doorbell/orders/test/alinotify" + paramUrl
                            + "&totalFee=0.01";
                        testService.runTest('POST', url);
                        api.result = response.stateCode + '\n\r';

                    }
                );
            }else{
                testService.runTest(api.method, url).then(
                    function (response) {
                        api.result = response.stateCode + '\n\r';
                        var data = response.data;
                        var show = api.show;
                        for (var i = 0; i < show.length; i++) {
                            api.result = api.result + show[i] + '=' + data[show[i]] + '  ';
                        }

                    }
                );
            }

        }
    }
]);