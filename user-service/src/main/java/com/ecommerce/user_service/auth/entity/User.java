package com.ecommerce.user_service.auth.entity;

import com.ecommerce.user_service.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Required by JPA, protected is best practice
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean isEnabled;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updateAt;

    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @PrePersist
    void onSave() {
        this.createdAt = new Date();
        this.updateAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.updateAt = new Date();
    }
}
