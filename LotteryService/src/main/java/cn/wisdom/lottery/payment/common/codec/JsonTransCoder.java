/**
 * JsonTransCoder.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Apr 7, 2016
 */
package cn.wisdom.lottery.payment.common.codec;

import java.io.IOException;

import cn.wisdom.lottery.payment.common.exception.OVTException;
import cn.wisdom.lottery.payment.common.exception.OVTRuntimeException;
import cn.wisdom.lottery.payment.common.utils.JsonUtils;
import com.schooner.MemCached.ObjectTransCoder;
import com.schooner.MemCached.SockOutputStream;

/**
 * JsonTransCoder
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class JsonTransCoder extends ObjectTransCoder
{

    /*
     * (non-Javadoc)
     * 
     * @see com.schooner.MemCached.TransCoder#encode(com.schooner.MemCached.
     * SockOutputStream, java.lang.Object)
     */
    @Override
    public int encode(SockOutputStream out, Object object) throws IOException
    {
        
        final boolean isPrimitive = object.getClass().isPrimitive();
        
        if (isPrimitive)
        {
            return super.encode(out, object);
        }
        
        String json = null;
        try
        {
            json = JsonUtils.toJson(object);
        }
        catch (OVTException e)
        {
            throw new OVTRuntimeException("JsonConvertError",
                    "Failed to convert object to json!", e);
        }
        
        return super.encode(out, json);
    }

}
