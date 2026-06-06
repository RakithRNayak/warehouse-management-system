package com.wms.order.controller;

import com.wms.order.entity.InboundOrder;
import com.wms.order.service.InboundOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/inbound")
public class InboundOrderController {

    private final InboundOrderService inboundOrderService;

    public InboundOrderController(InboundOrderService inboundOrderService) {
        this.inboundOrderService = inboundOrderService;
    }

    @GetMapping
    public ResponseEntity<Page<InboundOrder>> getAllOrders(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(inboundOrderService.getAllOrders(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InboundOrder> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(inboundOrderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<InboundOrder> createOrder(@RequestBody InboundOrder order) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inboundOrderService.createOrder(order));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<InboundOrder> updateStatus(
            @PathVariable Long id,
            @RequestParam InboundOrder.InboundOrderStatus status) {
        return ResponseEntity.ok(inboundOrderService.updateStatus(id, status));
    }
}
