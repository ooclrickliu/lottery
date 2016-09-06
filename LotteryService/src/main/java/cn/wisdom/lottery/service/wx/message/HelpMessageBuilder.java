package cn.wisdom.lottery.service.wx.message;

import javax.annotation.PostConstruct;

import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.NewsBuilder;

import org.springframework.stereotype.Component;

@Component("help")
public class HelpMessageBuilder implements MessageBuilder {
	
	// 平台介绍
	private WxMpXmlOutNewsMessage.Item ptjsArticle;
	
	// 购买、兑奖
	private WxMpXmlOutNewsMessage.Item gmdjArticle;
	
	// 红包
	private WxMpXmlOutNewsMessage.Item redpackArticle;
	
	// 其他
	private WxMpXmlOutNewsMessage.Item otherArticle;
	
	@PostConstruct
	private void init() {
		buildPTJS();
		buildGMDJ();
		buildRedpack();
		buildOther();
	}
	
	private void buildOther() {
		otherArticle = new WxMpXmlOutNewsMessage.Item();
		otherArticle.setTitle("常见问题 | 其他");
		otherArticle.setDescription("");
		otherArticle.setPicUrl("");
		otherArticle.setUrl("http://cai.southwisdom.cn/#/help");
	}

	private void buildRedpack() {
		redpackArticle = new WxMpXmlOutNewsMessage.Item();
		redpackArticle.setTitle("常见问题 | 红包");
		redpackArticle.setDescription("");
		redpackArticle.setPicUrl("");
		redpackArticle.setUrl("http://cai.southwisdom.cn/#/help");
	}

	private void buildGMDJ() {
		gmdjArticle = new WxMpXmlOutNewsMessage.Item();
		gmdjArticle.setTitle("常见问题 | 购买、兑奖");
		gmdjArticle.setDescription("");
		gmdjArticle.setPicUrl("");
		gmdjArticle.setUrl("http://cai.southwisdom.cn/#/help");
	}

	private void buildPTJS() {
		ptjsArticle = new WxMpXmlOutNewsMessage.Item();
		ptjsArticle.setTitle("常见问题 | 平台介绍");
		ptjsArticle.setDescription("");
		ptjsArticle.setPicUrl("");
		ptjsArticle.setUrl("http://cai.southwisdom.cn/#/help");
	}

	public WxMpXmlOutMessage buildMessage(WxMpXmlMessage wxMessage) {
		
		NewsBuilder news = WxMpXmlOutMessage.NEWS();
		
		// 平台介绍
		news.addArticle(ptjsArticle);
		
		// 购买、兑奖
		news.addArticle(gmdjArticle);
		
		// 红包
		news.addArticle(redpackArticle);
		
		// 其他
		news.addArticle(otherArticle);
		
		news.fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
		
		return news.build();
	}
}
