package KSUTech.demo.auth;

import KSUTech.demo.auth.domain.User;
import KSUTech.demo.auth.domain.UserType;
import KSUTech.demo.auth.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AuthService {

    private UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public String getKakaoAccessToken(String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";
        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값을 변경
            conn.setRequestMethod("POST");
            // Json 데이터를 출력 가능한 상태로 변경해줘야함
            conn.setDoOutput(true);

            //POST 요청에 필요하는 파라미터 스트림으로 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=621a5bbf4799d30e3f92f52493d3f37f");
            sb.append("&redirect_uri=http://localhost:8080/api/auth/kakaocallback");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while((line = br.readLine()) !=null){
                result +=line;
            }
            System.out.println("response body = " + result);
            JsonElement element = JsonParser.parseString(result);
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        }catch(IOException e){
            System.out.println("e = " + e);
        }
        return access_Token;
    }

    public void createKakaoUser(String token){
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "bearer " + token);

            // 결과 코드 200 이면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null){
                result += line;
            }
            System.out.println("responsebody  :  " + result);

            JsonElement element = JsonParser.parseString(result);
            Long id = element.getAsJsonObject().get("id").getAsLong();
            String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            User user = User.builder()
                            .user_id(id)
                            .user_name(nickname)
                            .userType(UserType.KAKAO)
                            .refresh_token("1234")
                            .build();
            userRepository.save(user);
            br.close();
        } catch (IOException e){
            System.out.println("e = " + e);
        }
    }
}
