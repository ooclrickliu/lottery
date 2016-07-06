
USE ovpaydb;

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(1, 'alipay.partner', '2088121594253464', CURRENT_TIMESTAMP, '支付宝商家');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(2, 'alipay.private_key', 'MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOySNMBOohCrVAzfWGvAh6pOlD44SBYMIIvhiMTdQ1t6lDWv+YMx2BJkst+hcUEOGVgQmPDvn2xGCKt48UDJN0s6nY2GOEhO3+LZ5iLGHa73/AZlmNJvjJ4yzZm5v63HNKeXxLCIM2A2vCQtxJTssoHeSY/eXnQkJ5bG2mTvfWkhAgMBAAECgYA1PIGadReLYMK9UtV3ChB9hsldZxTxwTDhyZMZmiE6SM9pdXxUxba2DOsj4BS4aw/Q/YstTP83QltZGxJPXfiPCoP6EC6GWsI9f/Ig5pyU5/pfFPHUP3mdHvf2xYdOOMRs3Nay4qS8GNbkHFEqoaCb4Z9hIO57E+/o9gouGdaZcQJBAP5FaFdZo5yr2qXTkiSaOKXYCZzWnh7p78HBpRD+8VKHd0I7kZeVeU7DDm9ZbFr+VBcUcaUdD8yEDCN/LBw/Do8CQQDuLf1Bmz/Jl4Z236fgz5G8u6VPcW7TEWq78jw4Bt4y8mKmtej3kRQmM+DZebAxacosG0+ECq2w8op3JG/Q3uVPAkEA7p+0jEVThnTBfV5QiIkzIHDelJHVUq24k91nS8v2Lb1z7nR47gkZDxF/OoBxb/O+dup4vLvJV+WUfruaaefDgQJAHZnnnMy/q71CP+niAmWptiwYCnyNinviqQMdCtWHrgtDyXZYQtRsDadq7XxbMnj0YHDwe/ebyJ8MKGyF3Q6mrwJABv8Ao5U/PNRvQPEMFx7hnzBsN2nwADJ6xLCp2FkecuTTxySsOpSIYjOjzGIDvcIGch0KmWXKZ9j9jYcIFcjaPg==', CURRENT_TIMESTAMP, '支付宝商家私钥');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(3, 'alipay.input_charset', 'utf-8', CURRENT_TIMESTAMP, '支付宝字符编码');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(4, 'alipay.sign_type', 'RSA', CURRENT_TIMESTAMP, '支付宝签名类型');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(5, 'alipay.seller_email', 'whshitutong@163.com', CURRENT_TIMESTAMP, '支付宝卖家账号');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(6, 'alipay.refund_notify_url', 'http://116.211.106.179:20080/payment/refund/alinotify', CURRENT_TIMESTAMP, '支付宝退款通知地址');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(7, 'alipay.transfer_notify_url', 'http://116.211.106.179:20080/payment/transfer/alinotify', CURRENT_TIMESTAMP, '支付宝转账通知地址');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(8, 'doorbell.server_url', 'http\://localhost\:28080/DoorbellService/api', CURRENT_TIMESTAMP, 'Doorbell服务器地址');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(9, 'payment.pay_deadline', '5', CURRENT_TIMESTAMP, '支付时限');
