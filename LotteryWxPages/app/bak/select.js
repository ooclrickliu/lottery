/**
 * Created by Administrator on 2016/6/23.
 */
"use strict";
;
(function ($) {
    var deviceWidth = window.outerWidth;
    if (deviceWidth >= 320) {
        $('.ball_row').css("padding-left", Math.floor((deviceWidth - 320) % 51 / 2));
    }

    var selectBalls = {red: [], blue: []};
    var savedBallsCopy = Cookie.getFromJson('balls') || [];
    var savedBalls = [];
    var red_balls = $('.ball_red'),
        blue_balls = $('.ball_blue'),
        stakeNum = $('#stakeNum'),
        randomBtn = $('#random'),
        currentPeriod = $('#currentPeriod'),
        currentOpenTime = $('#currentOpenTime'),
        currentTotal = $('#currentTotal'),
        submitDoubleBallSelect = $("#submitDoubleBallSelect");

    $.ajax({
        method: 'GET', url: '/api/lottery/currentPeriod?lotteryType=ssq',
        success: function (response) {
            var lottery = JSON.parse(response).data;
            currentPeriod.text(lottery.period);
            currentOpenTime.text(getTimeStr(lottery.openTime));
            currentTotal.text(lottery.totalMoney);
        }
    });

    updateStateNum();
    red_balls.on('tap', function (event) {
        var length = $('.ball_red.active').length,
            target = $(event.target);

        if (target.hasClass('active')) {
            target.removeClass('active');
            selectBalls.red.splice(selectBalls.red.indexOf(target.text()), 1);
        } else {
            target.addClass('active');
            selectBalls.red[length] = target.text();
        }
        updateStateNum();
    });

    blue_balls.on('tap', function (event) {
        var length = $('.ball_blue.active').length,
            target = $(event.target);

        if (target.hasClass('active')) {
            target.removeClass('active');
            selectBalls.blue.splice(selectBalls.blue.indexOf(target.text()), 1);
        } else {
            target.addClass('active');
            selectBalls.blue[length] = target.text();
        }
        updateStateNum();
    });

    randomBtn.on('tap', function () {
        selectBalls = {red: getRandomReds(), blue: getRandomBlue()};
        updateView(selectBalls);
        updateStateNum();
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
        stakeNum.text(stake);
    }

    submitDoubleBallSelect.on('tap', function () {
        if (savedBalls.length > 0 && stakeNum.text() > 0) {
            Cookie.setToJson('balls', savedBalls);
            window.location.href = "ball_buy.html";
        }
    });

    var updateView = function (balls) {
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
    };
})(Zepto);