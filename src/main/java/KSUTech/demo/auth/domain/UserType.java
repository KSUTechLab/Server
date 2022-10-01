package KSUTech.demo.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum UserType {
    KAKAO("ROLE_KAKAO"),
    GOOGLE("ROLE_GOOGLE"),
    NAVER("ROLE_NAVER");
    final private String social;

}
