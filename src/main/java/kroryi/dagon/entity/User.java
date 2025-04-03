package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uno;

    private String uid;
    private String upw;
    private String uname;
    private String unickname;
    private String uemail;
    private String uprofileImg;
    private int upoints;

    @Enumerated
    @Column(name = "ulevel", nullable = false)
    private ulevel ulevel;
    public enum ulevel {
        Diamond, Gold, Platinum, Silver
    }

    private LocalDateTime ucreatedAt;
    private String uphone;

    @Enumerated(EnumType.STRING)
    private Role urole;
    public enum Role {
        Admin, Partner, Normal_user
    }


}
