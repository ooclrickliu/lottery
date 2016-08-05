/**
 * Created by leo.liu on 2016/8/2.
 */
(function ($) {
    "use strict";

    var router = new Router({
        container: '#container'
    });

    var serverBusy = "<p style=\"margin-top: 50%;text-align: center;\">Sorry! 服务器繁忙,请稍后再试</p>";

    var ssqSelect = {
        url: '/select',
        className: 'ssq-select',
        render: function () {
            return new Promise(function (resolve, reject) {
                $.getJSON('/api/lottery/period/current?lotteryType=ssq', function (response) {
                    if (response.stateCode == "SUCCESS") {
                        resolve(template('ssqSelect', {lottery: response.data}));
                        adjustPage();
                    } else {
                        resolve(serverBusy);
                    }
                }, function () {resolve(serverBusy);});
            })
        },
        bind: ssqSelectController
    };

    var ssqBuy = {
        url: '/buy',
        className: 'ssq-buy',
        render: function () {
            var savedBalls = Cookie.getFromJson('balls') || [];
            return new Promise(function (resolve, reject) {
                resolve(template('ssqBuy', {list: savedBalls}));
            })
        },
        bind: ssqBuyController
    };

    router.push(ssqSelect)
        .push(ssqBuy)
        .setDefault('/select').init();

    Cookie.set('openid', 'olz_hvsELAlrfI_0715gnh8un04Q');
    $.getJSON('/api/user/current?code=' + GetQueryString('code') + '&openid=' + Cookie.get('openid'),
        function (response) {
            if(response.stateCode == 'SUCCESS'){
                var user = response.data;
                Cookie.set('openid', user.openid);
            } else {
                $('#container').html(serverBusy);
            }
        },
        function (response) {
            $('#container').html(serverBusy);
        }
    );

    if (/Android/gi.test(navigator.userAgent)) {
        window.addEventListener('resize', function () {
            if (document.activeElement.tagName == 'INPUT' || document.activeElement.tagName == 'TEXTAREA') {
                window.setTimeout(function () {
                    document.activeElement.scrollIntoViewIfNeeded();
                }, 0);
            }
        })
    }

    function adjustPage() {
        var deviceWidth = window.outerWidth;
        if (deviceWidth >= 320) {
            $('.ball_row').css("padding-left", Math.floor((deviceWidth - 320) % 51 / 2));
        }
    }

    function ssqSelectController() {
        var selectBalls = {red: [], blue: []};
        var savedBallsCopy = Cookie.getFromJson('balls') || [];
        var savedBalls = [];

        updateStateNum();

        $('#container').off().on('tap', '.ball_red', function () {
            var length = $('.ball_red.active').length,
                target = $(this);
            if (target.hasClass('active')) {
                target.removeClass('active');
                selectBalls.red.splice(selectBalls.red.indexOf(target.text()), 1);
            } else {
                target.addClass('active');
                selectBalls.red[length] = target.text();
            }
            updateStateNum();
        }).on('tap', '.ball_blue', function () {
            var length = $('.ball_blue.active').length,
                target = $(this);
            if (target.hasClass('active')) {
                target.removeClass('active');
                selectBalls.blue.splice(selectBalls.blue.indexOf(target.text()), 1);
            } else {
                target.addClass('active');
                selectBalls.blue[length] = target.text();
            }
            updateStateNum();
        }).on('tap', '#random', function () {
            selectBalls = {red: getRandomReds(), blue: getRandomBlue()};
            updateView(selectBalls);
            updateStateNum();
        }).on('tap', '#submitDoubleBallSelect', function () {
            if (savedBalls.length > 0 && $('#stakeNum').text() > 0) {
                Cookie.setToJson('balls', savedBalls);
                window.location.hash = '#/buy';
            }
        });

        function updateStateNum() {
            savedBalls = $.extend([], savedBallsCopy);
            if (selectBalls.red.length >= 6 && selectBalls.blue.length >= 1) {
                if (JSON.stringify(savedBalls).indexOf(JSON.stringify(selectBalls)) < 0) {
                    savedBalls.push(selectBalls);
                }
            }
            var stake = 0;
            $.each(savedBalls, function (index, ball) {
                stake += getCombineNum(ball.red.length, 6) * getCombineNum(ball.blue.length, 1);
            });
            $('#stakeNum').text(stake);
        }

        function updateView(balls) {
            var red_balls = $('.ball_red'), blue_balls = $('.ball_blue');
            red_balls.removeClass('active');
            blue_balls.removeClass('active');

            red_balls.each(function (index, ballItem) {
                if (balls.red.indexOf($(ballItem).text()) >= 0) {
                    $(ballItem).addClass('active');
                }
            });
            blue_balls.each(function (index, ballItem) {
                if (balls.blue.indexOf($(ballItem).text()) >= 0) {
                    $(ballItem).addClass('active');
                }
            });
        }
    }

    function ssqBuyController() {
        var periodInput = $('#period'),
            multipleInput = $('#multiple');
        var savedBalls = Cookie.getFromJson('balls') || [];
        var singlePeriodAndOneMultipleFee = 0;

        updateView();
        $('#container').off()
        .on('tap', '#randomFive', function () {
            for (var i = 0; i < 5;) {
                var param = {red: getRandomReds(), blue: getRandomBlue()};
                if (savedBalls.indexOf(param) < 0) {
                    savedBalls.push(param);
                    i++;
                }
            }
            Cookie.setToJson('balls', savedBalls);
            updateView();
        })
        .on('tap', '#selectBall', function () {
            history.go(-1);
        })
        .on('input', ['#period', '#multiple'], updateTotalFee)
        .on('tap', '#periodMinus', function () {
            var period = periodInput.val();
            if (period > 1) {
                $('#period').val(--period);
                updateTotalFee();
            }
        })
        .on('tap', '#periodAdd', function () {
            var period = periodInput.val();
            $('#period').val(++period);
            updateTotalFee();
        })
        .on('tap', '#multipleMinus', function () {
            var multiple = multipleInput.val();
            if (multiple > 1) {
                $('#multiple').val(--multiple);
                updateTotalFee();
            }
        })
        .on('tap', '#multipleAdd', function () {
            var multiple = multipleInput.val();
            $('#multiple').val(++multiple);
            updateTotalFee();
        })
        .on('change', '#confirmProto', function () {
            var submitDoubleBallSelect = $('#submitDoubleBallSelect');
            if ($(this).attr('checked')) {
                submitDoubleBallSelect.removeClass('weui_btn_disabled');
            } else {
                submitDoubleBallSelect.addClass('weui_btn_disabled');
            }
        })
        .on('tap', '.ball_list_item', function () {
            var _this = $(this);
            _this.hide();
            savedBalls.splice(_this.attr('data-index'), 1);
            Cookie.setToJson('balls', savedBalls);
            updateView();
        })
        .on('tap', '#submitDoubleBallSelect', function () {
            if (!$('#submitDoubleBallSelect').hasClass('weui_btn_disabled')) {
                //调用支付接口
                $.ajax({
                    type: 'POST',
                    url: '/api/lottery/ssq/create?tradeType=JSAPI&body=test',
                    contentType: "application/json",
                    dataType:'json',
                    data: JSON.stringify({
                        numbers: stringifyBalls(savedBalls),
                        periods: periodInput.val(),
                        times: multipleInput.val()
                    }),
                    success: function (response) {
                        alert(JSON.stringify(response));
                        var payInfo = response.data;
                        pay(generatePayHandle(payInfo));
                    },
                    error: function (response) {
                        alert(JSON.stringify(response));
                    }
                });
            }
        });

        function updateView() {
            $('#ball_list').html(template('ssqBuyList', {list: savedBalls}));
            singlePeriodAndOneMultipleFee = 0;
            $.each(savedBalls, function (i, ball) {
                singlePeriodAndOneMultipleFee += getCombineNum(ball.red.length, 6) * getCombineNum(ball.blue.length, 1) * 2;
            });
            updateTotalFee();
        }
        function updateTotalFee() {
            $('#totalFee').text(singlePeriodAndOneMultipleFee * periodInput.val() * multipleInput.val());
        }
    }

    function generatePayHandle(payInfo) {
      return function () {
          WeixinJSBridge.invoke(
              'getBrandWCPayRequest', {
                  "appId": payInfo.appId,
                  "timeStamp": payInfo.timeStamp,
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
    }

    function pay(payHandle) {
        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', payHandle, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', payHandle);
                document.attachEvent('onWeixinJSBridgeReady', payHandle);
            }
        } else {
            payHandle();
        }
    }
})(Zepto);