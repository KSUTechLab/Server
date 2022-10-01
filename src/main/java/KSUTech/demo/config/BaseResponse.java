package KSUTech.demo.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
    private boolean success;
    private int code;
    private String message;
}
