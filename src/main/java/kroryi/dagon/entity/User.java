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
    private String uprofile_img;
    private int upoints;
    private int ulevel;
    private LocalDateTime ucreated_at;
    private String uphone;
    @Enumerated(EnumType.STRING)
    private Role urole;

    public enum Role {
        admin, partner, normal_user
    }

}
