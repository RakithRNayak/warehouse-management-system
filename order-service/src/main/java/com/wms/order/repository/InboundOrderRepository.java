package com.wms.order.repository;

import com.wms.order.entity.InboundOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InboundOrderRepository extends JpaRepository<InboundOrder, Long> {

    Optional<InboundOrder> findByOrderNumber(String orderNumber);

    Page<InboundOrder> findByWarehouseIdOrderByCreatedAtDesc(Long warehouseId, Pageable pageable);

    Page<InboundOrder> findByStatusOrderByCreatedAtDesc(InboundOrder.InboundOrderStatus status, Pageable pageable);

    Page<InboundOrder> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
