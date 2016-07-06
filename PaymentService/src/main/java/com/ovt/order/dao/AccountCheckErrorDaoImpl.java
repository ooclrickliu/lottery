package com.ovt.order.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.ovt.common.utils.CollectionUtils;
import com.ovt.order.dao.mapper.AccountCheckErrorMapper;
import com.ovt.order.dao.vo.AccountCheckError;

@Repository
public class AccountCheckErrorDaoImpl implements AccountCheckErrorDao
{
    @Autowired
    private DaoHelper daoHelper;

    @Autowired
    private AccountCheckErrorMapper accountCheckErrorMapper;

    private static final String SQL_INSERT_ACCOUNT_CHECK_ERROR = "insert into account_check_error"
            + "(order_no, detail, create_time) values(?, ?, CURRENT_TIMESTAMP)";

    private static final String SQL_GET_ACCOUNT_CHECK_ERROR_LIST = "select id, order_no, detail, create_time"
            + " from account_check_error where is_read = 0";

    private static final String SQL_MARK_AS_READ = "update account_check_error set is_read = 1 where id = ?";

    @Override
    public void save(final List<AccountCheckError> accountCheckErrors)
    {
        if (CollectionUtils.isNotEmpty(accountCheckErrors))
        {
            String errMsg = "Failed to save account check error list";
            daoHelper.saveBatch(SQL_INSERT_ACCOUNT_CHECK_ERROR, errMsg,
                    new BatchPreparedStatementSetter()
                    {

                        @Override
                        public void setValues(PreparedStatement ps, int i)
                                throws SQLException
                        {
                            AccountCheckError accountCheckError = accountCheckErrors
                                    .get(i);
                            ps.setString(1, accountCheckError.getOrderNo());
                            ps.setString(2, accountCheckError.getDetail());
                        }

                        @Override
                        public int getBatchSize()
                        {
                            return accountCheckErrors.size();
                        }
                    });
        }
    }

    @Override
    public List<AccountCheckError> getAccountCheckErrorList()
    {
        String errMsg = "Failed to query account check error list";

        List<AccountCheckError> accountCheckErrors = daoHelper.queryForList(
                SQL_GET_ACCOUNT_CHECK_ERROR_LIST, accountCheckErrorMapper,
                errMsg);
        return accountCheckErrors;
    }

    @Override
    public void markAccountCheckErrorAsRead(long id)
    {
        String errMsg = "Failed to update accout check error read state";
        
        daoHelper.update(SQL_MARK_AS_READ, null, errMsg, id);
    }

}
