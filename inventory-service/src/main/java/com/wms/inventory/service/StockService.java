package com.wms.inventory.service;

import com.wms.inventory.entity.*;
import com.wms.inventory.exception.ResourceNotFoundException;
import com.wms.inventory.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository movementRepository;
    private final StockAdjustmentRepository adjustmentRepository;
    private final StockTransferRepository transferRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public StockService(StockRepository stockRepository,
                        ProductRepository productRepository,
                        StockMovementRepository movementRepository,
                        StockAdjustmentRepository adjustmentRepository,
                        StockTransferRepository transferRepository,
                        KafkaTemplate<String, Object> kafkaTemplate) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.adjustmentRepository = adjustmentRepository;
        this.transferRepository = transferRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(readOnly = true)
    public List<Stock> getStockByProductId(Long productId) {
        return stockRepository.findByProductId(productId);
    }

    @Transactional(readOnly = true)
    public Page<Stock> getStockByWarehouseId(Long warehouseId, Pageable pageable) {
        return stockRepository.findByWarehouseId(warehouseId, pageable);
    }

    @Transactional
    public Stock receiveStock(Long productId, Long warehouseId, Long binId,
                              Integer quantity, String lotNumber, Long performedBy) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Stock stock = stockRepository
                .findByProductIdAndWarehouseIdAndBinIdAndLotNumber(productId, warehouseId, binId, lotNumber)
                .orElse(Stock.builder()
                        .product(product)
                        .warehouseId(warehouseId)
                        .binId(binId)
                        .lotNumber(lotNumber)
                        .build());

        stock.setQuantity(stock.getQuantity() + quantity);
        stock = stockRepository.save(stock);

        // Record movement
        StockMovement movement = StockMovement.builder()
                .product(product)
                .warehouseId(warehouseId)
                .movementType(StockMovement.MovementType.RECEIVED)
                .quantity(quantity)
                .referenceType("RECEIVING")
                .toBinId(binId)
                .lotNumber(lotNumber)
                .performedBy(performedBy)
                .build();
        movementRepository.save(movement);

        // Publish event
        kafkaTemplate.send("wms.inventory.stock-received", Map.of(
                "productId", productId,
                "warehouseId", warehouseId,
                "quantity", quantity
        ));

        // Check low stock
        checkLowStock(product);

        return stock;
    }

    @Transactional
    public StockAdjustment createAdjustment(StockAdjustment adjustment) {
        return adjustmentRepository.save(adjustment);
    }

    @Transactional
    public StockTransfer createTransfer(StockTransfer transfer) {
        return transferRepository.save(transfer);
    }

    @Transactional(readOnly = true)
    public Page<StockMovement> getMovementHistory(Pageable pageable) {
        return movementRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public List<Stock> getLowStockItems() {
        return stockRepository.findLowStockItems();
    }

    private void checkLowStock(Product product) {
        Integer totalQuantity = stockRepository.getTotalQuantityByProductId(product.getId());
        if (totalQuantity <= product.getReorderPoint()) {
            kafkaTemplate.send("wms.inventory.low-stock", Map.of(
                    "productId", product.getId(),
                    "sku", product.getSku(),
                    "productName", product.getName(),
                    "currentQuantity", totalQuantity,
                    "reorderPoint", product.getReorderPoint()
            ));
        }
    }
}
