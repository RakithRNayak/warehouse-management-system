package com.wms.order.service;

import com.wms.order.entity.InboundOrder;
import com.wms.order.exception.ResourceNotFoundException;
import com.wms.order.repository.InboundOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class InboundOrderService {

    private final InboundOrderRepository inboundOrderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InboundOrderService(InboundOrderRepository inboundOrderRepository,
                                KafkaTemplate<String, Object> kafkaTemplate) {
        this.inboundOrderRepository = inboundOrderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(readOnly = true)
    public Page<InboundOrder> getAllOrders(Pageable pageable) {
        return inboundOrderRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public InboundOrder getOrderById(Long id) {
        return inboundOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inbound order not found with id: " + id));
    }

    @Transactional
    public InboundOrder createOrder(InboundOrder order) {
        order.setOrderNumber("INB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setInboundOrder(order));
        }
        InboundOrder saved = inboundOrderRepository.save(order);

        kafkaTemplate.send("wms.order.inbound-created", Map.of(
                "orderId", saved.getId(),
                "orderNumber", saved.getOrderNumber(),
                "warehouseId", saved.getWarehouseId()
        ));

        return saved;
    }

    @Transactional
    public InboundOrder updateStatus(Long id, InboundOrder.InboundOrderStatus status) {
        InboundOrder order = getOrderById(id);
        order.setStatus(status);
        InboundOrder updated = inboundOrderRepository.save(order);

        kafkaTemplate.send("wms.order.inbound-status-changed", Map.of(
                "orderId", updated.getId(),
                "orderNumber", updated.getOrderNumber(),
                "status", status.name()
        ));

        return updated;
    }
}
