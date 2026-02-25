package com.wms.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zones", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"warehouse_id", "code"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private String code;

    @Enumerated(EnumType.STRING)
    private ZoneType type;

    @Column(name = "min_temperature", precision = 5, scale = 2)
    private BigDecimal minTemperature;

    @Column(name = "max_temperature", precision = 5, scale = 2)
    private BigDecimal maxTemperature;

    @Column(name = "area_sqft", precision = 10, scale = 2)
    private BigDecimal areaSqft;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Aisle> aisles = new ArrayList<>();

    public enum ZoneType {
        RECEIVING, STORAGE, PICKING, PACKING, SHIPPING, QUARANTINE, RETURNS
    }
}
