/**
 * JsonUtils.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 26, 2015
 */
package cn.wisdom.lottery.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.wisdom.lottery.common.exception.OVTErrorCode;
import cn.wisdom.lottery.common.exception.OVTException;

/**
 * JsonUtils
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class JsonUtils
{
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static
    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Convert object to json.
     * 
     * @param object
     * @return
     * @throws OVTException
     */
    public static String toJson(Object object) throws OVTException
    {
        String json = "";

        if (object != null)
        {
            try
            {
                json = objectMapper.writeValueAsString(object);
            }
            catch (JsonProcessingException e)
            {
                final String errMsg = "Failed to convert object to json!";
                throw new OVTException(OVTErrorCode.JSON_CONVERT_ERROR, errMsg,
                        e);
            }
        }

        return json;
    }

    /**
     * Convert json to Object.
     * 
     * @param json
     * @param javaType
     * @return
     * @throws OVTException
     */
    public static <T> T fromJson(String json, Class<T> javaType)
            throws OVTException
    {
        T object = null;
        if (StringUtils.isNotBlank(json))
        {
            try
            {
                object = objectMapper.readValue(json, javaType);
            }
            catch (Exception e)
            {
                final String errMsg = "Failed to convert json to object!";
                throw new OVTException(OVTErrorCode.JSON_CONVERT_ERROR, errMsg,
                        e);
            }
        }

        return object;
    }
}
