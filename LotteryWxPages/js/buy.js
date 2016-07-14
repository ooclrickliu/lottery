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
            ballListInner += '</div><div class="ball_list_item_right"><a href="javascript:;" class="text-light remove" data-index="' + i +
                '"></a></div><div style="clear:both;height:0px;"></div></div>';

            singlePeriodAndOneMultipleFee += getCombineNum(ball.red.length, 6) * getCombineNum(ball.blue.length, 1) * 2;
        });

        ballList.html(ballListInner);
        updateTotalFee();

        $('.remove').on('tap', function (event) {
            var index = $(event.target).attr('data-index');
            savedBalls.splice(index, 1);
            Cookie.setToJson('balls', savedBalls);

            var targetDom = $(event.target).parent();
            targetDom.animate({opacity: 0}, 300, 'linear', function () {
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

    submitDoubleBallSelect.on('tap', function () {
        if (!submitDoubleBallSelect.hasClass('weui_btn_disabled')) {
            //调用支付接口
            console.log(totalFeeText.text());
        }
    });

})(Zepto);