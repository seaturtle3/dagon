package kroryi.dagon.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
public class FreeboardComments {
    @Id
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fbid")
    private FishingReport fishingReport;

    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "unickname", length = 50)
    private String unickname;

    @Column(name = "ulevel")
    private Integer ulevel;

    @Column(name = "coment_contentt")
    private String comentContentt;

    @Column(name = "modify_at")
    private LocalDateTime modifiedAt;

}
