package kroryi.dagon.entity;

import jakarta.persistence.*;
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
    @Column(name = "uaid", nullable = false)
    private Long uaid;



    @Lob
    @Column(name = "board_type", nullable = false)
    private String boardType;

    @Lob
    @Column(name = "action_type", nullable = false)
    private String actionType;

    @ColumnDefault("0")
    @Column(name = "views")
    private Integer views;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private kroryi.dagon.entity.User uid;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false, referencedColumnName = "uid")
    private FishingDiary boardId;

}