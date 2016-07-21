/**
 * Created by Administrator on 2016/6/25.
 */
"use strict";

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}

var Cookie = {
    get: function (name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    },
    set: function (name, value) {
        document.cookie = name + "=" + value;
    },
    getFromJson: function (name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg))
            return JSON.parse(unescape(arr[2]));
        else
            return null;
    },
    setToJson: function (name, value) {
        document.cookie = name + "=" + JSON.stringify(value);
    }
};

var getCombineNum = function (total, select) {
    if (total <= 0 || select <= 0 || total < select) {
        return 0;
    }

    var length = select;
    if (total < select * 2) {
        length = total - select;
    }

    if (length == 0) {
        return 1;
    }

    var combineNum = 1;
    for (var i = 0; i < length; i++) {
        combineNum *= (total - i);
    }
    var sub = 1;
    for (var j = length; j >= 1; j--) {
        sub *= j;
    }
    combineNum /= sub;

    return combineNum;
};

var ballsText = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33"];

var genRandom = function (max) {
    return parseInt(max * Math.random());
};

var getRandomReds = function () {
    var red = [];
    var tempBalls = [].concat(ballsText);
    for (var i = 0; i < 6; i++) {
        var index = genRandom(33 - i);
        red[i] = tempBalls[index];
        tempBalls.splice(index, 1);
    }
    return red.sort();
};

var getRandomBlue = function () {
    return [ballsText[genRandom(16)]];
};

var stringifyBalls = function (balls) {
    return balls.map(function (ball) {
        return ball.red.toString() + '+' + ball.blue.toString();
    });
};

var week = ['日', '一', '二', '三', '四', '五', '六'];
var getTimeStr = function (time) {
    var date = new Date(time);
    var retStr = '周'+week[date.getDay()] + ' ';
    retStr += date.getHours() + ':';
    var minute = date.getMinutes();
    if(minute < 10) minute = '0'+minute;
    retStr += minute;
    return retStr;
};