-- Create databases for each microservice
CREATE DATABASE IF NOT EXISTS wms_auth;
CREATE DATABASE IF NOT EXISTS wms_inventory;
CREATE DATABASE IF NOT EXISTS wms_warehouse;
CREATE DATABASE IF NOT EXISTS wms_order;
CREATE DATABASE IF NOT EXISTS wms_shipping;
CREATE DATABASE IF NOT EXISTS wms_notification;

-- Grant privileges
GRANT ALL PRIVILEGES ON wms_auth.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON wms_inventory.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON wms_warehouse.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON wms_order.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON wms_shipping.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON wms_notification.* TO 'root'@'%';
FLUSH PRIVILEGES;
