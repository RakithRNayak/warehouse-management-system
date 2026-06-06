package com.wms.order.repository;

import com.wms.order.entity.PackingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackingRecordRepository extends JpaRepository<PackingRecord, Long> {

    List<PackingRecord> findByOutboundOrderId(Long outboundOrderId);
}
