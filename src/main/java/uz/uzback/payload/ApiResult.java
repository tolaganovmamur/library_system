package uz.uzback.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
public class ApiResult {
    private Boolean success;
    private String message;
    private Integer code;
    private Object data;

    private ApiResult(boolean success, String message, Integer code, Object data) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    private ApiResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    private ApiResult(boolean success, String message, Integer code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    private ApiResult(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    private ApiResult(boolean success) {
        this.success = success;
    }

    public static ApiResult successResponse() {
        return new ApiResult(true);
    }

    public static ApiResult failResponse() {
        return new ApiResult(false);
    }

    public static ApiResult successResponse(String massage) {
        return new ApiResult(true, massage);
    }

    public static ApiResult successLogin(String token) {
        return ApiResult.builder().success(true).data(token).build();
    }

    public static ApiResult failResponse(String massage) {
        return new ApiResult(false, massage);
    }

    public static ApiResult successResponse(String massage, Integer code) {
        return new ApiResult(true, massage, code);
    }

    public static ApiResult failResponse(String massage, Integer code) {
        return new ApiResult(false, massage, code);
    }

    public static ApiResult successResponse(Object data) {
        return new ApiResult(true, data);
    }

    public static ApiResult failResponse(Object data) {
        return new ApiResult(false, data);
    }


}
