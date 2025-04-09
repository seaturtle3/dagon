package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "freeboard_comments")
public class FreeBoardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fb_comment_id", nullable = false)
    private Long fbCommentId;

    @Column(name = "comment_content")
    private String commentContent;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;


    // 자유게시판
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fb_id", nullable = false)
    private FreeBoard freeBoard;

    // 회원
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private User user;

}