package KSUTech.demo.auth;

import KSUTech.demo.auth.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping("/register")
    public String createUser() {
        return "요청했습니다.";
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        try {
            authService.createUser(user);
            return ResponseEntity.ok("성공하셨습니다.");
        } catch(Exception e){
            return ResponseEntity.ok("실패");
        }
    }

    @GetMapping("/kakao")
    public void kakao(HttpServletResponse response) throws IOException {
        String redirect_uri="https://kauth.kakao.com/oauth/authorize?client_id=621a5bbf4799d30e3f92f52493d3f37f&redirect_uri=http://localhost:8080/api/auth/kakaocallback&response_type=code";
        response.sendRedirect(redirect_uri);
    }

    @GetMapping("/kakaocallback")
    public String kakaocallback(@RequestParam String code){
        String access_token = authService.getKakaoAccessToken(code);
        System.out.println("access_token = " + access_token);
        authService.createKakaoUser(access_token);
        return "성공입니다.";
    }

    @GetMapping("/naver")
    public void naver(HttpServletResponse response) throws IOException {
        String redirect_uri="https://nid.naver.com/oauth2.0/authorize?" +
                "response_type=code" +                  // 인증과정에 대한 내부 구분값 code 로 전공 (고정값)
                "&client_id=EbDraUVpe9nfxcxTJrkq" +     // 발급받은 client_id 를 입력
                "&state=NAVER_LOGIN_TEST" +             // CORS 를 방지하기 위한 특정 토큰값(임의값 사용)
                "&redirect_uri=http://localhost:8080/api/auth/navercallback";   // 어플케이션에서 등록했던 CallBack URL를 입력
        response.sendRedirect(redirect_uri);
    }

    // 연동 해제
    @GetMapping("/delete/naver")
    public void deletenaver() throws IOException {
        authService.deleteNaverUser();
    }

    // 네이버 콜백 처리
    @GetMapping("/navercallback")
    public String navercallback(@RequestParam String code,@RequestParam String state){
        // code 를 받아오면 code 를 사용하여 access_token를 발급받는다.
//        final NaverLoginVo naverLoginVo = service.requestNaverLoginAcceccToken(resValue, "authorization_code");
        authService.createNaverUser(code,state);
        // access_token를 사용하여 사용자의 고유 id값을 가져온다.
//        final NaverLoginProfile naverLoginProfile = service.requestNaverLoginProfile(naverLoginVo);
        return code;
    }

    @GetMapping("/naver/access-token")
    public String naver_access_token(@RequestParam String access_token){
        return "";
    }
}