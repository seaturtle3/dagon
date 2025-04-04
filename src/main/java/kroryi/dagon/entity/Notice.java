package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nid", nullable = false, length = 50)
    private String nid;

    @Column(name = "admin_id", nullable = false, length = 50)
    private String adminId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "modify_at")
    private Instant modifyAt;

    @Column(name = "ncontent")
    private String ncontent;

    @Column(name = "nnickname", nullable = false, length = 50)
    private String nnickname;

    @Column(name = "ntitle", nullable = false)
    private String ntitle;

    @Column(name = "views")
    private Integer views;

}