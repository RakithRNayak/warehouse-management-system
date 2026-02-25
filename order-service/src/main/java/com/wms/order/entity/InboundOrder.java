package com.wms.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inbound_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false, length = 30)
    private String orderNumber;

    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    @Column(name = "supplier_reference", length = 50)
    private String supplierReference;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private InboundOrderStatus status = InboundOrderStatus.CREATED;

    @Column(name = "expected_date")
    private LocalDate expectedDate;

    @Column(name = "received_date")
    private LocalDate receivedDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "inboundOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InboundOrderItem> items = new ArrayList<>();

    public enum InboundOrderStatus {
        CREATED, CONFIRMED, IN_TRANSIT, ARRIVED, RECEIVING, PARTIALLY_RECEIVED, RECEIVED, CANCELLED
    }
}
