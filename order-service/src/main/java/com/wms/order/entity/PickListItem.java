package com.wms.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pick_list_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pick_list_id", nullable = false)
    private PickList pickList;

    @Column(name = "outbound_order_id", nullable = false)
    private Long outboundOrderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_sku", nullable = false, length = 50)
    private String productSku;

    @Column(name = "from_bin_id", nullable = false)
    private Long fromBinId;

    @Column(name = "from_bin_code", nullable = false, length = 30)
    private String fromBinCode;

    @Column(name = "quantity_to_pick", nullable = false)
    private Integer quantityToPick;

    @Column(name = "quantity_picked")
    @Builder.Default
    private Integer quantityPicked = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PickItemStatus status = PickItemStatus.PENDING;

    public enum PickItemStatus { PENDING, PICKED, SHORT_PICKED, SKIPPED }
}
