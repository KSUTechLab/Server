package KSUTech.demo.auth;

import KSUTech.demo.auth.domain.User;
import KSUTech.demo.auth.domain.UserType;
import KSUTech.demo.auth.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AuthService {

    private UserRepository userRepository;

    @Value("${naver-id}")
    String naver_id;

    @Value("${naver-secret}")
    String naver_secret;
    @Autowired
    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void createUser(User user){
        userRepository.save(user);
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
            String id = element.getAsJsonObject().get("id").getAsString();
            String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            User user = User.builder()
                    .user_id(id)
                    .user_name(nickname)
                    .userType("KAKAO")
                    .refresh_token("1234")
                    .build();
            userRepository.save(user);
            br.close();
        } catch (IOException e){
            System.out.println("e = " + e);
        }
    }

    public void createNaverUser(String code,String state){


        // code로 access,refresh 토큰 받아오기
        String reqURL = "https://nid.naver.com/oauth2.0/"+
                "token?grant_type=authorization_code"+
                "&client_id="+naver_id+
                "&client_secret="+naver_secret+
                "&code="+code+
                "&state=" + state;
        // 유저 정보 가져오는 url
        String reqURL2 = "https://openapi.naver.com/v1/nid/me";
        try {



            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

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
            String access_token = element.getAsJsonObject().get("access_token").getAsString();
            String refresh_token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token = " + access_token);
            System.out.println("refresh_token = " + refresh_token);

            URL url2 = new URL(reqURL2);
            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();

            conn2.setRequestMethod("GET");
            conn2.setDoOutput(true);
            conn2.addRequestProperty("Content-type", "application/x-www-form-urlencoded"); // 프로퍼티 설정
            conn2.setRequestProperty("Authorization", "bearer " + access_token);
            br = new BufferedReader(new InputStreamReader(conn2.getInputStream(),"UTF-8"));
            line = "";
            result = "";
            while ((line = br.readLine()) != null){
                result += line;
            }
            System.out.println("responsebody  :  " + result);

//            String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            br.close();
        } catch (IOException e){
            System.out.println("e = " + e);
        }
    }

    // 연동해제
    public void deleteNaverUser(){
        String reqURL = "https://nid.naver.com/oauth2.0/token?grant_type="+
            "delete&client_id="+naver_id+
                "&client_secret="+naver_secret+
                "&access_token="+"AAAAN2X-9MvjhahmXTc2hQiTPMk5fi8oa1Auks1wHCfK3i3mkgJc8OIKESr_nsTOWjAXCTyxNA9ovJKHHbOrDpfStKI"+
                "&service_provider=NAVER";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

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
            br.close();
        } catch (IOException e){
            System.out.println("e = " + e);
        }
    }
}
