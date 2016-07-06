app.controller('deviceController', ['$scope', 'Profile', 'Order', 'Resource','$filter','$timeout','Device','$state','$stateParams',function ($scope, Profile, Order, Resource, $filter, $timeout, Device, $state,$stateParams) {
    $scope.selectedDeviceItem = null;
    const spaceUnits = ['KB', 'MB', 'GB'];
    const flowUnits = ['秒', '分', '小时'];
    const spaceUnitValue = 1024;
    const flowUnitValue = 60;

    $scope.getProgressBarColor = function (usedRate) {
        usedRate = usedRate/100;
        if(usedRate < 0.8) {
            return 'info';
        } else if( usedRate >= 0.8 && usedRate < 1) {
            return 'warning';
        } else if (usedRate >= 1){
            return 'danger';
        }
    };

    $scope.actionStatusMap = {
        'NONCANCEABLE': {
            value: '退订',
            active: false
        },
        'CANCELABLE': {
            value: '退订',
            active: true
        },
        'REVOCABLE': {
            value: '撤销',
            active: true
        },
        'IRREVOCABLE': {
            value: '撤销',
            active: false
        }
    };
    var showSpaceFlowBar = function (deviceItem) {
        var space = deviceItem;
        if (space.total >= 9999999999) {
            space.spaceUsedRate = 0;
            space.spaceTotal = {value: '无限', unit: spaceUnits[2]};
        } else {
            space.spaceUsedRate = space.used / space.total * 100;
            space.spaceTotal = getUnitChangeValue(space.total, spaceUnitValue, spaceUnits);
        }
        space.spaceUsed = getUnitChangeValue(space.used, spaceUnitValue, spaceUnits);
        $scope.getProgressBarColor(space.spaceUsedRate);
    };

    var getUnitChangeValue = function (used, unitValue, units) {
        var index = 0;
        var calculateValue = used;
        while (used >= unitValue) {
            used /= unitValue;
            calculateValue = used.toFixed(1);
            index++;
            if (index >= units.length - 1) {
                break;
            }
        }
        return {value: calculateValue, unit: units[index]};
    };

    $scope.spaceResource = [];
    $scope.pageInfo = {
        page: 0,
        limit: 15,
        sortBy: 'device_id',
        order: 'desc'
    };

    var init = function () {
        if ($stateParams.data) {
            //$scope.ServerDeviceItem.deviceId = $stateParams.data;
            //$scope.searchInfo = { deviceId : $stateParams.data } ;
            //console.log( $scope.searchInfo);
            //Profile.filterDevice($scope.searchInfo).success(function(response) {
            //    $scope.app.devices = response.data;
            //    $scope.deviceHandle($scope.app.devices);
            //})
            $scope.searchDeviceId = parseInt($stateParams.data);
            $scope.searchInfo = { deviceId : $scope.searchDeviceId};
            Profile.filterDevice($scope.searchInfo).success(function(response) {
                $scope.app.devices = response.data;
                $scope.deviceHandle($scope.app.devices);
            })
        } else {
            Profile.deviceInfos($scope.pageInfo).success(function (response) {
                $scope.app.devices = response.data;
                $scope.deviceHandle($scope.app.devices);
            });
        }
    };
    init();

    $scope.deviceHandle = function(data) {
        angular.forEach(data, function (item) {
            var _item = item.spaceResource;
            showSpaceFlowBar(_item);
            for( var p in _item) {
                if( p == 'total' || p == 'used' || p == 'remain') {
                    if(_item[p] == 0 && p == 'total'){
                        _item[p] ='免费';
                    } else if( _item[p] == 9999999999 && p == 'total'){
                        _item[p] = '无限';
                    } else {
                        _item[p] = _item[p]/1024/1024;
                    }
                }
            }
            $scope.spaceResource.push(_item);
        });
    };

    $scope.getDeviceListByPage = function(action) {
        angular.element("#" + action).attr('disabled', true);
        if (action === 'prev') {
            if ( $scope.pageInfo.page < 1) {
                angular.element("#" + action).attr('disabled', true);
                return false;
            } else {
                $scope.pageInfo.page = $scope.pageInfo.page - 1;
            }
        } else if (action === 'next') {
            $scope.pageInfo.page = $scope.pageInfo.page + 1;
        }
        Profile.deviceInfos($scope.pageInfo).then(function (response) {
            $scope.app.devices = response.data.data;
            console.log($scope.app.devices);
            $scope.deviceHandle($scope.app.devices);
            angular.element("#" + action).attr('disabled', false);
        });
    };
    $scope.showAllDevice  = function() {
        if($stateParams.data) {
            $stateParams.data = null;
            $state.go('app.devices');
        }
        init();
    };

    $scope.searchDeviceInfo = function() {
        console.log($scope.searchDeviceId);
        deviceId = $scope.searchDeviceId;
        $state.go('app.devices',{data:deviceId});
        $scope.searchInfo = { deviceId : $scope.searchDeviceId } ;
        console.log( $scope.searchInfo);
        Profile.filterDevice($scope.searchInfo).success(function(response) {
            $scope.app.devices = response.data;
            $scope.deviceHandle($scope.app.devices);
        })
    };

    $scope.deviceOrders = [];
    //$scope.selectDeviceItem = function (selectedDeviceItem) {
    //    $scope.selectedDeviceItem = selectedDeviceItem;
    //    angular.forEach($scope.app.devices, function (deviceItem) {
    //        deviceItem.selected = false;
    //    });
    //    //$scope.selectedDeviceItem.selected = true;
    //    Profile.getCurrent($scope.selectedDeviceItem).success(function (response) {
    //        $scope.deviceOrderItem = response.data;
    //        showSpaceFlowBar($scope.deviceOrderItem);
    //    });
    //    Profile.getActionList($scope.selectedDeviceItem).success(function (response) {
    //        $scope.deviceOrders = response.data;
    //        console.log(response.data);
    //    });
    //    console.log($scope.deviceOrders)
    //    return $scope.deviceOrders;
    //};

    $scope.processProfile = function (profile) {
        if (profile.actionStatus === 'CANCELABLE') {
            Profile.cancel(profile.id).success(function () {
                profile.actionStatus = 'REVOCABLE';
            });
        } else if (profile.actionStatus === 'REVOCABLE') {
            Profile.revokeCancel(profile.id).success(function () {
                profile.actionStatus = 'CANCELABLE';
            });
        }
    }
    //var showSpaceFlowInTradeDetail = function (orderDetail) {
    //    $scope.spaceFrom = getUnitChangeValue(orderDetail.space.from, spaceUnitValue);
    //    $scope.spaceTo = getUnitChangeValue(orderDetail.space.to, spaceUnitValue);
    //    $scope.flowFrom = getUnitChangeValue(orderDetail.flow.from, flowUnitValue);
    //    $scope.flowTo = getUnitChangeValue(orderDetail.flow.to, flowUnitValue);
    //}
    $scope.syncResource = function (id) {
        var deviceId = id;
        Profile.syncResource(deviceId).success(function () {
            $scope.deviceHandle(deviceId);
            toast('success','','资源同步成功');
        });
    }


}]);

