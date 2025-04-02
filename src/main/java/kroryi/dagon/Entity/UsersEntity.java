package kroryi.dagon.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_no;

    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String pw;

    private String name;
    private String nickname;
    private String email;

    @Lob
    private String profile_image;

    private int points;
    private int level;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    private String phone;
    private String role;
}
