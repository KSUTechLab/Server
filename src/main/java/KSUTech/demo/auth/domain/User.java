package KSUTech.demo.auth.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_test")
public class User {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카카오 유저 아이디
    private Long user_id;

    // 유저 이름
    private String user_name;

    // 소셜 로그인 타입
    @Enumerated(EnumType.STRING)
    private UserType social;

    // refresh_token 값
    private String refresh_token;

    @Builder
    public User(Long user_id,String user_name,UserType userType,String refresh_token){
        this.user_id = user_id;
        this.user_name = user_name;
        this.social = userType;
        this.refresh_token = refresh_token;
    }
}