app.controller('deviceDetailController', ['$scope', 'Profile', 'Order', 'Resource','$filter','$timeout','Device','$stateParams',function ($scope, Profile, Order, Resource, $filter, $timeout, Device, $stateParams) {
    Profile.getCurrent($stateParams.id).success(function (response) {
        $scope.ServerDeviceItem = response.data;
        $scope.ServerDeviceItem.deviceId = $stateParams.id;
        $scope.ServerDeviceItem.deviceName = $stateParams.name;
        console.log($scope.ServerDeviceItem)
    });
    $scope.getOrderDetail = function(id,profile) {
        $scope.orderDetail = profile;
        console.log(profile);
        Order.detail(profile.orderNo).success(function (response) {
            console.log(response)
            var orderDetail = response.data;
            $scope.paidGoodsInfo = getGoodsInfo(orderDetail.orderRemark);
            if (orderDetail.orderState == 'ORDER_PAID') {
                Order.payDetail(profile.orderNo).success(function (response) {
                    console.log(response)
                    $scope.payDetail = response.data;
                });
            }
            if (orderDetail.refundedFee > 0) {
                Refund.getByOrderNo(profile.orderNo).success(function (response) {
                    console.log(response.data[0]);
                    $scope.refundGoodsInfo = getGoodsInfo(response.data[0]);
                });
                Order.refundDetail(profile.orderNo).success(function (response) {
                    $scope.refundDetail = response.data;
                });
            }
        });
    };
    var getGoodsInfo = function (requestDetail) {
        var goodsInfo = {};
        if (requestDetail != null && requestDetail.refundDesc != null) {
            var params = requestDetail.refundDesc.split(';');
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

app.controller('deviceOrderController', ['$scope', 'Profile', 'Order', 'Resource','$filter','$timeout','Device','$stateParams',function ($scope, Profile, Order, Resource, $filter, $timeout, Device, $stateParams) {
    var id = $stateParams.id;
    Profile.getActionList(id).success(function (response) {
        $scope.OrderDeviceItem = response.data;
        $scope.OrderDeviceItem.deviceId = $stateParams.id;
        $scope.OrderDeviceItem.deviceName = $stateParams.name;
    });
    $scope.getOrderDetail = function (id, action) {
        $scope.orderDetail = null;
        Device.detail(id).success(function (response) {
            $scope.orderDetail = response.data;
            $scope.creatTime = new Date($scope.orderDetail.createTime);
            var endTime = $scope.creatTime.getTime() + ($scope.orderDetail.month * 2592000000);
            $scope.endTime = new Date(endTime);
            showSpaceFlowInTradeDetail($scope.orderDetail);
        });
		$scope.orderAction = action;
    };
}]);

app.controller('devicePurchaseController', ['$scope', 'Profile', 'Order', 'Resource','$filter','$timeout','Device','$stateParams',function ($scope, Profile, Order, Resource, $filter, $timeout, Device, $stateParams) {
    $scope.deviceItem = {};
    $scope.deviceItem.month = 1;
    var init = function() {
        Resource.getList().then(function (response) {
            $scope.itemsSelect = response.data.data;
            for(var i = 0;i <$scope.itemsSelect.length; i++){
                if($scope.itemsSelect[i].price ==0){
                    $scope.itemsSelect[i].price ='免费';
                }
                if( $scope.itemsSelect[i].size == 9999999999){
                    $scope.itemsSelect[i].size = '无限';
                }else if($scope.itemsSelect[i].type == 'SPACE'){
                    var value = $scope.itemsSelect[i].size;
                    $scope.itemsSelect[i].size = value/1024/1024;
                }else if($scope.itemsSelect[i].type == 'FLOW'){
                    var value = $scope.itemsSelect[i].size;
                    $scope.itemsSelect[i].size = value/60/60;
                }
            }
        });
    };
    init();
    if( $stateParams.id ) {
        $scope.deviceInput = $stateParams.id;
        console.log("1")
    }
    $scope.deviceIds = [];
    $scope.infoMessage = false;
    $scope.checkDeviceId = function() {
        $scope.deviceIds = $scope.deviceInput.split(",");
        console.log($scope.deviceIds);
    };

    $scope.isEmpty = function (param) {
        if (param === null || param === undefined || param === '') {
            return true;
        }
        return false;
    };
    var placeholder = {region_id:0, region_name: '请选择...'};

    $scope.buyNewset = function(deviceIds) {
        console.log($scope.deviceItem);
        console.log(deviceIds);
        //$scope.deviceIds = deviceIds;
        //$scope.deviceItems = [];
        var regx = /^\d+(,\d+)*$/;

        if( $stateParams.id != ''){
            $stateParams.id = ($stateParams.id).replace(new RegExp('\"','gm'),'');
            console.log($stateParams.id);
            if( $stateParams.id == $scope.deviceInput ) {
                $scope.deviceIds.push($stateParams.id);
            }
        }
        if(regx.exec($scope.deviceIds)) {
            //$scope.deviceIds = $scope.deviceInput.split(",");
            console.log($scope.deviceIds);
        } else {
            toast('error', '', '设备ID输入格式有误,请参照标准格式');
            return;
        }
        if( !$scope.deviceItem.buyWay ) {
            toast('error','','请选择购买方式');
            return false;
        }
        if ($scope.deviceItem.buyWay == '购买') {
            if( $scope.isEmpty($scope.deviceItem.space) ) {
                toast('error','','请选择空间大小');
                $scope.deviceIds = [];
                return false;
            } else if( !$scope.deviceItem.month ) {
                toast('error','','请选择购买时长');
                $scope.deviceIds = [];
                return false;
            } else if ( $scope.deviceItem.month > 24 || $scope.deviceItem.month < 1) {
                toast('error','','请选择有效时间在0到24之间的月');
                $scope.deviceIds = [];
                return false;
            } else if( !$scope.deviceItem.activateType ) {
                toast('error','','请选择生效方式');
                $scope.deviceIds = [];
                return false;
            }
        } else {
            if( !$scope.deviceItem.space ) {
                toast('error','','请选择空间大小');
                $scope.deviceIds = [];
                return false;
            } else if( !($scope.deviceItem.startTime && $scope.deviceItem.endTime)) {
                toast('error','','请选择有效日期');
                $scope.deviceIds = [];
                return false;
            }  else if( !$scope.deviceItem.buyWay ) {
                toast('error','','请选择购买方式');
                $scope.deviceIds = [];
                return false;
            }
        }
        $scope.deviceItems = [];
        angular.forEach(deviceIds, function(deviceId, deviceItem) {
            deviceItem.value = deviceId;
            if($stateParams.id && $stateParams.deviceIds == $stateParams.id) {
                deviceId = $stateParams.id;
            }
            $scope.deviceItem.param = {
                deviceId: deviceId,
                space:  $scope.deviceItem.space.name == 'Max Space' ? 9999999999 :($scope.deviceItem.space).size * 1024 * 1024
                //flow: ($scope.deviceItem.flow).size * 60 * 60
                //month: $scope.deviceItem.month,
                //activateType: $scope.deviceItem.activateType
            };
            if ($scope.deviceItem.buyWay == '签约') {
                $scope.deviceItem.param.startTime = $scope.deviceItem.startTime + "-" + "01" + " 00:00:00";
                var d = new Date($scope.deviceItem.endTime.split('-')[0], $scope.deviceItem.endTime.split('-')[1], 0);
                d.setHours(23);
                d.setMinutes(59);
                d.setSeconds(59);
                $scope.deviceItem.param.endTime = $filter('date')(d,'yyyy-MM-dd HH:mm:ss');
            } else {
                $scope.deviceItem.param.activateType = $scope.deviceItem.activateType;
                $scope.deviceItem.param.month = $scope.deviceItem.month;
            }
            //console.log($scope.deviceItem.param);
            $scope.deviceItems.push($scope.deviceItem.param);
        });
        $scope.deviceFail = [];
        if ($scope.deviceItem.buyWay == '签约') {
            if (new Date($scope.deviceItem.param.startTime) > new Date($scope.deviceItem.param.endTime)) {
                toast('error','','开始时间不能大于结束时间');
                return false;
            }
            Order.signup($scope.deviceItems).success(function(response) {
                $scope.signErrordata = response.failed;
                $scope.deviceFail = $scope.signErrordata;
                if( $scope.deviceFail.length ) {
                    toast('error','','购买失败');
                    $scope.errorD = true;
                    jQuery("#buy")[0].disabled = true
                } else {
                    toast('success','','购买成功');
                }
            });
        } else if ($scope.deviceItem.buyWay == '购买'){
            Order.buy($scope.deviceItems).success(function(response) {
                $scope.buyErrordata = response.data.failed;
                $scope.deviceFail = $scope.buyErrordata;
                if( $scope.deviceFail.length ) {
                    toast('error','','购买失败');
                    $scope.errorD = true;
                    jQuery("#buy")[0].disabled = true
                } else {
                    toast('success','','购买成功');
                }
            });
        }
        $scope.deviceItem = [];
        $scope.deviceItem.month = 1;
    };

    $scope.buyAgain = function() {
        var items = $scope.deviceFail + '';
        $scope.deviceInput = items;
        $scope.checkDeviceId();
        $scope.errorD = false;
        jQuery("#buy")[0].disabled = false;
    };

    $scope.timeSelect = function ($event) {
        var selectId = "#" + $event.currentTarget.id;
        $(selectId).datetimepicker({
            language: 'zh-CN',
            format: 'yyyy-mm',
            autoclose: true,
            startView: 3,
            minView: 3
        });
        $(selectId).datetimepicker('show');
    };

    $scope.signShow = false;
    $scope.buyShow = true;
    $scope.buyShow = true;
    $scope.changeMenu = function() {
        if($scope.deviceItem.buyWay == '购买') {
            $scope.signShow = false;
            $scope.buyShow = true;
            $scope.buyShow = true;
        } else if ($scope.deviceItem.buyWay == '签约') {
            $scope.signShow = true;
            $scope.buyShow = false;
            $scope.buyShow = false;
        }
    };
}]);

app.controller('device1Controller', ['$scope', 'Profile', 'Order', 'Resource','$filter','$timeout','Device','$state','$stateParams',function ($scope, Profile, Order, Resource, $filter, $timeout, Device, $state,$stateParams) {
    $scope.selectedDeviceItem = null;
    const spaceUnits = ['KB', 'MB', 'GB'];
    const flowUnits = ['秒', '分', '小时'];
    const spaceUnitValue = 1024;
    const flowUnitValue = 60;

    $scope.getProgressBarColor = function (usedRate) {
        usedRate = usedRate/100;
        if(usedRate < 0.8) {
            return 'info';
        } else if( usedRate >= 0.8 && usedRate < 1) {
            return 'warning';
        } else if (usedRate >= 1){
            return 'danger';
        }
    };

    $scope.actionStatusMap = {
        'NONCANCEABLE': {
            value: '退订',
            active: false
        },
        'CANCELABLE': {
            value: '退订',
            active: true
        },
        'REVOCABLE': {
            value: '撤销',
            active: true
        },
        'IRREVOCABLE': {
            value: '撤销',
            active: false
        }
    };

    $scope.spaceResource = [];
    $scope.pageInfo = {
        page: 0,
        limit: 15,
        sortBy: 'device_id',
        order: 'desc'
    };

    var init = function () {
        Profile.listDevice().success(function (response) {
            var selectItem = null;
            $scope.app.devices = [];
            angular.forEach(response.data, function (key, value) {
                if (value == $scope.app.selectDeviceId) {
                    selectItem = {key: key, value: value};
                    $scope.app.devices.push(selectItem);
                } else {
                    $scope.app.devices.push({key: key, value: value});
                }
            });
            if ($scope.app.devices.length > 0) {
                selectItem = selectItem ? selectItem : $scope.app.devices[0];
                $scope.selectDeviceItem(selectItem);
            }
        });
    };
    init();

    var showSpaceFlowBar = function (deviceOrderItem) {
        var space = deviceOrderItem.space;
        if (space.total >= 9999999999) {
            $scope.spaceUsedRate = 0;
            $scope.spaceTotal = {value: '无限', unit: spaceUnits[2]};
        } else {
            $scope.spaceUsedRate = space.used / space.total * 100;
            $scope.spaceTotal = getUnitChangeValue(space.total, spaceUnitValue, spaceUnits);
        }
        $scope.spaceUsed = getUnitChangeValue(space.used, spaceUnitValue, spaceUnits);

        //var flow = deviceOrderItem.flow;
        //if (flow.total >= 9999999999) {
        //    $scope.flowUsedRate = 0;
        //    $scope.flowTotal = {value: '无限', unit: flowUnits[2]};
        //} else {
        //    $scope.flowUsedRate = flow.used / flow.total * 100;
        //    $scope.flowTotal = getUnitChangeValue(flow.total, flowUnitValue, flowUnits);
        //}
        //$scope.flowUsed = getUnitChangeValue(flow.used, flowUnitValue, flowUnits);
    }

    var getUnitChangeValue = function (used, unitValue, units) {
        var index = 0;
        var calculateValue = used;
        while (used >= unitValue) {
            used /= unitValue;
            calculateValue = used.toFixed(1);
            index++;
            if (index >= units.length - 1) {
                break;
            }
        }
        return {value: calculateValue, unit: units[index]};
    }

    $scope.selectDeviceItem = function (selectedDeviceItem) {
        $scope.selectedDeviceItem = selectedDeviceItem;
        angular.forEach($scope.app.devices, function (deviceItem) {
            deviceItem.selected = false;
        });
        $scope.selectedDeviceItem.selected = true;
        //console.log($scope.selectedDeviceItem.value);
        Profile.getCurrent($scope.selectedDeviceItem.value).success(function (response) {
            $scope.deviceOrderItem = response.data;
            console.log(response.data);
            showSpaceFlowBar($scope.deviceOrderItem);
        });
        Profile.getActionList($scope.selectedDeviceItem.value).success(function (response) {
            console.log(response.data);
            $scope.deviceOrders = response.data;
        });
    };

    $scope.processProfile = function (profile) {
        if (profile.actionStatus === 'CANCELABLE') {
            Profile.cancel(profile.id).success(function () {
                profile.actionStatus = 'REVOCABLE';
            });
        } else if (profile.actionStatus === 'REVOCABLE') {
            Profile.revokeCancel(profile.id).success(function () {
                profile.actionStatus = 'CANCELABLE';
            });
        }
    }

    var showSpaceFlowInTradeDetail = function (orderDetail) {
        $scope.spaceFrom = getUnitChangeValue(orderDetail.space.from, spaceUnitValue);
        $scope.spaceTo = getUnitChangeValue(orderDetail.space.to, spaceUnitValue);
        $scope.flowFrom = getUnitChangeValue(orderDetail.flow.from, flowUnitValue);
        $scope.flowTo = getUnitChangeValue(orderDetail.flow.to, flowUnitValue);
    }

    $scope.getOrderDetail = function (id) {
        $scope.getorderDetail = null;
        Device.detail(id).success(function (response) {
            console.log(response.data)
            $scope.getorderDetail = response.data;
            $scope.creatTime = new Date($scope.getorderDetail.createTime);
            var endTime = $scope.creatTime.getTime() + ($scope.getorderDetail.month * 2592000000);
            $scope.endTime = new Date(endTime);

            showSpaceFlowInTradeDetail($scope.getorderDetail);
        });
    };
    $scope.getDetail = function(id,profile) {
        $scope.orderDetail = profile;
        console.log(profile);
        Order.detail(profile.orderNo).success(function (response) {
            console.log(response)
            var orderDetail = response.data;
            $scope.paidGoodsInfo = getGoodsInfo(orderDetail.orderRemark);
            if (orderDetail.orderState == 'ORDER_PAID') {
                Order.payDetail(profile.orderNo).success(function (response) {
                    console.log(response)
                    $scope.payDetail = response.data;
                });
            }
            if (orderDetail.refundedFee > 0) {
                Refund.getByOrderNo(profile.orderNo).success(function (response) {
                    console.log(response.data[0]);
                    $scope.refundGoodsInfo = getGoodsInfo(response.data[0]);
                });
                Order.refundDetail(profile.orderNo).success(function (response) {
                    $scope.refundDetail = response.data;
                });
            }
        });
    };
    var getGoodsInfo = function (requestDetail) {
        var goodsInfo = {};
        if (requestDetail != null && requestDetail.refundDesc != null) {
            var params = requestDetail.refundDesc.split(';');
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

    $scope.syncResource = function () {
        var deviceItem = $scope.selectedDeviceItem;
        Profile.syncResource(deviceItem).success(function () {
            $scope.selectDeviceItem(deviceItem);
        });
    }
}]);
