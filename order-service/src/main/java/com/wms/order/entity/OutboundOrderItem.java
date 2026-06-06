package com.wms.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "outbound_order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboundOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outbound_order_id", nullable = false)
    private OutboundOrder outboundOrder;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "ordered_quantity", nullable = false)
    private Integer orderedQuantity;

    @Column(name = "allocated_quantity")
    @Builder.Default
    private Integer allocatedQuantity = 0;

    @Column(name = "picked_quantity")
    @Builder.Default
    private Integer pickedQuantity = 0;

    @Column(name = "packed_quantity")
    @Builder.Default
    private Integer packedQuantity = 0;

    @Column(name = "bin_id")
    private Long binId;
}
