package server.rebid.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import server.rebid.common.exception.dto.ErrorResponseDTO;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
    // AUTH + 401 Unauthorized - 권한 없음
    TOKEN_EXPIRED(UNAUTHORIZED, "AUTH401_1", "인증 토큰이 만료 되었습니다. 토큰을 재발급 해주세요"),
    INVALID_TOKEN(UNAUTHORIZED, "AUTH401_2", "인증 토큰이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "AUTH401_3", "리프레시 토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(UNAUTHORIZED, "AUTH401_4", "리프레시 토큰이 만료 되었습니다."),
    AUTHENTICATION_REQUIRED(UNAUTHORIZED, "AUTH401_5", "인증 정보가 유효하지 않습니다."),
    LOGIN_REQUIRED(UNAUTHORIZED, "AUTH401_6", "로그인이 필요한 서비스입니다."),
    REST_TEMPLATE_FAIL1(INTERNAL_SERVER_ERROR, "AUTH401_7", "Rest Template Error"),
    REST_TEMPLATE_FAIL2(INTERNAL_SERVER_ERROR, "AUTH401_7", "Rest Template Error"),
    // AUTH + 403 Forbidden - 인증 거부
    AUTHENTICATION_DENIED(FORBIDDEN, "AUTH403_1", "인증이 거부 되었습니다."),

    // AUTH + 404 Not Found - 찾을 수 없음
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "AUTH404_1", "리프레시 토큰이 존재하지 않습니다."),

    // GLOBAL + 500 Server Error
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "GLOBAL500_1", "서버 에러, 서버 개발자에게 알려주세요."),

    // GLOBAL + Args Validation Error
    BAD_ARGS_ERROR(BAD_REQUEST, "GLOBAL400_1", "request body의 validation이 실패했습니다. 응답 body를 참고해주세요"),

    // USER + 400 BAD_REQUEST - 잘못된 요청
    NOT_VALID_PHONE_NUMBER(BAD_REQUEST, "USER400_1", "유효하지 않은 전화번호 입니다."),

    // USER + 401 Unauthorized - 권한 없음

    // USER + 403 Forbidden - 인증 거부

    // USER + 404 Not Found - 찾을 수 없음
    USER_NOT_FOUND(NOT_FOUND, "USER404_1", "등록된 사용자 정보가 없습니다."),

    // USER + 409 CONFLICT : Resource 를 찾을 수 없음
    DUPLICATE_PHONE_NUMBER(CONFLICT, "USER409_1", "중복된 전화번호가 존재합니다."),

    // MEMBER + 401 Unauthorized - 권한 없음
    MEMBER_NOT_AUTHORIZED(UNAUTHORIZED, "MEMBER401_1", "해당 멤버는 권한이 없습니다."),
    // MEMBER + 404 Not Found - 찾을 수 없음
    MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER404_1", "존재하지 않는 멤버입니다."),
    AVAILABLE_PROFILE_NOT_FOUND(NOT_FOUND, "MEMBER404_2", "현재 선택된 멤버 정보가 없습니다."),


    // NOTIFICATION + 404 Not Found - 찾을 수 없음
    NOTIFICATION_NOT_FOUND(NOT_FOUND, "NOTIFICATION404_1", "존재하지 않는 알림입니다."),

    //FEIGN + 400 BAD_REQUEST - 잘못된 요청
    FEIGN_CLIENT_ERROR_400(BAD_REQUEST, "FEIGN400", "feignClient 에서 400번대 에러가 발생했습니다."),

    //FEIGN + 500 INTERNAL_SERVER_ERROR - 서버 에러
    FEIGN_CLIENT_ERROR_500(INTERNAL_SERVER_ERROR, "FEIGN500", "feignClient 에서 500번대 에러가 발생했습니다."),

    // NCP Phone Auth
    PHONE_NUMBER_EXIST(OK, "NCP200_1", "이미 인증된 전화번호입니다."),
    PHONE_AUTH_NOT_FOUND(BAD_REQUEST, "NCP400_1", "인증 번호 요청이 필요합니다."),
    PHONE_AUTH_WRONG(BAD_REQUEST, "NCP400_2", "잘못된 인증 번호 입니다."),
    PHONE_AUTH_TIMEOUT(BAD_REQUEST, "NCP400_3", "인증 시간이 초과되었습니다."),

    // Bid + 404 Not Found - 찾을 수 없음
    BID_NOT_FOUND(NOT_FOUND, "BID404_1", "존재하지 않는 경매입니다."),
    BID_KEYWORD_NOT_FOUND(NOT_FOUND, "BID404_2", "존재하지 않는 검색 키워드 입니다."),

    // Bid + 409 CONFLICT
    BID_ALREADY_CONFIRM(CONFLICT, "BID409_1", "이미 승인 완료된 경매입니다."),
    BID_ALREADY_REJECT(CONFLICT, "BID409_2", "이미 승인 거부된 경매입니다."),
    BID_NOT_REAL_TIME(CONFLICT, "BID409_3", "해당 경매는 실시간 경매가 아닙니다."),
    BID_NOT_RESERVATION(CONFLICT, "BID409_4", "해당 경매는 기간 경매가 아닙니다."),

    //Category + 404 Not Found - 찾을 수 없음
    CATEGORY_NOT_FOUND(NOT_FOUND, "CATEGORY404_1", "존재하지 않는 카테고리입니다."),

    //BidHistory + 400 BAD_REQUEST - 잘못된 요청
    INVALID_BID_HISTORY(BAD_REQUEST, "BIDHISTORY400_1", "입찰 금액이 현재 입찰 금액보다 낮습니다."),
    WRONG_BID_ITEM(BAD_REQUEST, "BIDHISTORY400_2", "자신의 제품에는 입찰할 수 없습니다."),

    // Material + 404 Not Found - 찾을 수 없음
    MATERIAL_NOT_FOUND(NOT_FOUND, "MATERIAL404_1", "존재하지 않는 재료입니다."),

    // Comment + 404 Not Found - 찾을 수 없음
    COMMENT_NOT_FOUND(NOT_FOUND, "COMMENT404_1", "존재하지 않는 댓글입니다.")
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorResponseDTO getReason() {
        return ErrorResponseDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorResponseDTO getReasonHttpStatus() {
        return ErrorResponseDTO.builder()
                .message(message)
                .code(code)
                .httpStatus(httpStatus)
                .isSuccess(false)
                .build();
    }
}
