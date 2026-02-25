package com.wms.inventory.repository;

import com.wms.inventory.entity.StockTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {

    Page<StockTransfer> findByStatusOrderByCreatedAtDesc(
            StockTransfer.TransferStatus status, Pageable pageable);

    Page<StockTransfer> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
