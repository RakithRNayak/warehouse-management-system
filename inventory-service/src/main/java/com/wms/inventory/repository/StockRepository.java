package com.wms.inventory.repository;

import com.wms.inventory.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByProductId(Long productId);

    List<Stock> findByWarehouseId(Long warehouseId);

    Page<Stock> findByWarehouseId(Long warehouseId, Pageable pageable);

    Optional<Stock> findByProductIdAndWarehouseIdAndBinIdAndLotNumber(
            Long productId, Long warehouseId, Long binId, String lotNumber);

    @Query("SELECT s FROM Stock s WHERE s.product.id = :productId AND s.warehouseId = :warehouseId")
    List<Stock> findByProductIdAndWarehouseId(
            @Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId);

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Stock s WHERE s.product.id = :productId")
    Integer getTotalQuantityByProductId(@Param("productId") Long productId);

    @Query("SELECT s FROM Stock s JOIN s.product p WHERE s.quantity <= p.reorderPoint AND p.isActive = true")
    List<Stock> findLowStockItems();
}
