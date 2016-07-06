/**
 * EncryptionUtils.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 6, 2015
 */
package cn.wisdom.lottery.payment.common.utils;

import java.util.UUID;

/**
 * EncryptionUtils
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class EncryptionUtils
{
    /**
     * Encrypt the source string into 32 characters.
     * 
     * @param source
     * @return
     */
    public static String encrypt(String source)
    {
        return YmMD5.encrypt(source);
    }

    /**
     * Encrypt the source strings into 32 characters.
     * 
     * @param source
     * @param args
     * @return
     */
    public static String encrypt(String source, String... args)
    {
        StringBuilder sb = new StringBuilder(source);
        for (String arg : args)
        {
            sb.append(arg);
        }

        return YmMD5.stringDigest(sb.toString());
    }

    /**
     * Generate a global unique ID (32 characters).
     * 
     * @return
     */
    public static String generateUUID()
    {
        return EncryptionUtils.encrypt(UUID.randomUUID().toString());
    }

}
