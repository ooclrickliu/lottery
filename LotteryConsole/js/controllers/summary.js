/**
 * Created by deqin.zhu on 2016/5/16.
 */
app.controller('SummaryController', ['$scope', 'Summary', '$window', 'Resource', '$http', '$stateParams',
    '$filter', function ($scope, Summary, $window, Resource, $http, $stateParams,$filter) {
    $scope.items = {};
    $scope.unit = {};
    $scope.product = {};
    $scope.summaryArgs = {};
    $scope.id = $scope.$stateParams.id;
    $scope.detailButton = false;
    $scope.showUnit = false;
    var init = function () {
        Resource.getList().success(function (response) {
            $scope.items = response.data;
        });
        $scope.barboolean = true;
        $scope.showMonthSelect = false;
        if (!$scope.id) {
            $scope.nav_title = '商品销售统计';
        } else {
            $scope.nav_title = '';
        }
        $scope.showDaySelect = true;
        $scope.bardata = {data:[],boolean:false,ticks:[],tickFormatter:function(){}};

    };
    init();
    $scope.bar_unit = ['元','笔','月'];
    $scope.nav_nameList = ['收支统计', '订单统计', '商品销售统计', '设备消费统计'];
    $scope.nav_name = $scope.nav_nameList[$scope.id - 1];
    $scope.timeUnitShow = {
        DAY: '天',
        MONTH: '月份',
        QUARTER: '季度',
        YEAR: '年份'
    };
    $scope.summaryType = ['INOUT', 'ORDER', 'COMMODITY', 'CONSUMPTION'];
    /*商品销售统计数据格式化*/
    var modChartDataFormat = function (data) {
        var chartdata = data;
        var reldata = {
            amount: [],
            sales: [],
            count: [],
            ticks: []
        };
        var formatTick = function(data) {
            if(data.length < 20) {
                $scope.step = 1;
            } else {
                $scope.step = Math.ceil(data.length/16);
            }
            return $scope.step;
        };
        //angular.forEach(chartdata, function (item) {
        //    //$scope.chartSpace = item.spaceList;
        //    //var _item = item;
        //    //angular.forEach($scope.chartSpace, function (item) {
        //    //    item.time = _item.time;
        //    //    item.productType = 'SPACE';
        //    handledata.push(item);
        //    //});
        //});
        var handledata = chartdata;
        for (var i = 0; i < handledata.length; i = i + 1) {
            formatTick(handledata);
            if( $scope.searchWay == '商品类型' && i%$scope.step != 0) {
                handledata[i].time = '';
            }
            var tempchart = {
                ticks: $scope.searchWay == '时间' ? [i + 1, handledata[i].resCode] : [i + 1, handledata[i].time],
                amount: [i + 1, handledata[i].amount],
                sales: [i + 1, handledata[i].sales],
                count: [i +1 , handledata[i].count]
            };
            reldata.amount.push(tempchart.amount);
            reldata.sales.push(tempchart.sales);
            reldata.count.push(tempchart.count);
            reldata.ticks.push(tempchart.ticks);
        }
        handledata = [];
        return reldata;

    };
    /*收支、订单、设备消费数据格式化*/
    var chartDataFormat = function (data) {
        var chartdata = data;
        var reldata = {};
        var formatTick = function(data,i) {
            if(data.length < 20) {
                $scope.step = 1;
            } else {
                $scope.step = Math.ceil(data.length/16);
            }
            return $scope.step;
        };
        reldata.chartdata = chartdata;
        reldata.ticks = [];
        reldata.fir = [];
        reldata.sec = [];
        reldata.title = {};
        reldata.total = {};
        reldata.title.fir = $scope.id == 1 ? '收入金额' : '购买数量';
        reldata.title.sec = $scope.id == 1 ? '支出金额' : '退订数量';
        reldata.total.name = "总计";
        reldata.total.fir = $scope.id == 1 ? chartdata.totalIncoming : chartdata.totalPurchase;
        reldata.total.sec = $scope.id == 1 ? chartdata.totalOutgoing : chartdata.totalRefund;
        var databody = chartdata.detailDatas;
        for (i = 0; i < databody.length; i++) {
            reldata.fir.push([i + 1, databody[i].incoming == null ? databody[i].purchase : databody[i].incoming]);
            reldata.sec.push([i + 1, databody[i].outgoing == null ? databody[i].refund : databody[i].outgoing]);
            formatTick(databody,i);
            if( i%$scope.step == 0) {
                reldata.ticks.push([i + 1, databody[i].time]);
            } else {
                reldata.ticks.push([i + 1, '']);
            }
        }
        return reldata;
    };
    /*flot图表渲染*/
    $scope.barshow = {};
    var showBarChart = function (data) {
        $scope.barshow = data;
        if ($scope.nav_title == '商品销售统计' ) {
            $scope.barUnit = $scope.bar_unit[2];
            $('#1').addClass('active');
            if ($scope.searchWay == '商品类型') {
                if ($scope.barshow.amount.length < 8) {
                    $scope.bardata = {data:$scope.barshow.amount,boolean:true,ticks:$scope.barshow.ticks,tickFormatter:null};
                } else {
                    $scope.bardata = {data:$scope.barshow.amount,boolean:false,ticks:$scope.barshow.ticks,tickFormatter:null};
                }
            } else if ($scope.searchWay == '时间') {
                $scope.summaryArgs.productChoose = null;
                if ($scope.barshow.amount.length < 8) {
                    $scope.bardata = {data:$scope.barshow.amount,boolean:true,ticks:$scope.barshow.ticks,tickFormatter:null};
                } else {
                    $scope.bardata = {data:$scope.barshow.amount,boolean:false,ticks:$scope.barshow.ticks,tickFormatter:null};
                }
            }
        } else if ( $scope.id == 1 || $scope.id == 2) {
            var barChartData = [];
            $('#1').addClass('active');
            $scope.barshow.fir.length > 8 ? $scope.barboolean = false : $scope.barboolean = true;
            barChartData.push({label:$scope.barshow.title.fir,data:$scope.barshow.fir});
            barChartData.push({label:$scope.barshow.title.sec,data:$scope.barshow.sec});
            var barChartOption = {
                legend: {show: true, labelBoxBorderColor: $scope.barcolor.primary},
                bars: {
                    show: true,
                    fill: true,
                    lineWidth: 1,
                    order: 1,
                    barWidth: 0.1,
                    fillColor: {colors: [{opacity: 0.5}, {opacity: 0.9}]}
                },
                colors: [$scope.barcolor.info, $scope.barcolor.primary, $scope.barcolor.success],
                series: {shadowSize: 1},
                xaxis: {
                    reserveSpace: true,
                    ticks: $scope.barshow.ticks,
                    position: 'bottom',
                    autoscaleMargin: $scope.barboolean
                },
                yaxis:{ font: { color: 'black' },
                        position:'left',
                        tickFormatter:  $scope.tickFormatter
                },
                grid: {
                    show: true,
                    color: 'black',
                    hoverable: true,
                    clickable: true,
                    borderWidth: 1,
                    labelMargin: 10
                },
                tooltip: true,
                tooltipOpts: { content: '%y.2',  defaultTheme: false, shifts: { x: 0, y: 20 } }
            };
            jQuery.plot(jQuery('#barChart'),barChartData,barChartOption);
        }
    };
    /*详细数据列表*/
    $scope.chartshow = {};
    var showChart = function (data) {
        $scope.chartshow = data;
    };
    /*时间粒度参数变化*/
    $scope.onTimeUnitChange = function () {
        $scope.showTimeSelect = true;
        $scope.timeUnit = $scope.unit.timeUnit;
        $scope.summaryArgs = {};
        $('.form_date').datetimepicker('remove');
        switch ($scope.timeUnit ) {
            case 'YEAR':
                $scope.showYearSelect = true;
                $scope.showQuarter = false;
                $scope.showMonthSelect = false;
                $scope.showDaySelect = false;
                break;
            case 'QUARTER':
                $scope.showYearSelect = false;
                $scope.showQuarter = true;
                $scope.showMonthSelect = false;
                $scope.showDaySelect = false;
                break;
            case 'MONTH':
                $scope.showYearSelect = false;
                $scope.showQuarter = false;
                $scope.showMonthSelect = true;
                $scope.showDaySelect = false;
                break;
            case 'DAY':
                $scope.showYearSelect = false;
                $scope.showQuarter = false;
                $scope.showMonthSelect = false;
                $scope.showDaySelect = true;
                break;
        }
    };
    /*时间控件dateTimePicker初始化*/
    $scope.timeSelect = function ($event) {
        var selectId = "#" + $event.currentTarget.id;
        if ($scope.timeUnit == 'MONTH') {
            $(selectId).datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm',
                autoclose: true,
                startView: 3,
                minView: 3
            });
            $(selectId).datetimepicker('show');
        } else if ($scope.timeUnit == 'YEAR' || $scope.timeUnit == 'QUARTER') {
            $(selectId).datetimepicker({
                language: 'zh-CN',
                format: 'yyyy',
                autoclose: true,
                startView: 4,
                minView: 4
            });
            $(selectId).datetimepicker('show');
        } else {
            $(selectId).datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                startView: 2,
                minView: 2
            });
            $(selectId).datetimepicker('show');
        }

    };
    /*日期初始化*/
    $scope.timeShow = function (start,end) {
        var endTime = end.split('-');
        var startTime = start.split('-');
        end = new Date(endTime[0], endTime[1] ? endTime[1] : 12, 0);
        switch (startTime.length) {
            case 1:
                start = start + '-' + '01' + '-' + '01';
                break;
            case 2:
                start = start + '-' + '01';
                break;
        }
        $scope.summaryArgs.startTime = $scope.dateformat(start);
        $scope.summaryArgs.endTime = $scope.dateformat(end);
    };
    /*日期格式化为YYYY-MM-DD*/
    $scope.dateformat = function (date) {
        if (date instanceof  Date) {
            if(date.getMonth() < 9 && date.getDate() < 10) {
                return date.getFullYear() + '-0' + (date.getMonth() + 1) + '-0' + date.getDate();
            } else if ( date.getMonth() < 9 && date.getDate() >= 10) {
                return date.getFullYear() + '-0' + (date.getMonth() + 1) + '-' + date.getDate();
            } else if ( date.getMonth() >= 9 && date.getDate() < 10) {
                return date.getFullYear() + '-' + (date.getMonth() + 1) + '-0' + date.getDate();
            } else if ( date.getMonth() >= 9 && date.getDate() >= 10) {
                return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
            }
        } else {
            return date;
        }
    };
    /*flot插件y轴货币单位转化*/
    $scope.tickFormatter = function(t) {
        if($scope.id == 1 || !$scope.id) {
            if(t >= 1000) {
                return  $filter('currency')(t/10000,'￥') + '万';
            } else {
                return  $filter('currency')(t,'￥');
            }
        } else {
            return t;
        }
    };
    /*tab视图切换*/
    $scope.changeBarChart = function (e) {
        switch (e.delegateTarget.id) {
            case '1' :
                $scope.bardata = $scope.searchWay == '时间' ? {data:$scope.barshow.amount,boolean:false,ticks:$scope.barshow.ticks,tickFormatter:null}:
                    {data:$scope.barshow.amount,boolean:$scope.bardata.boolean,ticks:$scope.barshow.ticks,tickFormatter:null};
                $scope.barUnit = $scope.bar_unit[2];
                break;
            case '2' :
                $scope.bardata = $scope.searchWay == '时间' ? {data:$scope.barshow.sales,boolean:false,ticks:$scope.barshow.ticks,tickFormatter:$scope.tickFormatter}:
                    {data:$scope.barshow.sales,boolean:$scope.bardata.boolean,ticks:$scope.barshow.ticks,tickFormatter:$scope.tickFormatter};
                $scope.barUnit = $scope.bar_unit[0];
                break;
            case '3' :
                $scope.bardata = {data:$scope.barshow.count,boolean:$scope.bardata.boolean,ticks:$scope.barshow.ticks,tickFormatter:null};
                break;
        }

    };
    /*数据检查 是否为空*/
    $scope.isEmpty = function (param) {
        if (param === null || param === undefined || param === '') {
            return true;
        }
        return false;
    };

    $scope.info = function(info) {
        $scope.rankInfo = $scope.dateformat(info.startTime) + '~' + $scope.dateformat(info.endTime) +
            '按' + $scope.timeUnitShow[info.timeUnit] +  '统计TOP' + info.topNum;

    };

    $scope.checkTime = function(start,end) {
        if($scope.isEmpty(start)) {
            toast('error','','请输入开始时间');
            return false;
        } else if($scope.isEmpty(end)) {
            toast('error','','请输入结束时间');
            return false;
        } else if(new Date(start) > new Date(end)){
            toast('error','','开始时间不能大于结束时间');
            return false;
        } else {
            return true;
        }
    };
    /*统计按钮*/
    $scope.summary = function ($valid) {
        if (!$valid) return;
        var summaryargs = {};
        $scope.detailButton = true;
        $scope.showUnit = true;

        if ($scope.id == 1) {
            $scope.barUnit = $scope.bar_unit[0];
            $scope.barTitle = '收支统计'
        } else if ( $scope.id == 2 ) {
            $scope.barUnit = $scope.bar_unit[1];
            $scope.barTitle = '订单统计'
        }
        //if ($scope.searchWay == '商品类型' || $scope.id) {
        if (!$scope.id && !$scope.searchWay) {
            toast('error', '', '请选择统计方式');
            return false;
        }
        if ( $scope.searchWay == '商品类型' && $scope.isEmpty($scope.summaryArgs.productChoose)) {
                toast('error', '', '请输入商品类型');
                return false;
            }
        if ($scope.isEmpty($scope.unit.timeUnit)) {
            toast('error', '', '请输入时间粒度');
            return;
        }
        if ($scope.summaryArgs.yearStart || $scope.summaryArgs.yearEnd) {
            if(!($scope.summaryArgs.yearStart && $scope.summaryArgs.yearEnd)) {
                toast('error','','请输入完整年份');
                return false;
            }
            switch ($scope.summaryArgs.quarterChooseStart) {
                case '1':
                    summaryargs.startTime = $scope.summaryArgs.yearStart + "-" + "01" + "-" + "01";
                    break;
                case '2':
                    summaryargs.startTime = $scope.summaryArgs.yearStart + "-" + "04" + "-" + "01";
                    break;
                case '3':
                    summaryargs.startTime = $scope.summaryArgs.yearStart + "-" + "07" + "-" + "01";
                    break;
                case '4':
                    summaryargs.startTime = $scope.summaryArgs.yearStart + "-" + "09" + "-" + "01";
                    break;
                default :
                    toast('error','','请输入开始季度');
                    return;
            }
            switch ($scope.summaryArgs.quarterChooseEnd) {
                case '1':
                    summaryargs.endTime = $scope.dateformat(new Date($scope.summaryArgs.yearEnd, 3, 0));
                    break;
                case '2':
                    summaryargs.endTime = $scope.dateformat(new Date($scope.summaryArgs.yearEnd, 6, 0));
                    break;
                case '3':
                    summaryargs.endTime = $scope.dateformat(new Date($scope.summaryArgs.yearEnd, 9, 0));
                    break;
                case '4':
                    summaryargs.endTime = $scope.dateformat(new Date($scope.summaryArgs.yearEnd, 12, 0));
                    break;
                default :
                    toast('error','','请输入结束季度');
                    return;
            }
            $scope.timeShow(summaryargs.startTime,summaryargs.endTime);
            if( !$scope.checkTime(summaryargs.startTime,summaryargs.endTime)) {
                return false;
            }
        } else if ($scope.summaryArgs.dayChooseStart || $scope.summaryArgs.dayChooseEnd) {
            if( !$scope.checkTime($scope.summaryArgs.dayChooseStart,$scope.summaryArgs.dayChooseEnd)) {
                return false;
            }
            $scope.timeShow($scope.summaryArgs.dayChooseStart,$scope.summaryArgs.dayChooseEnd);
            summaryargs.startTime = $scope.summaryArgs.dayChooseStart;
            summaryargs.endTime = $scope.summaryArgs.dayChooseEnd;
        } else if ($scope.summaryArgs.monthChooseStart || $scope.summaryArgs.monthChooseEnd) {
            if(!$scope.checkTime($scope.summaryArgs.monthChooseStart,$scope.summaryArgs.monthChooseEnd)) {
                return false;
            }
            $scope.timeShow($scope.summaryArgs.monthChooseStart,$scope.summaryArgs.monthChooseEnd);
            summaryargs.startTime = $scope.summaryArgs.startTime;
            summaryargs.endTime = $scope.summaryArgs.endTime;
        } else {
            if(!$scope.checkTime($scope.summaryArgs.yearChooseStart,$scope.summaryArgs.yearChooseEnd)) {
                return false;
            }
            $scope.timeShow($scope.summaryArgs.yearChooseStart,$scope.summaryArgs.yearChooseEnd);
            summaryargs.startTime = $scope.summaryArgs.startTime;
            summaryargs.endTime = $scope.summaryArgs.endTime;
        }
        if (!$scope.id) {
            $scope.summaryArgs.type = $scope.searchWay == '商品类型' ? 'byProduct' : 'byTime';
            summaryargs.commodityType= $scope.summaryArgs.productChoose ? $scope.summaryArgs.productChoose.code : '';
        } else {
            $scope.summaryArgs.type = $scope.summaryType[$scope.id - 1] ;
        }

        console.log($scope.summaryArgs);
        //$scope.summaryArgs.productType = $scope.summaryType[$scope.id - 1] || 'COMMODITY';
        summaryargs.topNum = $scope.summaryArgs.topNum ? $scope.summaryArgs.topNum : null;
        summaryargs.timeUnit = $scope.unit.timeUnit;
        summaryargs.type = $scope.summaryArgs.type;

        if ( $scope.id == 4 && $scope.isEmpty(summaryargs.topNum)) {
            toast('error', '', '请输入TOP值');
            return;
        }
        console.log(summaryargs);
        Summary.getSummaryResult(summaryargs).then(function (response) {
            var data = response.data.data;
            $scope.rowData = data;
            console.log($scope.rowData);
            if($scope.rowData === null) {
                toast('error', '没有符合条件的数据');
            }
            if ($scope.nav_title == '商品销售统计') {
                $scope.data = modChartDataFormat($scope.rowData);
            } else if ($scope.id == 1 || $scope.id == 2) {
                $scope.data = chartDataFormat($scope.rowData);
            }
            $scope.id == 4 ? showChart($scope.rowData) : showChart($scope.data);
            showBarChart($scope.data);
            $scope.id == 4 ? $scope.info(summaryargs) : null;
        });
    };

    $scope.modType = false;
    $scope.datachange = function() {
        if($scope.searchWay == '时间') {
            $scope.summaryArgs = {};
            $scope.modType = false;
            //$scope.summaryArgs.productChoose.code = null;
            $scope.unit.timeUnit = null;
            $scope.showYearSelect = false;
            $scope.showQuarter = false;
            $scope.showMonthSelect = false;
            $scope.showDaySelect = true;
            $scope.summaryArgs = {};
        } else if($scope.searchWay == '商品类型'){
            $scope.summaryArgs = {};
            $scope.unit.timeUnit = null;
            $scope.modType = true;
            $scope.showYearSelect = false;
            $scope.showQuarter = false;
            $scope.showMonthSelect = false;
            $scope.showDaySelect = true;
        }
    };

    $scope.ifShow = false;
    $scope.text = "显示详情";
    $scope.toggleShow = function () {
        $scope.ifShow = !$scope.ifShow;
        $scope.text = $scope.text == "显示详情" ? "隐藏" : "显示详情";
    };

    $scope.barcolor = {
        primary: '#7266ba',
        info: '#23b7e5',
        success: '#27c24c',
        warning: '#fad733',
        danger: '#f05050',
        light: '#e8eff0',
        dark: '#3a3f51',
        black: '#1c2b36'
    };
}]);