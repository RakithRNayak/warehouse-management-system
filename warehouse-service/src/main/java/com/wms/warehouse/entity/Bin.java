package com.wms.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rack_id", nullable = false)
    private Rack rack;

    @Column(unique = true, nullable = false, length = 30)
    private String code;

    @Column(nullable = false)
    private Integer level;

    @Enumerated(EnumType.STRING)
    private BinType type;

    @Column(name = "max_weight_kg", precision = 10, scale = 2)
    private BigDecimal maxWeightKg;

    @Column(name = "max_volume_cbm", precision = 10, scale = 4)
    private BigDecimal maxVolumeCbm;

    @Column(name = "current_weight_kg", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal currentWeightKg = BigDecimal.ZERO;

    @Column(name = "current_volume_cbm", precision = 10, scale = 4)
    @Builder.Default
    private BigDecimal currentVolumeCbm = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BinStatus status = BinStatus.AVAILABLE;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum BinType { STANDARD, BULK, FLOOR, COLD }
    public enum BinStatus { AVAILABLE, OCCUPIED, FULL, BLOCKED, MAINTENANCE }
}
