/**
 * Created by Administrator on 2016/7/17.
 */
(function ($) {
    var prize_list = $('#prize_list');

    $.ajax({
        method: 'GET',
        url: '/api/prize/lastOpen?lotteryType=ssq',
        success: function (response) {
            var prize = JSON.parse(response).data;
            showView(prize);
        }
    });

    function showView(prize) {
        var item = '<div class="prize_item"><div class="prize_item_header">' +
            '<div class="col-xs-6"><p><span>' + prize.period + '</span>期</p></div>' +
            '<div class="col-xs-6"><p class="text-right">奖池: <span>' + prize.fee + '</span> 元</p></div>' +
            '<div style="clear:both;height:0;"></div></div>'
            + '<div class="prize_item_body"><div class="ball_row_center">';
        var balls = prize.number.split('+');
        var reds = balls[0], blue = balls[1];
        reds.split(',').map(function (number) {
            item += '<a class="ball ball_sm ball_red">' + number + '</a>';
        });
        item += '<a class="ball ball_sm ball_blue">' + blue + '</a></div></div></div>';
        prize_list.html(item);
    }
})(Zepto);