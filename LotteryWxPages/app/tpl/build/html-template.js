/*TMODJS:{"version":"1.0.0"}*/
!function(){function a(a,b){return(/string|function/.test(typeof b)?h:g)(a,b)}function b(a,c){return"string"!=typeof a&&(c=typeof a,"number"===c?a+="":a="function"===c?b(a.call(a)):""),a}function c(a){return l[a]}function d(a){return b(a).replace(/&(?![\w#]+;)|[<>"']/g,c)}function e(a,b){if(m(a))for(var c=0,d=a.length;d>c;c++)b.call(a,a[c],c,a);else for(c in a)b.call(a,a[c],c)}function f(a,b){var c=/(\/)[^\/]+\1\.\.\1/,d=("./"+a).replace(/[^\/]+$/,""),e=d+b;for(e=e.replace(/\/\.\//g,"/");e.match(c);)e=e.replace(c,"/");return e}function g(b,c){var d=a.get(b)||i({filename:b,name:"Render Error",message:"Template not found"});return c?d(c):d}function h(a,b){if("string"==typeof b){var c=b;b=function(){return new k(c)}}var d=j[a]=function(c){try{return new b(c,a)+""}catch(d){return i(d)()}};return d.prototype=b.prototype=n,d.toString=function(){return b+""},d}function i(a){var b="{Template Error}",c=a.stack||"";if(c)c=c.split("\n").slice(0,2).join("\n");else for(var d in a)c+="<"+d+">\n"+a[d]+"\n\n";return function(){return"object"==typeof console&&console.error(b+"\n\n"+c),b}}var j=a.cache={},k=this.String,l={"<":"&#60;",">":"&#62;",'"':"&#34;","'":"&#39;","&":"&#38;"},m=Array.isArray||function(a){return"[object Array]"==={}.toString.call(a)},n=a.utils={$helpers:{},$include:function(a,b,c){return a=f(c,a),g(a,b)},$string:b,$escape:d,$each:e},o=a.helpers=n.$helpers;a.get=function(a){return j[a.replace(/^\.\//,"")]},a.helper=function(a,b){o[a]=b},"function"==typeof define?define(function(){return a}):"undefined"!=typeof exports?module.exports=a:this.template=a,/*v:21*/
a("ssqBuy",'<div style="display: flex;border-bottom: 1px solid #cccccc"> <div class="col-xs-6" style="padding: 10px;"> <a href="javascript:;" class="weui_btn weui_btn_default" id="selectBall"><span class="icon">+</span> <span>\u81ea\u9009\u53f7\u7801</span></a> </div> <div class="col-xs-6" style="padding: 10px;"> <a href="javascript:;" class="weui_btn weui_btn_default" id="randomFive"><span class="icon">+</span> <span>\u673a\u9009\u4e94\u6ce8</span></a> </div> </div> <div class="content" style="margin-bottom: 160px;"> <div class="ball_list" id="ball_list"> </div> </div> <div class="footer"> <div style="border-bottom: 1px solid #cccccc"> <div class="col-xs-6"> <div class="spinner" style="border-right: 1px solid #dddddd;"> <span class="text-light">\u8ffd</span> <a class="button" id="periodMinus">-</a> <input type="number" id="period" class="text" value="1"/> <a class="button" style="padding: 3px 8px;" id="periodAdd">+</a> <span class="text-light">\u671f</span> </div> </div> <div class="col-xs-6"> <div class="spinner" > <span class="text-light">\u6295</span> <a class="button" id="multipleMinus">-</a> <input type="number" id="multiple" class="text" value="1"/> <a class="button" style="padding: 3px 8px;" id="multipleAdd">+</a> <span class="text-light">\u500d</span> </div> </div> <div style="clear:both;height:0px;"></div> </div> <div class="content"> <div style="padding: 5px 0;"> <a href="javascript:;" id="submitDoubleBallSelect" class="weui_btn weui_btn_warn">\u7acb\u5373\u4ed8\u6b3e <span id="totalFee">0</span>&nbsp;\u5143</a> </div> <div class="text-center text-light"> <input type="checkbox" value="" id="confirmProto" checked/> <label for="confirmProto" style="font-size: 14px;"> \u6211\u5df2\u9605\u8bfb\u5e76\u540c\u610f\u300a<a href="#" class="text-light text-underline">\u670d\u52a1\u534f\u8bae</a>\u300b\u548c\u300a<a href="#" class="text-light text-underline">\u514d\u5bc6\u534f\u8bae</a>\u300b </label> </div> </div> </div>'),/*v:1*/
a("ssqBuyList",function(a){"use strict";var b=this,c=(b.$helpers,b.$each),d=a.list,e=(a.item,a.i,b.$escape),f=(a.red,a.j,"");return c(d,function(a,b){f+=' <div class="ball_list_item" data-index="',f+=e(b),f+='"> ',c(a.red,function(a){f+=' <p class="text-red">',f+=e(a),f+="</p> "}),f+=" ",c(a.blue,function(a){f+=' <p class="text-info">',f+=e(a),f+="</p> "}),f+=" </div> "}),f+=" ",new k(f)}),/*v:12*/
a("ssqSelect",function(a){"use strict";var b=this,c=(b.$helpers,b.$escape),d=a.lottery,e="";return e+='<div class="notice"> <p><span>',e+=c(d.period),e+="</span>\u671f ",e+=c(d.openTime),e+=" \u5f00\u5956 \u5956\u6c60 ",e+=c(d.totalMoney),e+='</p> </div> <div class="content" style="margin-bottom: 96px;"> <div class="ball_row"> <a href="javascript:void(0);" class="ball ball_red">01</a> <a href="javascript:void(0);" class="ball ball_red">02</a> <a href="javascript:void(0);" class="ball ball_red">03</a> <a href="javascript:void(0);" class="ball ball_red">04</a> <a href="javascript:void(0);" class="ball ball_red">05</a> <a href="javascript:void(0);" class="ball ball_red">06</a> <a href="javascript:void(0);" class="ball ball_red">07</a> <a href="javascript:void(0);" class="ball ball_red">08</a> <a href="javascript:void(0);" class="ball ball_red">09</a> <a href="javascript:void(0);" class="ball ball_red">10</a> <a href="javascript:void(0);" class="ball ball_red">11</a> <a href="javascript:void(0);" class="ball ball_red">12</a> <a href="javascript:void(0);" class="ball ball_red">13</a> <a href="javascript:void(0);" class="ball ball_red">14</a> <a href="javascript:void(0);" class="ball ball_red">15</a> <a href="javascript:void(0);" class="ball ball_red">16</a> <a href="javascript:void(0);" class="ball ball_red">17</a> <a href="javascript:void(0);" class="ball ball_red">18</a> <a href="javascript:void(0);" class="ball ball_red">19</a> <a href="javascript:void(0);" class="ball ball_red">20</a> <a href="javascript:void(0);" class="ball ball_red">21</a> <a href="javascript:void(0);" class="ball ball_red">22</a> <a href="javascript:void(0);" class="ball ball_red">23</a> <a href="javascript:void(0);" class="ball ball_red">24</a> <a href="javascript:void(0);" class="ball ball_red">25</a> <a href="javascript:void(0);" class="ball ball_red">26</a> <a href="javascript:void(0);" class="ball ball_red">27</a> <a href="javascript:void(0);" class="ball ball_red">28</a> <a href="javascript:void(0);" class="ball ball_red">29</a> <a href="javascript:void(0);" class="ball ball_red">30</a> <a href="javascript:void(0);" class="ball ball_red">31</a> <a href="javascript:void(0);" class="ball ball_red">32</a> <a href="javascript:void(0);" class="ball ball_red">33</a> </div> <div class="ball_row"> <a href="javascript:void(0);" class="ball ball_blue">01</a> <a href="javascript:void(0);" class="ball ball_blue">02</a> <a href="javascript:void(0);" class="ball ball_blue">03</a> <a href="javascript:void(0);" class="ball ball_blue">04</a> <a href="javascript:void(0);" class="ball ball_blue">05</a> <a href="javascript:void(0);" class="ball ball_blue">06</a> <a href="javascript:void(0);" class="ball ball_blue">07</a> <a href="javascript:void(0);" class="ball ball_blue">08</a> <a href="javascript:void(0);" class="ball ball_blue">09</a> <a href="javascript:void(0);" class="ball ball_blue">10</a> <a href="javascript:void(0);" class="ball ball_blue">11</a> <a href="javascript:void(0);" class="ball ball_blue">12</a> <a href="javascript:void(0);" class="ball ball_blue">13</a> <a href="javascript:void(0);" class="ball ball_blue">14</a> <a href="javascript:void(0);" class="ball ball_blue">15</a> <a href="javascript:void(0);" class="ball ball_blue">16</a> </div> </div> <div class="footer"> <div class="content"> <div> <p class="text-center" style="margin-bottom: 10px;">\u5df2\u9009\u62e9 <span id="stakeNum" class="text-red" style="font-weight: bold;">0</span> \u6ce8</p> </div> <div class="col-xs-3"> <a href="javascript:;" id="random" class="weui_btn weui_btn_default text-red">\u968f\u673a\u4e00\u6ce8</a> </div> <div class="col-xs-9 pull-right"> <a href="javascript:;" id="submitDoubleBallSelect" class="weui_btn weui_btn_warn">\u4e0b\u4e00\u6b65</a> </div> <div style="clear:both;height:0px;"></div> </div> </div>',new k(e)})}();