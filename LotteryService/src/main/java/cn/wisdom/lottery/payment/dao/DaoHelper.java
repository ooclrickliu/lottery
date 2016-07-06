/**
 * AbstractDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package cn.wisdom.lottery.payment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.wisdom.lottery.payment.dao.accessor.DataAccessorManager;
import cn.wisdom.lottery.payment.dao.exception.DBException;

/**
 * AbstractDao
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
@Component
public class DaoHelper
{

    public static final String DB_ERROR_CODE_SAVE = "SaveFailed";

    public static final String DB_ERROR_CODE_QUERY = "QueryFailed";

    public static final String DB_ERROR_CODE_UPDATE = "UpdateFailed";
    
    private static final long DEFAULT_KEY = 1;

    @Autowired
    private DataAccessorManager dataAccessorManager;

    public long save(final String sql,
            String errMsg, boolean returnKey, final Object... args)
    {
        long key = 0;
        if (returnKey)
        {
            key = saveWithKeyReturn(sql, errMsg, args);
        }
        else
        {
            key = save(sql, errMsg, args);
        }

        return key;
    }

    private long save(String sql , String errMsg,Object... args)
    {
        try
        {
            dataAccessorManager.getJdbcTemplate().update(sql, args);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_SAVE, errMsg, e);
        }

        return DEFAULT_KEY;
    }

    private long saveWithKeyReturn(final String sql, 
            String errMsg, final Object... args)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException
            {
                PreparedStatement ps =
                        con.prepareStatement(sql,
                                Statement.RETURN_GENERATED_KEYS);
                for (int i = 1; i <= args.length; i++)
                {
                    ps.setObject(i, args[i - 1]);
                }
                return ps;
            }

        };

        try
        {
            dataAccessorManager.getJdbcTemplate().update(psc, keyHolder);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_SAVE, errMsg, e);
        }

        Number key = keyHolder.getKey();
        long id = key != null ? key.longValue() : -1;

        return id;
    }

    public void update(String sql, String errMsg,
            Object... args)
    {
        try
        {
            dataAccessorManager.getJdbcTemplate().update(sql, args);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_UPDATE, errMsg, e);
        }
    }
    
    public void batchUpdate(String sql, List<Object[]> batchArgs, String errMsg) 
    {
    	try
        {
            dataAccessorManager.getJdbcTemplate().batchUpdate(sql, batchArgs);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_UPDATE, errMsg, e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper,
            String errMsg, Object... args)
    {
        T result = null;
        try
        {
            result =
                    dataAccessorManager.getJdbcTemplate().queryForObject(sql,
                            rowMapper, args);
        }
        catch (EmptyResultDataAccessException e)
        {
            result = null;
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        return result;
    }

    public <T> T queryForObject(String sql, Class<T> elementType,
            String errMsg, Object... args)
    {
        T result = null;
        try
        {
            result =
                    dataAccessorManager.getJdbcTemplate().queryForObject(sql,
                            elementType, args);
        }
        catch (EmptyResultDataAccessException e)
        {
            result = null;
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        return result;
    }

    public <T> List<T> queryForList(String sql, Class<T> elementType,
            String errMsg, Object... args)
    {
        List<T> result = null;
        try
        {
            result =
                    dataAccessorManager.getJdbcTemplate().queryForList(sql,
                            elementType, args);

        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        return result;
    }
    
    public List<Map<String, Object>> queryForList(String sql,
            String errMsg, Object... args)
            {
        List<Map<String, Object>> result = null;
        try
        {
            result =
                    dataAccessorManager.getJdbcTemplate().queryForList(sql,
                            args);
            
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }
        
        return result;
            }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper,
            String errMsg, Object... args)
    {
        List<T> result = null;
        try
        {
            result =
                    dataAccessorManager.getJdbcTemplate().query(sql, rowMapper,
                            args);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        return result;
    }
}
