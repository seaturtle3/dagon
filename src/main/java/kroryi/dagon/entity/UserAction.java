package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.ActionType;
import kroryi.dagon.enums.BoardType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "user_actions")
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ua_id", nullable = false)
    private Long uaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", nullable = false, length = 20)
    private BoardType boardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, length = 20)
    private ActionType actionType;;

    @ColumnDefault("1")
    @Column(name = "value", nullable = false)
    private Integer value = 1;

    // 게시글, 상품 ID
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    // User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private User user;



}