package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fishing_diary_comment")
public class FishingDiaryComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fd_comment_id", nullable = false)
    private Long fdCommentId;

    @Lob
    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    // 조행기
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fd_id", nullable = false)
    private FishingDiary fishingDiary;

    // 회원
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private User user;

}