
USE ovdoorbellpaydb;

INSERT IGNORE INTO admin(id,admin_name, admin_pwd, create_time, update_time, update_by) 
VALUES(1,'admin', '0192023a7bbd73250516f069df18b500', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT IGNORE INTO permission(id, perm_code, perm_name, perm_desc, create_time, is_sa) 
VALUES(1,'SA','超级管理员', 'Permission for Super Admin who have all permissions', CURRENT_TIMESTAMP, 1);

INSERT IGNORE INTO permission(id, perm_code, perm_name, perm_desc, create_time, is_sa) 
VALUES(2,'REFUND','退款管理', 'Permission for refunder who can process refund', CURRENT_TIMESTAMP, 0);

INSERT IGNORE INTO permission(id, perm_code, perm_name, perm_desc, create_time, is_sa) 
VALUES(3,'RESOURCE', '商品管理','Permission for edit product', CURRENT_TIMESTAMP, 0);

INSERT IGNORE INTO permission(id, perm_code, perm_name, perm_desc, create_time, is_sa) 
VALUES(4,'PROFILE', '设备状态','Permission for which to query member profile', CURRENT_TIMESTAMP, 0);

INSERT IGNORE INTO permission(id, perm_code, perm_name, perm_desc, create_time, is_sa) 
VALUES(5,'TRANSFER', '转账管理', 'Permission for transfer', CURRENT_TIMESTAMP, 0);

INSERT IGNORE INTO permission_url(id, url_name, perm_code, url_type, create_time) 
VALUES(1,'user', 'SA', 'INCLUDE', CURRENT_TIMESTAMP);

INSERT IGNORE INTO permission_url(id, url_name, perm_code, url_type, create_time) 
VALUES(2,'permission', 'SA', 'INCLUDE', CURRENT_TIMESTAMP);

INSERT IGNORE INTO permission_url(id, url_name, perm_code, url_type, create_time) 
VALUES(3,'resources', 'RESOURCE', 'INCLUDE', CURRENT_TIMESTAMP);

INSERT IGNORE INTO permission_url(id, url_name, perm_code, url_type, create_time) 
VALUES(4,'profile', 'PROFILE', 'INCLUDE', CURRENT_TIMESTAMP);

INSERT IGNORE INTO permission_url(id, url_name, perm_code, url_type, create_time) 
VALUES(5,'actions', 'PROFILE', 'INCLUDE', CURRENT_TIMESTAMP);

INSERT IGNORE INTO permission_url(id, url_name, perm_code, url_type, create_time) 
VALUES(6,'refund', 'REFUND', 'INCLUDE', CURRENT_TIMESTAMP);

INSERT IGNORE INTO permission_url(id, url_name, perm_code, url_type, create_time) 
VALUES(7,'transfer', 'TRANSFER', 'INCLUDE', CURRENT_TIMESTAMP);

INSERT IGNORE INTO permission_url(id, url_name, perm_code, url_type, create_time) 
VALUES(8,'/user/login', '', 'EXCLUDE', CURRENT_TIMESTAMP);

INSERT IGNORE INTO permission_url(id, url_name, perm_code, url_type, create_time) 
VALUES(9,'/user/logout', '', 'EXCLUDE', CURRENT_TIMESTAMP);

INSERT IGNORE INTO permission_url(id, url_name, perm_code, url_type, create_time) 
VALUES(10,'/user/changePassword', '', 'EXCLUDE', CURRENT_TIMESTAMP);

INSERT IGNORE INTO permission_url(id, url_name, perm_code, url_type, create_time) 
VALUES(11,'/user/current', '', 'EXCLUDE', CURRENT_TIMESTAMP);

INSERT IGNORE INTO admin_permission(id,ap_admin_id, ap_perm_id, create_time, update_time, update_by) 
VALUES(1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT IGNORE INTO resource(id,res_code,res_name,res_type,res_size,res_price,res_unit) 
VALUES(1,'Space-1','1GB Space','SPACE',1048576,5,'MONTH');

INSERT IGNORE INTO resource(id,res_code,res_name,res_type,res_size,res_price,res_unit) 
VALUES(2,'Space-2','2GB Space','SPACE',2097152,8,'MONTH');

INSERT IGNORE INTO resource(id,res_code,res_name,res_type,res_size,res_price,res_unit) 
VALUES(3,'Space-5','5GB Space','SPACE',5242880,18,'MONTH');

INSERT IGNORE INTO resource(id,res_code,res_name,res_type,res_size,res_price,res_unit) 
VALUES(4,'Space-10','10GB Space','SPACE',10485760,28,'MONTH');

INSERT IGNORE INTO resource(id,res_code,res_name,res_type,res_size,res_price,res_unit) 
VALUES(5,'Space-20','20GB Space','SPACE',20971520,48,'MONTH');

INSERT IGNORE INTO resource(id,res_code,res_name,res_type,res_size,res_price,res_unit) 
VALUES(6,'Space-50','50GB Space','SPACE',52428800,88,'MONTH');

INSERT IGNORE INTO resource(id,res_code,res_name,res_type,res_size,res_price,res_unit) 
VALUES(7,'Space-100','100GB Space','SPACE',104857600,148,'MONTH');

INSERT IGNORE INTO resource(id,res_code,res_name,res_type,res_size,res_price,res_unit) 
VALUES(8,'Space-Max','Max Space','SPACE',9999999999,288,'MONTH');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(1, 'debugMode', 'false', CURRENT_TIMESTAMP, '调试模式');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(2, 'debugPay', 'true', CURRENT_TIMESTAMP, '支付调试模式');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(3, 'cookie.access_token.age', '1800', CURRENT_TIMESTAMP, '登录cookie有效时间(单位: 秒)');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(4, 'resource.manager.url', 'http://192.168.1.180:9090/payment?wsdl', CURRENT_TIMESTAMP, 'doorbell设备服务器');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(5, 'payment.service.url', 'http://localhost:28080/PaymentService/api', CURRENT_TIMESTAMP, '支付系统服务器');

INSERT IGNORE INTO app_properties(id,prop_name,prop_value,create_time,`desc`) 
VALUES(6, 'payment.refund.deduct.fee', '0,50,0', CURRENT_TIMESTAMP, '退款手续费(费率, 最大值, 最小值)');