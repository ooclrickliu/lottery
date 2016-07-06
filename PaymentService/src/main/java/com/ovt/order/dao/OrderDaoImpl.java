/**
 * OrderDaoImpl.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * 2015年12月21日
 */
package com.ovt.order.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;

import com.ovt.common.exception.OVTException;
import com.ovt.common.log.Logger;
import com.ovt.common.log.LoggerFactory;
import com.ovt.common.utils.CollectionUtils;
import com.ovt.common.utils.DataConvertUtils;
import com.ovt.common.utils.DateTimeUtils;
import com.ovt.common.utils.JsonUtils;
import com.ovt.common.utils.StringUtils;
import com.ovt.order.dao.cache.RedisKeyUtils;
import com.ovt.order.dao.cache.RedisManager;
import com.ovt.order.dao.constant.DBConstants.TABLES.ORDER;
import com.ovt.order.dao.constant.OrderState;
import com.ovt.order.dao.handler.PostSaveHandler;
import com.ovt.order.dao.mapper.OrderItemMapper;
import com.ovt.order.dao.mapper.OrderMapper;
import com.ovt.order.dao.mapper.OrderWithPaytimeMapper;
import com.ovt.order.dao.mapper.OrderWithRefundtimeMapper;
import com.ovt.order.dao.vo.Order;
import com.ovt.order.dao.vo.OrderItem;
import com.ovt.order.dao.vo.PageInfo;

