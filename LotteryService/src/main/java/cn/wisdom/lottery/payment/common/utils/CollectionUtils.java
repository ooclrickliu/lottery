/**
 * CollectionUtils.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.payment.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CollectionUtils
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[Common] 1.0
 */
public class CollectionUtils
{
    /**
     * Check if the collection is not empty.
     * 
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection)
    {
        return !isEmpty(collection);
    }

    /**
     * Check if the map is not empty.
     * 
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * Check if the collection is empty.
     * 
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection)
    {
        return collection == null || collection.size() == 0;
    }

    /**
     * Check if the map is empty.
     * 
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return map == null || map.size() == 0;
    }

    /**
     * Get the 1st element of the collection.
     * 
     * @param collection
     * @return
     */
    public static <T> T getFirstItem(Collection<T> collection)
    {
        if (isEmpty(collection))
        {
            return null;
        }
        for (T item : collection)
        {
            return item;
        }
        return null;
    }

    /**
     * Convert the collection to long set.
     * 
     * @param collection
     * @return
     */
    public static <T> Set<Long> toLongSet(Collection<T> collection)
    {
        Set<Long> longSet = new LinkedHashSet<Long>();
        for (T longVal : collection)
        {
            longSet.add(DataConvertUtils.toLong(longVal));
        }

        return longSet;
    }

    /**
     * Convert array to linked hash set.
     * 
     * @param source
     * @return
     */
    public static <T> Set<T> getSetFromArray(T[] source)
    {
        if (source == null || source.length == 0)
        {
            return null;
        }

        Set<T> set = new LinkedHashSet<T>();
        if (source != null && source.length > 0)
        {
            for (T item : source)
            {
                set.add(item);
            }
        }
        return set;
    }

    /**
     * Convert array to array list.
     * 
     * @param source
     * @return
     */
    public static <T> List<T> getListFromArray(T[] source)
    {
        if (source == null || source.length == 0)
        {
            return null;
        }

        List<T> set = new ArrayList<T>(source.length);
        for (T item : source)
        {
            set.add(item);
        }
        return set;
    }

    /**
     * Convert the collection to linked list.
     * 
     * @param items
     * @return
     */
    public static <T> List<T> toList(Collection<T> items)
    {
        return new LinkedList<T>(items);
    }

    /**
     * Convert the collection to linked hash set.
     * 
     * @param items
     * @return
     */
    public static <T> Set<T> toSet(Collection<T> items)
    {
        return new LinkedHashSet<T>(items);
    }

    /**
     * Sort the collection and return as list.
     * 
     * @param items
     * @return
     */
    public static <T extends Comparable<? super T>> List<T> sort(Collection<T> items)
    {
        List<T> list = toList(items);
        Collections.sort(list);
        
        return list;
    }
    
}
