package KSUTech.demo.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    KAKAO("ROLE_KAKAO", "카카오"),
    GOOGLE("ROLE_GOOGLE", "구글");
    private final String key;
    private final String value;

}