/**
 * OrderDaoImpl
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Repository
public class OrderDaoImpl implements OrderDao
{

    private static final String ELEMENT_NULL = "N";

    @Autowired
    private DaoHelper daoHelper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderWithPaytimeMapper orderWithPaytimeMapper;

    @Autowired
    private OrderWithRefundtimeMapper orderWithRefundtimeMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private RedisManager redisManager;

    private static final Logger logger = LoggerFactory
            .getLogger(OrderDaoImpl.class.getName());

    public static final String SQL_INSERT_ORDER = "INSERT INTO `order`(order_no, "
            + " order_state, order_total_fee, refunded_fee,"
            + " user_id, create_by, order_remark, is_delete, extra_1, extra_2, extra_3, "
            + " create_time, update_time) "
            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

    public static final String SQL_INSERT_ORDERITEM = "INSERT INTO order_item(order_id, "
            + " item_no, item_name, item_price, item_num)"
            + " VALUES(?, ?, ?, ?, ?)";

    public static final String SQL_GET_ORDER_PERFIX = "SELECT id, order_no, order_state, order_total_fee, refunded_fee, user_id, create_by,"
            + " create_time, update_time, order_remark, is_delete, extra_1, extra_2, extra_3 FROM `order`";

    public static final String SQL_GET_ORDER = SQL_GET_ORDER_PERFIX
            + " WHERE (order_no = ? AND is_delete = 0)";

    public static final String SQL_GET_ORDER_LIST_BY_ORDER_NOS = SQL_GET_ORDER_PERFIX
            + " WHERE (order_no in ({0}) AND is_delete = 0)";

    public static final String SQL_GET_ORDER_LIST_BY_USER_ID = SQL_GET_ORDER_PERFIX
            + " WHERE (user_id = ? AND is_delete = 0)" + " ORDER BY id DESC";

    public static final String SQL_GET_PAID_ORDER_LIST_BY_TIME_SCOPE = "SELECT `order`.id, `order`.order_no, order_state, order_total_fee, refunded_fee, user_id, create_by, `order`.create_time, `order`.update_time, order_remark, `order`.is_delete, extra_1, extra_2, extra_3, pay_time"
            + " FROM `order` INNER JOIN `payment` ON `order`.order_no = `payment`.order_no"
            + " WHERE `payment`.pay_time >= ? AND `payment`.pay_time <= ? AND `payment`.pay_state = 'TRADE_SUCCESS' AND `order`.create_by >= '0' ;";

    public static final String SQL_GET_ALL_PAID_ORDER_LIST_ORDER_BY_PAY_TIME = "SELECT `order`.id, `order`.order_no, order_state, order_total_fee, refunded_fee, user_id, create_by, `order`.create_time, `order`.update_time, order_remark, `order`.is_delete, extra_1, extra_2, extra_3, pay_time"
            + " FROM `order` INNER JOIN `payment` ON `order`.order_no = `payment`.order_no"
            + " WHERE `payment`.pay_state = 'TRADE_SUCCESS' AND `order`.create_by >= '0' ORDER BY `payment`.pay_time ASC";

    public static final String SQL_GET_REFUNDED_ORDER_LIST_BY_TIME_SCOPE = "SELECT `order`.id, `order`.order_no, order_state, order_total_fee, refunded_fee, user_id, `order`.create_by, `order`.create_time, `order`.update_time, order_remark, is_delete, extra_1, extra_2, extra_3, `refund`.refund_time"
            + " FROM (`order` INNER JOIN `refund_request` ON `order`.order_no = `refund_request`.order_no) INNER JOIN `refund` ON `refund_request`.ID = `refund`.refund_request_id"
            + " WHERE `refund`.refund_time >= ? AND `refund`.refund_time <= ? AND `refund`.refund_state = 'SUCCESS' AND `order`.create_by >= '0'";

    public static final String SQL_GET_ALL_REFUNDED_ORDER_LIST_ORDER_BY_REFUND_TIME = "SELECT `order`.id, `order`.order_no, order_state, order_total_fee, refunded_fee, user_id, `order`.create_by, `order`.create_time, `order`.update_time, order_remark, is_delete, extra_1, extra_2, extra_3, `refund`.refund_time"
            + " FROM (`order` INNER JOIN `refund_request` ON `order`.order_no = `refund_request`.order_no) INNER JOIN `refund` ON `refund_request`.ID = `refund`.refund_request_id"
            + " WHERE `refund`.refund_state = 'SUCCESS' AND `order`.create_by >= '0' ORDER BY `refund`.refund_time ASC";

    public static final String SQL_GET_ORDER_LIST_BY_STATE = SQL_GET_ORDER_PERFIX
            + " WHERE (order_state = ? AND is_delete = 0)"
            + " ORDER BY {0} {1} LIMIT {2}, {3}";

    public static final String SQL_GET_ORDER_LIST_BY_MULTI_OPTION = SQL_GET_ORDER_PERFIX
            + " WHERE ({0} is_delete = 0)" + " ORDER BY {1} {2} LIMIT {3}, {4}";

    public static final String SQL_GET_REFUNDABLE_ORDER_LIST = SQL_GET_ORDER_PERFIX
            + " WHERE (user_id = ? AND order_state = 'ORDER_PAID' AND (order_total_fee > refunded_fee))  "
            + " ORDER BY id DESC";

    public static final String SQL_GET_UNPAID_ORDER_LIST = SQL_GET_ORDER_PERFIX
            + " WHERE (user_id = ? AND create_by = ? AND order_state in ('ORDER_WAIT_PAY', 'ORDER_WAIT_ALINOTIFY') AND is_delete = 0)"
            + " ORDER BY id DESC";

    public static final String SQL_GET_UNPAID_ORDER_NO = "SELECT order_no FROM `order`"
            + " WHERE user_id = ? AND create_by = ? AND order_state in ('ORDER_WAIT_PAY', 'ORDER_WAIT_ALINOTIFY') AND is_delete = 0";

    public static final String SQL_UPDATE_ORDER_STATE = "UPDATE `order` SET order_state = ?, update_time = CURRENT_TIMESTAMP"
            + " WHERE (order_no = ?)";

    public static final String SQL_UPDATE_ORDER_STATE_WAIT_PAY = "UPDATE `order` SET order_state = ?, update_time = CURRENT_TIMESTAMP"
            + " WHERE (order_no = ? AND order_state = 'ORDER_WAIT_PAY')";

    public static final String SQL_UPDATE_ORDERS_STATE = "UPDATE `order` SET order_state = 'ORDER_CANCELED', update_time = CURRENT_TIMESTAMP"
            + " WHERE (order_state = 'ORDER_WAIT_PAY' AND (create_time < ?))";

    public static final String SQL_GET_EXPIRED_UNPAID_ORDER = "SELECT order_no, user_id, create_by FROM `order`"
            + " WHERE (order_state = 'ORDER_WAIT_PAY' AND (create_time < ?))";

    public static final String SQL_UPDATE_DELETE_FLAG = "UPDATE `order` SET is_delete = ?, update_time = CURRENT_TIMESTAMP"
            + " WHERE (order_no = ?)";

    public static final String SQL_GET_ORDERITEM_LIST = "SELECT id, order_id, item_no, item_name, item_price, item_num "
            + " FROM order_item WHERE order_id = ?";

    @Override
    public long saveOrder(final Order order)
    {
        Object[] params = new Object[11];
        params[0] = order.getOrderNo();
        params[1] = order.getOrderState().toString();
        params[2] = order.getOrderTotalFee();
        params[3] = order.getRefundedFee();
        params[4] = order.getUserId();
        params[5] = order.getCreateBy();
        params[6] = order.getOrderRemark();
        params[7] = order.getIsDelete();
        params[8] = order.getExtra1();
        params[9] = order.getExtra2();
        params[10] = order.getExtra3();

        PostSaveHandler handler = new PostSaveHandler()
        {

            @Override
            public void handle(Long Id)
            {
                order.setId(Id);

                final Timestamp current = DateTimeUtils.getCurrentTimestamp();
                order.setCreateTime(current);
                order.setUpdateTime(current);

                if (redisManager.isEnable())
                {
                    final Jedis jedis = redisManager.getJedis();

                    try
                    {
                        // order
                        jedis.set(
                                RedisKeyUtils.buildOrderKey(order.getOrderNo()),
                                order.toString());

                        // unpaid order
                        // bug: if a device already exists several unpaid orders
                        // before, but not in cache, and now create a new unpaid
                        // order,
                        // then the cache will only have the new order, and lose
                        // the old ones.
                        jedis.sadd(
                                RedisKeyUtils.buildUserUnpaidOrderKey(
                                        order.getUserId(), order.getCreateBy()),
                                order.getOrderNo(), ELEMENT_NULL);
                    }
                    catch (Exception e)
                    {
                        logger.error("Failed put value into redis: " + order, e);
                    }
                    finally
                    {
                        jedis.close();
                    }
                }
            }
        };

        String errMsg = MessageFormat.format(
                "Failed insert order, orderNo={0}!", order.getOrderNo());

        daoHelper.save(SQL_INSERT_ORDER, handler, errMsg, true, params);

        final List<OrderItem> orderItems = order.getOrderItemList();

        // insert orderItems that belong to the order into DB.
        daoHelper.saveBatch(SQL_INSERT_ORDERITEM,
                "Failed to insert orderItem!",
                new BatchPreparedStatementSetter()
                {

                    @Override
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException
                    {
                        OrderItem orderItem = orderItems.get(i);
                        ps.setLong(1, order.getId());
                        ps.setString(2, orderItem.getProductNo());
                        ps.setString(3, orderItem.getProductName());
                        ps.setFloat(4, orderItem.getProductPrice());
                        ps.setInt(5, orderItem.getProductNum());
                    }

                    @Override
                    public int getBatchSize()
                    {
                        if (orderItems != null)
                        {
                            return orderItems.size();
                        }
                        return 0;
                    }
                });

        return order.getId();
    }

    @Override
    public List<Order> getOrderListByUserId(String userId)
    {
        String errMsg = MessageFormat.format(
                "Failed to query order's list which belong to userId={0}!",
                userId);
        List<Order> orders = daoHelper.queryForList(
                SQL_GET_ORDER_LIST_BY_USER_ID, orderMapper, errMsg, userId);

        if (CollectionUtils.isNotEmpty(orders))
        {
            // Add orderItems to each order in orders.
            for (Order order : orders)
            {
                long orderId = order.getId();
                List<OrderItem> orderItems = getOrderItemList(orderId);
                order.setOrderItemList(orderItems);
            }
        }

        return orders;
    }

    @Override
    public List<Order> getPaidOrderListByTimeScope(Timestamp startTime,
            Timestamp endTime)
    {
        String errMsg = MessageFormat
                .format("Failed to query paid order's list which paid time between {0} and {1}!",
                        startTime, endTime);
        List<Order> orders = daoHelper.queryForList(
                SQL_GET_PAID_ORDER_LIST_BY_TIME_SCOPE, orderWithPaytimeMapper,
                errMsg, startTime, endTime);

        if (CollectionUtils.isNotEmpty(orders))
        {
            // Add orderItems to each order in orders.
            for (Order order : orders)
            {
                long orderId = order.getId();
                List<OrderItem> orderItems = getOrderItemList(orderId);
                order.setOrderItemList(orderItems);
            }
        }

        return orders;
    }

    @Override
    public List<Order> getAllPaidOrderListOrderByPayTime()
    {
        String errMsg = "Failed to query all paid order's list!";
        List<Order> orders = daoHelper.queryForList(
                SQL_GET_ALL_PAID_ORDER_LIST_ORDER_BY_PAY_TIME,
                orderWithPaytimeMapper, errMsg);

        if (CollectionUtils.isNotEmpty(orders))
        {
            // Add orderItems to each order in orders.
            for (Order order : orders)
            {
                long orderId = order.getId();
                List<OrderItem> orderItems = getOrderItemList(orderId);
                order.setOrderItemList(orderItems);
            }
        }

        return orders;
    }

    @Override
    public List<Order> getRefundedOrderListByTimeScope(Timestamp startTime,
            Timestamp endTime)
    {
        String errMsg = MessageFormat
                .format("Failed to query refunded order's list which refunded time between {0} and {1}!",
                        startTime, endTime);
        List<Order> orders = daoHelper.queryForList(
                SQL_GET_REFUNDED_ORDER_LIST_BY_TIME_SCOPE,
                orderWithRefundtimeMapper, errMsg, startTime, endTime);

        /**
         * 此处用不到OrderItem，所以不构造
         */
        return orders;
    }

    @Override
    public List<Order> AllRefundedOrderListOrderByRefundTime()
    {
        String errMsg = "Failed to query all refunded order's list!";
        List<Order> orders = daoHelper.queryForList(
                SQL_GET_ALL_REFUNDED_ORDER_LIST_ORDER_BY_REFUND_TIME,
                orderWithRefundtimeMapper, errMsg);

        /**
         * 此处用不到OrderItem，所以不构造
         */
        return orders;
    }

    @Override
    public List<Order> getOrderListByState(PageInfo page, String orderState)
    {
        String sql = MessageFormat.format(SQL_GET_ORDER_LIST_BY_STATE, page
                .getSortBy(), page.getOrder().toString(), DataConvertUtils
                .toString(page.getPageNo() * page.getPageSize()),
                DataConvertUtils.toString(page.getPageSize()));
        String errMsg = MessageFormat.format(
                "Failed to query order list, state = {0}!", orderState);

        List<Order> orders = daoHelper.queryForList(sql, orderMapper, errMsg,
                orderState);

        return orders;
    }

    @Override
    public List<Order> getOrders(List<String> orderNos)
    {
        String sql = MessageFormat.format(SQL_GET_ORDER_LIST_BY_ORDER_NOS,
                StringUtils.getCSV(orderNos));

        String errMsg = MessageFormat.format(
                "Failed to query order list, orders = {0}!",
                StringUtils.getCSV(orderNos));

        List<Order> orders = daoHelper.queryForList(sql, orderMapper, errMsg);

        return orders;
    }

    @Override
    public List<Order> getOrderList(PageInfo page, String queryInfo)
    {
        String sql = MessageFormat
                .format(SQL_GET_ORDER_LIST_BY_MULTI_OPTION,
                        queryInfo,
                        page.getSortBy(),
                        page.getOrder().toString(),
                        DataConvertUtils.toString(page.getPageNo()
                                * page.getPageSize()),
                        DataConvertUtils.toString(page.getPageSize()));

        String errMsg = MessageFormat.format(
                "Failed to query order list, multioption = {0}!", queryInfo);

        List<Order> orders = daoHelper.queryForList(sql, orderMapper, errMsg);

        return orders;
    }

    @Override
    public List<Order> getRefundableOrders(String userId)
    {
        String errMsg = MessageFormat
                .format("Failed to query refundable order's list which belong to userId={0}!",
                        userId);
        List<Order> orders = daoHelper.queryForList(
                SQL_GET_REFUNDABLE_ORDER_LIST, orderMapper, errMsg, userId);

        if ((orders != null) && (orders.size() > 0))
        {
            // Add orderItems to order in orders.
            for (Order order : orders)
            {
                long orderId = order.getId();
                List<OrderItem> orderItems = getOrderItemList(orderId);
                order.setOrderItemList(orderItems);
            }
        }

        return orders;
    }

    /**
     * @param orderNo
     * @param queryItem
     * @return
     */
    private Order getOrderFromDB(String orderNo, boolean queryItem)
    {
        if (StringUtils.isBlank(orderNo))
        {
            return null;
        }

        Order order = null;
        String errMsg = MessageFormat.format(
                "Failed to query order by orderNo {0}!", orderNo);
        order = daoHelper.queryForObject(SQL_GET_ORDER, orderMapper, errMsg,
                orderNo);

        if (queryItem && (order != null))
        {
            long orderId = order.getId();
            List<OrderItem> orderItems = getOrderItemList(orderId);
            order.setOrderItemList(orderItems);
        }

        return order;
    }

    @Override
    public Order getOrder(String orderNo, boolean queryItem)
    {
        Collection<String> orderNos = new ArrayList<String>();
        orderNos.add(orderNo);

        final List<Order> orders = this.getOrders(orderNos);
        final Order order = orders.iterator().next();

        if (!queryItem && order != null)
        {
            order.setOrderItemList(null);
        }
        return order;
    }

    private List<Order> getOrders(Collection<String> orderNos)
    {
        List<Order> orders = new ArrayList<Order>();

        List<String> cachedOrderNos = new ArrayList<String>();
        if (redisManager.isEnable())
        {
            Jedis jedis = redisManager.getJedis();
            List<String> keys = new ArrayList<String>();

            for (String orderNo : orderNos)
            {
                keys.add(RedisKeyUtils.buildOrderKey(orderNo));
            }
            List<String> orderJsons = null;
            try
            {
                orderJsons = jedis.mget(keys.toArray(new String[0]));
            }
            catch (Exception e)
            {
                logger.error("Failed get value from redis: " + keys, e);
            }
            finally
            {
                jedis.close();
            }

            Order order;
            if (CollectionUtils.isNotEmpty(orderJsons))
            {
                for (String orderJson : orderJsons)
                {
                    if (!RedisManager.isNull(orderJson))
                    {
                        try
                        {
                            order = JsonUtils.fromJson(orderJson, Order.class);

                            if (order != null)
                            {
                                orders.add(order);
                                cachedOrderNos.add(order.getOrderNo());
                            }
                        }
                        catch (OVTException e)
                        {
                            String errMsg = MessageFormat.format(
                                    "Failed to convert json to Order: {0}",
                                    orderJson);
                            logger.error(errMsg, e);
                        }
                    }
                }
            }
        }

        // get no-cached orders from db
        orderNos.removeAll(cachedOrderNos);
        Collection<String> noCachedOrderNos = orderNos;

        if (CollectionUtils.isNotEmpty(noCachedOrderNos))
        {
            List<Order> noCachedOrders = new ArrayList<Order>();
            for (String orderNo : noCachedOrderNos)
            {
                final Order orderInfo = this.getOrderFromDB(orderNo, true);
                if (orderInfo != null)
                {
                    noCachedOrders.add(orderInfo);
                }
            }

            orders.addAll(noCachedOrders);

            // put into cache
            if (redisManager.isEnable()
                    && CollectionUtils.isNotEmpty(noCachedOrders))
            {
                List<String> keyValues = new ArrayList<String>();
                for (Order order : noCachedOrders)
                {
                    keyValues.add(RedisKeyUtils.buildOrderKey(order
                            .getOrderNo()));
                    keyValues.add(order.toString());
                }
                Jedis jedis = redisManager.getJedis();
                try
                {
                    jedis.mset(keyValues.toArray(new String[0]));
                }
                catch (Exception e)
                {
                    logger.error("Failed put value into redis: " + keyValues, e);
                }
                finally
                {
                    jedis.close();
                }
            }
        }

        return orders;
    }

    @Override
    public boolean updateOrderState(Order order, OrderState state)
    {
        this.removeFromOrderCache(order, state);

        Object[] params = new Object[2];
        params[0] = state.toString();
        params[1] = order.getOrderNo();

        String errMsg = MessageFormat.format(
                "Failed to update order [{0}] state!", order.getOrderNo());
        daoHelper.update(SQL_UPDATE_ORDER_STATE, null, errMsg, params);

        return true;
    }

    @Override
    public boolean updateOrderDeleteFlag(Order order, int flag)
    {
        this.removeFromOrderCache(order, OrderState.ORDER_DELETE);

        Object[] params = new Object[2];
        params[0] = flag;
        params[1] = order.getOrderNo();

        String errMsg = MessageFormat
                .format("Failed to update order [{0}] delete flag!",
                        order.getOrderNo());
        daoHelper.update(SQL_UPDATE_DELETE_FLAG, null, errMsg, params);

        return true;
    }

    /**
     * @param order
     */
    private void removeFromOrderCache(Order order, OrderState state)
    {
        if (redisManager.isEnable())
        {
            final Jedis jedis = redisManager.getJedis();
            try
            {
                // order cache
                jedis.del(RedisKeyUtils.buildOrderKey(order.getOrderNo()));

                // unpaid cache
                if (state != OrderState.ORDER_WAIT_PAY
                        && state != OrderState.ORDER_WAIT_ALINOTIFY)
                {
                    jedis.srem(
                            RedisKeyUtils.buildUserUnpaidOrderKey(
                                    order.getUserId(), order.getCreateBy()),
                            order.getOrderNo());
                }
            }
            catch (Exception e)
            {
                logger.error(
                        "Failed delete key from redis: " + order.getOrderNo(),
                        e);
            }
            finally
            {
                jedis.close();
            }
        }
    }

    private List<OrderItem> getOrderItemList(long orderId)
    {
        String errMsg = MessageFormat.format(
                "Failed to query OrderItem list which belong to orderId={0}!",
                orderId);
        List<OrderItem> orders = daoHelper.queryForList(SQL_GET_ORDERITEM_LIST,
                orderItemMapper, errMsg, orderId);

        return orders;
    }

    @Override
    public List<Order> getUnPaidOrderList(String userId, String createBy,
            boolean queryItem)
    {
        List<Order> orders = null;

        String key = null;
        if (redisManager.isEnable())
        {
            Jedis jedis = redisManager.getJedis();
            key = RedisKeyUtils.buildUserUnpaidOrderKey(userId, createBy);

            Set<String> orderNos = null;
            try
            {
                orderNos = jedis.smembers(key);
            }
            catch (Exception e)
            {
                logger.error("Failed get value from redis: " + key, e);
            }
            finally
            {
                jedis.close();
            }

            if (orderNos != null)
            {
                orderNos.remove(ELEMENT_NULL);
                if (CollectionUtils.isNotEmpty(orderNos))
                {
                    orders = this.getOrders(orderNos);

                    // sort by id desc
                    orders = this.desc(orders);

                    return orders;
                }
            }
        }

        String errMsg = MessageFormat.format(
                "Failed to query unpaid order's list belong to userId={0}!",
                userId);
        orders = daoHelper.queryForList(SQL_GET_UNPAID_ORDER_LIST, orderMapper,
                errMsg, userId, createBy);

        List<String> unpaidOrderNos = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(orders))
        {
            // Add orderItems to order in orders.
            for (Order order : orders)
            {
                unpaidOrderNos.add(order.getOrderNo());

                if (queryItem)
                {
                    long orderId = order.getId();
                    List<OrderItem> orderItems = getOrderItemList(orderId);
                    order.setOrderItemList(orderItems);
                }
            }
        }

        // put into cache
        if (redisManager.isEnable())
        {
            unpaidOrderNos.add(ELEMENT_NULL);
            Jedis jedis = redisManager.getJedis();
            try
            {
                jedis.sadd(key, unpaidOrderNos.toArray(new String[0]));
            }
            catch (Exception e)
            {
                logger.error("Failed put value to redis: " + key, e);
            }
            finally
            {
                jedis.close();
            }
        }

        return orders;
    }

    /**
     * @param orders
     * @return
     */
    private List<Order> desc(List<Order> orders)
    {
        orders = CollectionUtils.sort(orders, new Comparator<Order>()
        {

            @Override
            public int compare(Order o1, Order o2)
            {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return orders;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.order.dao.OrderDao#getUnPaidOrderNum(java.lang.String,
     * java.lang.String)
     */
    @Override
    public int getUnPaidOrderNum(String userId, String createBy)
    {
        int unpaidNum = 0;
        String key = null;
        if (redisManager.isEnable())
        {
            Jedis jedis = redisManager.getJedis();
            key = RedisKeyUtils.buildUserUnpaidOrderKey(userId, createBy);
            try
            {
                unpaidNum = jedis.scard(key).intValue() - 1;
            }
            catch (Exception e)
            {
                logger.error("Failed get value from redis: " + key, e);
            }
            finally
            {
                jedis.close();
            }

            if (unpaidNum >= 0) // <0 means key not exists
            {
                return unpaidNum;
            }
        }

        String errMsg = MessageFormat.format(
                "Failed to query unpaid order number belong to userId={0}!",
                userId);
        List<String> unpaidOrderNos = daoHelper
                .queryForList(SQL_GET_UNPAID_ORDER_NO, String.class, errMsg,
                        userId, createBy);

        unpaidNum = (unpaidOrderNos == null) ? 0 : unpaidOrderNos.size();
        if (redisManager.isEnable())
        {
            unpaidOrderNos.add(ELEMENT_NULL);
            Jedis jedis = redisManager.getJedis();
            try
            {
                jedis.sadd(key, unpaidOrderNos.toArray(new String[0]));
            }
            catch (Exception e)
            {
                logger.error("Failed put value to redis: " + key, e);
            }
            finally
            {
                jedis.close();
            }
        }

        return unpaidNum;
    }

    @Override
    public void cleanExpiredUnpaidOrders(Timestamp comparedTime)
    {
        String errMsg;
        if (redisManager.isEnable())
        {
            errMsg = MessageFormat.format(
                    "Failed to query expired unpaid order list!", comparedTime);
            List<Map<String, Object>> orderList = daoHelper.queryForList(
                    SQL_GET_EXPIRED_UNPAID_ORDER, errMsg, comparedTime);
            if (CollectionUtils.isNotEmpty(orderList))
            {
                String orderNo, userId, createBy;
                List<String> orderNos = new ArrayList<String>();
                final Jedis jedis = redisManager.getJedis();
                try
                {
                    for (Map<String, Object> orderInfo : orderList)
                    {
                        orderNo = DataConvertUtils.toString(orderInfo
                                .get(ORDER.ORDER_NO));
                        userId = DataConvertUtils.toString(orderInfo
                                .get(ORDER.USER_ID));
                        createBy = DataConvertUtils.toString(orderInfo
                                .get(ORDER.CREATE_BY));

                        orderNos.add(RedisKeyUtils.buildOrderKey(orderNo));

                        // remove from unpaid cache
                        // TODO: change to batch remove
                        jedis.srem(RedisKeyUtils.buildUserUnpaidOrderKey(
                                userId, createBy), orderNo);
                    }
                    // remove from order cache
                    jedis.del(orderNos.toArray(new String[0]));
                }
                catch (Exception e)
                {
                    logger.error("Failed delete keys from redis: " + orderNos,
                            e);
                }
                finally
                {
                    jedis.close();
                }
            }
        }

        errMsg = "Failed to clean expired unpaid order's list!";
        daoHelper.update(SQL_UPDATE_ORDERS_STATE, null, errMsg, comparedTime);
    }

    @Override
    public void updateOrderStateOfAppNotify(Order order, OrderState state)
    {
        this.removeFromOrderCache(order, state);

        Object[] params = new Object[2];
        params[0] = state.toString();
        params[1] = order.getOrderNo();

        String errMsg = MessageFormat.format(
                "Failed to update order [{0}] state!", order.getOrderNo());
        daoHelper.update(SQL_UPDATE_ORDER_STATE_WAIT_PAY, null, errMsg, params);
    }

}
