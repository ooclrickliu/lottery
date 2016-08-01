/**
 * YmMD5.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 6, 2015
 */
package cn.wisdom.lottery.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.wisdom.lottery.common.exception.OVTRuntimeException;

/**
 * YmMD5
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class YmMD5
{
    private static final String MD5 = "MD5";

    public static String encrypt(String text)
    {
        return stringDigest(text);
    }

    public static MessageDigest getMessageDigestInstance()
    {
        try
        {
            return MessageDigest.getInstance(MD5);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new OVTRuntimeException("No MD5 algorithem found!", e);
        }
    }

    private static char hexNibble(int num)
    {
        if (num < 10)
        {
            return (char) (num + '0');
        }
        else
        {
            return (char) (num - 10 + 'a');
        }
    }

    /**
     * Calculate 128 bit MD5 hash of byte array, and encodes the result as a 32
     * character hex string.
     */
    public static String stringDigest(byte[] bytes)
    {
        byte[] hash = digest(bytes);
        char[] buf = new char[32];
        for (int i = 0, j = 0; i < hash.length; i++)
        {
            int val = hash[i] & 255;
            buf[j++] = hexNibble(val >> 4);
            buf[j++] = hexNibble(val & 15);
        }
        return new String(buf);
    }

    /**
     * Calculate 128 bit MD5 hash of a string, and encodes the result as a 32
     * character hex string.
     */
    public static String stringDigest(String str)
    {
        return stringDigest(str.getBytes());
    }

    /**
     * Calculate 128 bit MD5 hash of byte array.
     */
    public static byte[] digest(byte[] bytes)
    {
        MessageDigest md = getMessageDigestInstance();
        return md.digest(bytes);
    }

    /**
     * Calculate 128 bit MD5 hash of string.
     */
    public static byte[] digest(String str)
    {
        return digest(str.getBytes());
    }
}
