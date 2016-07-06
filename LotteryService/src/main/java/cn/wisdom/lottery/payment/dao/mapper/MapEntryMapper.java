/**
 * MapEntryMapper.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 27, 2015
 */
package cn.wisdom.lottery.payment.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;

import org.springframework.jdbc.core.RowMapper;

/**
 * MapEntryMapper
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class MapEntryMapper<K, V> implements RowMapper<Entry<K, V>>
{

    /* (non-Javadoc)
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    @SuppressWarnings("unchecked")
    public Entry<K, V> mapRow(final ResultSet rs, int rowNum) throws SQLException
    {
        final K key = (K) rs.getObject(1);
        final V valueV = (V) rs.getObject(2);
        
        Entry<K, V> entry = new Entry<K, V>()
        {
            public K getKey()
            {
                return key;
            }

            public V getValue()
            {
                return valueV;
            }

            public V setValue(V value)
            {
                return valueV;
            }
        };
        return entry;
    }

}
