package com.wms.notification.listener;

import com.wms.notification.entity.Notification;
import com.wms.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaNotificationListener.class);

    private final NotificationService notificationService;

    public KafkaNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "wms.inventory.low-stock", groupId = "notification-service-group")
    public void handleLowStockEvent(Map<String, Object> event) {
        log.info("Received low stock event: {}", event);

        Notification notification = Notification.builder()
                .userId(1L) // Will be resolved to warehouse manager in Phase 2
                .title("Low Stock Alert")
                .message("Product " + event.get("productName") + " (SKU: " + event.get("sku") +
                        ") is below reorder point. Current quantity: " + event.get("currentQuantity"))
                .type(Notification.NotificationType.LOW_STOCK)
                .channel(Notification.NotificationChannel.IN_APP)
                .referenceType("PRODUCT")
                .status(Notification.NotificationStatus.SENT)
                .build();

        notificationService.createNotification(notification);
    }

    @KafkaListener(topics = "wms.order.outbound-status-changed", groupId = "notification-service-group")
    public void handleOrderStatusChange(Map<String, Object> event) {
        log.info("Received order status change event: {}", event);

        Notification notification = Notification.builder()
                .userId(1L) // Will be resolved from order context in Phase 2
                .title("Order Status Updated")
                .message("Order " + event.get("orderNumber") + " status changed to: " + event.get("status"))
                .type(Notification.NotificationType.ORDER_STATUS)
                .channel(Notification.NotificationChannel.IN_APP)
                .referenceType("ORDER")
                .status(Notification.NotificationStatus.SENT)
                .build();

        notificationService.createNotification(notification);
    }

    @KafkaListener(topics = "wms.shipping.status-updated", groupId = "notification-service-group")
    public void handleShipmentStatusChange(Map<String, Object> event) {
        log.info("Received shipment status change event: {}", event);

        Notification notification = Notification.builder()
                .userId(1L) // Will be resolved from shipment context in Phase 2
                .title("Shipment Status Updated")
                .message("Shipment " + event.get("shipmentNumber") + " status changed to: " + event.get("status"))
                .type(Notification.NotificationType.SHIPMENT_STATUS)
                .channel(Notification.NotificationChannel.IN_APP)
                .referenceType("SHIPMENT")
                .status(Notification.NotificationStatus.SENT)
                .build();

        notificationService.createNotification(notification);
    }
}
