/*TMODJS:{"version":21,"md5":"8325155f3658852df4bc17a797893e3f"}*/
template('ssqBuy','<div style="display: flex;border-bottom: 1px solid #cccccc"> <div class="col-xs-6" style="padding: 10px;"> <a href="javascript:;" class="weui_btn weui_btn_default" id="selectBall"><span class="icon">+</span> <span>自选号码</span></a> </div> <div class="col-xs-6" style="padding: 10px;"> <a href="javascript:;" class="weui_btn weui_btn_default" id="randomFive"><span class="icon">+</span> <span>机选五注</span></a> </div> </div> <div class="content" style="margin-bottom: 160px;"> <div class="ball_list" id="ball_list"> </div> </div> <div class="footer"> <div style="border-bottom: 1px solid #cccccc"> <div class="col-xs-6"> <div class="spinner" style="border-right: 1px solid #dddddd;"> <span class="text-light">追</span> <a class="button" id="periodMinus">-</a> <input type="number" id="period" class="text" value="1"/> <a class="button" style="padding: 3px 8px;" id="periodAdd">+</a> <span class="text-light">期</span> </div> </div> <div class="col-xs-6"> <div class="spinner" > <span class="text-light">投</span> <a class="button" id="multipleMinus">-</a> <input type="number" id="multiple" class="text" value="1"/> <a class="button" style="padding: 3px 8px;" id="multipleAdd">+</a> <span class="text-light">倍</span> </div> </div> <div style="clear:both;height:0px;"></div> </div> <div class="content"> <div style="padding: 5px 0;"> <a href="javascript:;" id="submitDoubleBallSelect" class="weui_btn weui_btn_warn">立即付款 <span id="totalFee">0</span>&nbsp;元</a> </div> <div class="text-center text-light"> <input type="checkbox" value="" id="confirmProto" checked/> <label for="confirmProto" style="font-size: 14px;"> 我已阅读并同意《<a href="#" class="text-light text-underline">服务协议</a>》和《<a href="#" class="text-light text-underline">免密协议</a>》 </label> </div> </div> </div>');