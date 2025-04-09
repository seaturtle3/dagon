package kroryi.dagon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseTimeEntity {
    // createdAt 컬럼에서 @ColumnDefault("CURRENT_TIMESTAMP")가 JPA에서 동작하지 않을 수도 있어서
    // 생성 시점에 직접 값 할당
    // 시간관련만

    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
