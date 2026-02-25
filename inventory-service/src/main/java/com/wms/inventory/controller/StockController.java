package com.wms.inventory.controller;

import com.wms.inventory.entity.Stock;
import com.wms.inventory.entity.StockAdjustment;
import com.wms.inventory.entity.StockMovement;
import com.wms.inventory.entity.StockTransfer;
import com.wms.inventory.service.StockService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Stock>> getStockByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getStockByProductId(productId));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<Page<Stock>> getStockByWarehouse(
            @PathVariable Long warehouseId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockService.getStockByWarehouseId(warehouseId, pageable));
    }

    @PostMapping("/receive")
    public ResponseEntity<Stock> receiveStock(@RequestBody Map<String, Object> request) {
        Long productId = Long.valueOf(request.get("productId").toString());
        Long warehouseId = Long.valueOf(request.get("warehouseId").toString());
        Long binId = request.get("binId") != null ? Long.valueOf(request.get("binId").toString()) : null;
        Integer quantity = Integer.valueOf(request.get("quantity").toString());
        String lotNumber = request.get("lotNumber") != null ? request.get("lotNumber").toString() : null;
        Long performedBy = Long.valueOf(request.get("performedBy").toString());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockService.receiveStock(productId, warehouseId, binId, quantity, lotNumber, performedBy));
    }

    @PostMapping("/adjust")
    public ResponseEntity<StockAdjustment> adjustStock(@RequestBody StockAdjustment adjustment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockService.createAdjustment(adjustment));
    }

    @PostMapping("/transfer")
    public ResponseEntity<StockTransfer> transferStock(@RequestBody StockTransfer transfer) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockService.createTransfer(transfer));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Stock>> getLowStockItems() {
        return ResponseEntity.ok(stockService.getLowStockItems());
    }

    @GetMapping("/movements")
    public ResponseEntity<Page<StockMovement>> getMovementHistory(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(stockService.getMovementHistory(pageable));
    }
}
