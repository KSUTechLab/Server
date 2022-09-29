package KSUTech.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
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
}