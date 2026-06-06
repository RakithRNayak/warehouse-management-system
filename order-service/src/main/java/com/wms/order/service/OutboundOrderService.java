package com.wms.order.service;

import com.wms.order.entity.OutboundOrder;
import com.wms.order.exception.ResourceNotFoundException;
import com.wms.order.repository.OutboundOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class OutboundOrderService {

    private final OutboundOrderRepository outboundOrderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OutboundOrderService(OutboundOrderRepository outboundOrderRepository,
                                 KafkaTemplate<String, Object> kafkaTemplate) {
        this.outboundOrderRepository = outboundOrderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(readOnly = true)
    public Page<OutboundOrder> getAllOrders(Pageable pageable) {
        return outboundOrderRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public OutboundOrder getOrderById(Long id) {
        return outboundOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Outbound order not found with id: " + id));
    }

    @Transactional
    public OutboundOrder createOrder(OutboundOrder order) {
        order.setOrderNumber("OUT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setOutboundOrder(order));
        }
        OutboundOrder saved = outboundOrderRepository.save(order);

        kafkaTemplate.send("wms.order.outbound-created", Map.of(
                "orderId", saved.getId(),
                "orderNumber", saved.getOrderNumber(),
                "warehouseId", saved.getWarehouseId(),
                "customerName", saved.getCustomerName()
        ));

        return saved;
    }

    @Transactional
    public OutboundOrder updateStatus(Long id, OutboundOrder.OutboundOrderStatus status) {
        OutboundOrder order = getOrderById(id);
        order.setStatus(status);
        OutboundOrder updated = outboundOrderRepository.save(order);

        kafkaTemplate.send("wms.order.outbound-status-changed", Map.of(
                "orderId", updated.getId(),
                "orderNumber", updated.getOrderNumber(),
                "status", status.name()
        ));

        return updated;
    }
}
