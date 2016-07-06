/**
 * AbstractDao.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package com.ovt.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.ovt.order.dao.exception.DBException;
import com.ovt.order.dao.handler.PostDeleteHandler;
import com.ovt.order.dao.handler.PostSaveHandler;
import com.ovt.order.dao.handler.PostUpdateHandler;

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

    private static final String DB_ERROR_CODE_SAVE = "401";

    private static final String DB_ERROR_CODE_QUERY = "402";

    private static final String DB_ERROR_CODE_UPDATE = "403";
    
    private static final String DB_ERROR_CODE_DELETE = "404";

    private static final long DEFAULT_KEY = 1;

    @Autowired
    private DataAccessorManager dataAccessorManager;

    public int[] saveBatch(final String sql, String errMsg,
            BatchPreparedStatementSetter bpss)
    {
        int[] saveIds;
        try
        {
            saveIds = dataAccessorManager.getJdbcTemplate().batchUpdate(sql,
                    bpss);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_SAVE, errMsg, e);
        }

        return saveIds;
    }

    public long save(final String sql, PostSaveHandler handler, String errMsg,
            boolean returnKey, final Object... args)
    {
        long key = 0;
        if (returnKey)
        {
            key = saveWithKeyReturn(sql, handler, errMsg, args);
        }
        else
        {
            key = save(sql, handler, errMsg, args);
        }

        return key;
    }

    private long save(String sql, PostSaveHandler handler, String errMsg,
            Object... args)
    {
        try
        {
            dataAccessorManager.getJdbcTemplate().update(sql, args);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_SAVE, errMsg, e);
        }

        // post save handle
        if (handler != null)
        {
            handler.handle(null);
        }

        return DEFAULT_KEY;
    }

    private long saveWithKeyReturn(final String sql, PostSaveHandler handler,
            String errMsg, final Object... args)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException
            {
                PreparedStatement ps = con.prepareStatement(sql,
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

        // post save handle
        if (handler != null)
        {
            handler.handle(id);
        }

        return id;
    }

    public void update(String sql, PostUpdateHandler handler, String errMsg,
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

        // post update handle
        if (handler != null)
        {
            handler.handle();
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper,
            String errMsg, Object... args)
    {
        T result = null;
        try
        {
            result = dataAccessorManager.getJdbcTemplate().queryForObject(sql,
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
            result = dataAccessorManager.getJdbcTemplate().queryForObject(sql,
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
            result = dataAccessorManager.getJdbcTemplate().queryForList(sql,
                    elementType, args);

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
            result = dataAccessorManager.getJdbcTemplate().query(sql,
                    rowMapper, args);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        return result;
    }
    
    public List<Map<String,Object>> queryForList(String sql,
            String errMsg, Object... args)
    {
        List<Map<String,Object>> result = null;
        try
        {
            result = dataAccessorManager.getJdbcTemplate().queryForList(sql, args);
            
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }
        
        return result;
    }

    public void delete(String sql, PostDeleteHandler handler, String errMsg,
            Object... args)
    {
        try
        {
            dataAccessorManager.getJdbcTemplate().update(sql, args);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_DELETE, errMsg, e);
        }

        // post update handle
        if (handler != null)
        {
            handler.handle();
        }
    }
}
