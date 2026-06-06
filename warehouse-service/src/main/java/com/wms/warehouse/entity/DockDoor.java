package com.wms.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dock_doors", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"warehouse_id", "code"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DockDoor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String code;

    @Enumerated(EnumType.STRING)
    private DockDoorType type;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DockDoorStatus status = DockDoorStatus.AVAILABLE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum DockDoorType { INBOUND, OUTBOUND, BOTH }
    public enum DockDoorStatus { AVAILABLE, IN_USE, MAINTENANCE }
}
