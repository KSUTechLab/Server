package KSUTech.demo.auth.domain;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_test")
public class User {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 소셜로그인 유저 고유 아이디
    @NotEmpty(message = "아이디가 공백입니다.")
    private String user_id;

    // 유저 이름 or 닉네임
    @NotEmpty(message = "유저이름이 공백입니다.")
    private String user_name;

    // 소셜 로그인 타입
    @NotEmpty(message = "로그인 타입이 공백입니다.")
    private String social;

    // refresh_token 값
    private String refresh_token;

    @Builder
    public User(String user_id,String user_name,String userType,String refresh_token){
        this.user_id = user_id;
        this.user_name = user_name;
        this.social = userType;
        this.refresh_token = refresh_token;
    }
}
