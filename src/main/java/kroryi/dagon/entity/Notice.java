package kroryi.dagon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notice {
    @Id
    @Column(name = "nid", nullable = false, length = 50)
    private String nid;

    @Column(name = "admin_id", nullable = false, length = 50)
    private String admin_id;

    @Column(name = "nnickname", nullable = false, length = 50)
    private String nnickname;

    @Column(name = "ntitle", nullable = false)
    private String ntitle;

    @Column(name = "ncontent")
    private String ncontent;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "modify_at")
    private LocalDateTime modify_at;

    @Column(name = "views")
    private Integer views;

}
