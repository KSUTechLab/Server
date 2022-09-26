package KSUTech.demo.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    @GetMapping("/kakao")
    public String kakaocallback(@RequestParam String code){
        System.out.println("code = " + code);
        return "성공입니다.";
    }
    @GetMapping("/kakao2")
    public String kakao(){
        return "성공입니다.";
    }
}