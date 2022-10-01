package KSUTech.demo.auth;


import KSUTech.demo.auth.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthCotrollerTest {
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    public void 성공() throws Exception{
        User user = User.builder()
                .user_name("mjung1798@gmail.com")
                .userType("KAKAO")
                .user_id("slbin")
                .refresh_token("asdf")
                .build();
        String userDtoJsonString = objectMapper.writeValueAsString(user);

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJsonString))
                .andExpect(status().isOk());
    }

    @Test
    public void 실패() throws Exception{
        User user = User.builder()
                .user_name("mjung1798@gmail.com")
                .user_id("slbin")
                .refresh_token("asdf")
                .build();
        String userDtoJsonString = objectMapper.writeValueAsString(user);

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJsonString))
                .andExpect(status().isBadRequest());
    }
}
