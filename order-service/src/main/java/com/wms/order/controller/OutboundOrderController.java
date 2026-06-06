package com.wms.order.controller;

import com.wms.order.entity.OutboundOrder;
import com.wms.order.service.OutboundOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/outbound")
public class OutboundOrderController {

    private final OutboundOrderService outboundOrderService;

    public OutboundOrderController(OutboundOrderService outboundOrderService) {
        this.outboundOrderService = outboundOrderService;
    }

    @GetMapping
    public ResponseEntity<Page<OutboundOrder>> getAllOrders(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(outboundOrderService.getAllOrders(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutboundOrder> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(outboundOrderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<OutboundOrder> createOrder(@RequestBody OutboundOrder order) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(outboundOrderService.createOrder(order));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OutboundOrder> updateStatus(
            @PathVariable Long id,
            @RequestParam OutboundOrder.OutboundOrderStatus status) {
        return ResponseEntity.ok(outboundOrderService.updateStatus(id, status));
    }
}
