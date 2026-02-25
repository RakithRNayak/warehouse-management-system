package com.wms.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_warehouse_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserWarehouseAssignment.UserWarehouseId.class)
public class UserWarehouseAssignment {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserWarehouseId implements Serializable {
        private Long userId;
        private Long warehouseId;
    }
}
