/**
 * Created by Administrator on 2016/6/23.
 */
"use strict";
;
(function ($) {
    var totalFeeText = $('#totalFee'),
        periodInput = $('#period'),
        multipleInput = $('#multiple'),
        submitDoubleBallSelect = $('#submitDoubleBallSelect');
    var savedBalls = Cookie.getFromJson('balls') || [];
    var singlePeriodAndOneMultipleFee;
    var ballList = $('#ball_list');
    var updateView = function () {
        var ballListInner = '';
        singlePeriodAndOneMultipleFee = 0;
        $.each(savedBalls, function (i, ball) {
            ballListInner += '<div class="ball_list_item"><div class="ball_list_item_left">';
            $.each(ball.red, function (j, red) {
                ballListInner += '<p class="text-red">' + red + '</p>';
            });
            $.each(ball.blue, function (j, blue) {
                ballListInner += '<p class="text-info">' + blue + '</p>';
            });
            ballListInner += '</div><div class="ball_list_item_right remove" data-index="' + i +
                '"><a href="javascript:;" class="text-light" ></a></div><div style="clear:both;height:0;"></div></div>';

            singlePeriodAndOneMultipleFee += getCombineNum(ball.red.length, 6) * getCombineNum(ball.blue.length, 1) * 2;
        });

        ballList.html(ballListInner);
        updateTotalFee();

        var ballListItems = $('.ball_list_item');
        ballListItems.each(function (index, item) {
            var left = $(item).find('.ball_list_item_left');
            var right = $(item).find('.ball_list_item_right');
            if (left.height() > right.height()) {
                var height = Math.floor((left.height() - right.height()) / 2);
                right.css("padding-top", 10+height);
                right.css("padding-bottom", 11+height);
            }
        });

        $('.remove').on('tap', function (event) {
            var index = $(event.target).attr('data-index');
            savedBalls.splice(index, 1);
            Cookie.setToJson('balls', savedBalls);

            var targetDom = $(event.target).parent();
            targetDom.animate({
                'margin-left': '-400px',
                'opacity': 0
            }, 200, 'ease', function () {
                updateView();
            })
        });
    };
    updateView();

    $('#selectBall').on('tap', function () {
        window.location.href = 'ball_select.html';
    });

    $('#randomFive').on('tap', function () {
        for (var i = 0; i < 5;) {
            var param = {red: getRandomReds(), blue: getRandomBlue()};
            if (savedBalls.indexOf(param) < 0) {
                savedBalls.push(param);
                i++;
            }
        }
        Cookie.setToJson('balls', savedBalls);
        updateView();
    });

    function updateTotalFee() {
        totalFeeText.text(singlePeriodAndOneMultipleFee * periodInput.val() * multipleInput.val());
    }

    $('#periodMinus').on('tap', function () {
        var period = periodInput.val();
        if (period > 1) {
            periodInput.val(--period);
            updateTotalFee();
        }
    });
    $('#periodAdd').on('tap', function () {
        var period = periodInput.val();
        periodInput.val(++period);
        updateTotalFee();
    });
    $('#multipleMinus').on('tap', function () {
        var multiple = multipleInput.val();
        if (multiple > 1) {
            multipleInput.val(--multiple);
            updateTotalFee();
        }
    });
    $('#multipleAdd').on('tap', function () {
        var multiple = multipleInput.val();
        multipleInput.val(++multiple);
        updateTotalFee();
    });

    periodInput.bind('input', updateTotalFee);
    multipleInput.bind('input', updateTotalFee);

    $('#confirmProto').bind('change', function () {
        if ($(this).attr('checked')) {
            submitDoubleBallSelect.removeClass('weui_btn_disabled');
        } else {
            submitDoubleBallSelect.addClass('weui_btn_disabled');
        }

    });

    var payInfo;
    submitDoubleBallSelect.on('tap', function () {
        if (!submitDoubleBallSelect.hasClass('weui_btn_disabled')) {
            //调用支付接口
            $.ajax({
                type: 'POST',
                url: '/api/lottery/order/create/ssq',
                contentType: "application/json",
                data: JSON.stringify({
                    numbers: stringifyBalls(savedBalls),
                    periods: periodInput.val(),
                    times: multipleInput.val()
                }),
                success: function (response) {
                    payInfo = response.data;
                    pay();
                }
            });
        }
    });

    function onBridgeReady() {
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', {
                "appId": payInfo.appId,
                "timeStamp": payInfo.timestamp,
                "nonceStr": payInfo.nonceStr,
                "package": payInfo.package,
                "signType": payInfo.signType,
                "paySign": payInfo.paySign
            },
            function (res) {
                if (res.err_msg == "get_brand_wcpay_request:ok") {
                    alert("支付成功");
                }
            }
        );
    }

    function pay() {
        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
        } else {
            onBridgeReady();
        }
    }
})(Zepto);