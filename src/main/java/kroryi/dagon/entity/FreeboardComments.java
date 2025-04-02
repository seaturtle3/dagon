package kroryi.dagon.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
public class FreeboardComments {
    @Id
    @Column(name = "comment_id", nullable = false)
    private Long comment_id;

    @Column(name = "fbid", nullable = false)
    private Long fbid;

    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "unickname", nullable = false, length = 50)
    private String unickname;

    @Column(name = "ulevel", nullable = false)
    private Integer ulevel;

    @Column(name = "coment_contentt")
    private String coment_contentt;

    @Column(name = "modify_at")
    private LocalDateTime modify_at;

}
