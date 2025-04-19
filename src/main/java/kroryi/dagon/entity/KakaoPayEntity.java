package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.DTO.KakaoPayDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name= "kakao_pay")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tid;
    private String nextRedirectPcUrl;
    private LocalDateTime createdAt;

    public static KakaoPayEntity fromDTO(KakaoPayDTO dto) {
        return KakaoPayEntity.builder()
                .tid(dto.getTid())
                .nextRedirectPcUrl(dto.getNext_redirect_pc_url())
                .createdAt(dto.getCreated_at().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
    }

    public KakaoPayDTO toDTO() {
        KakaoPayDTO dto = new KakaoPayDTO();
        dto.setTid(this.tid);
        dto.setNext_redirect_pc_url(this.nextRedirectPcUrl);
        dto.setCreated_at(java.sql.Timestamp.valueOf(this.createdAt));
        return dto;
    }

    public void updateFromDTO(KakaoPayDTO dto) {
        this.tid = dto.getTid();
        this.nextRedirectPcUrl = dto.getNext_redirect_pc_url();
        this.createdAt = dto.getCreated_at().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
