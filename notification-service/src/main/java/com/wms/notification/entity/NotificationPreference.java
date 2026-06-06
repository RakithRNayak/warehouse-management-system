package com.wms.notification.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_preferences", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "notification_type", "channel"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private Notification.NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Notification.NotificationChannel channel;

    @Column(name = "is_enabled")
    @Builder.Default
    private Boolean isEnabled = true;
}
