package cn.wisdom.lottery.service.wx.message;

import javax.annotation.PostConstruct;

import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.result.WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.common.log.Logger;
import cn.wisdom.lottery.common.log.LoggerFactory;
import cn.wisdom.lottery.common.utils.StringUtils;
import cn.wisdom.lottery.dao.vo.AppProperty;
import cn.wisdom.lottery.service.wx.WXService;

@Component
public class TrendMessageBuilder implements MessageBuilder {
	
	@Autowired
	private WxMpEventHandler wxMpEventHandler;
	
	@Autowired
	private WXService wxService;

	@Autowired
	private AppProperty appProperty;
	
	private Logger logger = LoggerFactory.getLogger(TrendMessageBuilder.class.getName());
	
	@PostConstruct
	private void init() {
		wxMpEventHandler.registerMessageBuilder(this);
	}

	@Override
	public WxMpXmlOutMessage buildMessage(WxMpXmlMessage wxMessage) {
		WxMpXmlOutMessage response = null;
		try {
			int batchSize = 10;
			int index = 0;
			String mediaId = "";
			WxMpMaterialFileBatchGetResult images = wxService.getWxMpService().materialFileBatchGet("image", index, batchSize);
			boolean hasMore = images != null && images.getItemCount() == batchSize;
			
			while (StringUtils.isBlank(mediaId) && hasMore) {
				images = wxService.getWxMpService().materialFileBatchGet("image", index, batchSize);

				for (WxMaterialFileBatchGetNewsItem item : images.getItems()) {
					if (StringUtils.equalsIgnoreCase("trend.png", item.getName())) {
						mediaId = item.getMediaId();
						break;
					}
				}
				if (StringUtils.isNotBlank(mediaId)) {
					response = WxMpXmlOutMessage.IMAGE().toUser(wxMessage.getFromUserName())
							.fromUser(wxMessage.getToUserName()).mediaId(mediaId)
							.build();
					break;
				}
			}
			if (StringUtils.isBlank(mediaId)) {
				logger.error("failed to get trend.png from wx.");
			}
		} catch (Exception e) {
			logger.error("failed to get trend.png from wx.", e);
		}

		return response;
	}

	@Override
	public String getMenuKey() {
		return "trend";
	}

}
