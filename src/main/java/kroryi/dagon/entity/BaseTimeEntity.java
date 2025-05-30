package kroryi.dagon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {
    // createdAt 컬럼에서 @ColumnDefault("CURRENT_TIMESTAMP")가 JPA에서 동작하지 않을 수도 있어서
    // 생성 시점에 직접 값 할당
    // 시간관련만

    @Column(name = "created_at")
    protected LocalDateTime createdAt;


    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // LocalDateTime을 LocalDate로 변환
    public LocalDate getCreatedAt() {
        return this.createdAt != null ? this.createdAt.toLocalDate() : null;
    }
}
