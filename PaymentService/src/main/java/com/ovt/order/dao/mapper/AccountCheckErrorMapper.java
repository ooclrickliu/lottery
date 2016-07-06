package com.ovt.order.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ovt.order.dao.constant.DBConstants.TABLES.ACCOUNT_CHECK_ERROR;
import com.ovt.order.dao.vo.AccountCheckError;

public class AccountCheckErrorMapper implements RowMapper<AccountCheckError>
{

    @Override
    public AccountCheckError mapRow(ResultSet rs, int rowNum)
            throws SQLException
    {
        AccountCheckError accountCheckError = new AccountCheckError();

        accountCheckError.setId(rs.getLong(ACCOUNT_CHECK_ERROR.ID));
        accountCheckError
                .setOrderNo(rs.getString(ACCOUNT_CHECK_ERROR.ORDER_NO));
        accountCheckError.setDetail(rs.getString(ACCOUNT_CHECK_ERROR.DETAIL));
        accountCheckError.setRead(rs.getBoolean(ACCOUNT_CHECK_ERROR.ISREAD));
        
        return accountCheckError;
    }
}
