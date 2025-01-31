package server.rebid.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder( {"isSuccess", "code", "message", "data"} )
public class CommonResponse<T> {
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Java 8 날짜/시간 모듈 등록
            mapper.registerModule(new JavaTimeModule());
            // 날짜와 시간을 ISO-8601 형식의 문자열로 직렬화
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            // 이쁘게 출력하기
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonProperty("isSuccess")
    private Boolean isSuccess;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

    // 성공한 경우 응답 생성

    public static <T> CommonResponse<T> onSuccess(T data){
        return new CommonResponse<>(true, "200" , "요청에 성공하였습니다.",  data);
    }

    // 실패한 경우 응답 생성
    public static <T> CommonResponse<T> onFailure(String code, String message, T data){
        return new CommonResponse<>(false,code, message, data);
    }

}
