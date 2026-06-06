package com.wms.order.repository;

import com.wms.order.entity.OutboundOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OutboundOrderRepository extends JpaRepository<OutboundOrder, Long> {

    Optional<OutboundOrder> findByOrderNumber(String orderNumber);

    Page<OutboundOrder> findByWarehouseIdOrderByCreatedAtDesc(Long warehouseId, Pageable pageable);

    Page<OutboundOrder> findByStatusOrderByCreatedAtDesc(OutboundOrder.OutboundOrderStatus status, Pageable pageable);

    Page<OutboundOrder> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<OutboundOrder> findByStatusOrderByPriorityAscCreatedAtAsc(
            OutboundOrder.OutboundOrderStatus status, Pageable pageable);
}
