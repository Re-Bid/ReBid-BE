package server.rebid.common.exception;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import server.rebid.common.CommonResponse;
import server.rebid.common.exception.dto.ErrorResponseDTO;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { GeneralException.class})
    protected ResponseEntity<Object> handleCustomException(GeneralException e, HttpServletRequest request) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return handleExceptionInternal(e, e.getErrorReasonHttpStatus(), HttpHeaders.EMPTY, request);
    }


    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        String errorMessage =
                e.getConstraintViolations().stream()
                        .map(constraintViolation -> constraintViolation.getMessage())
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "ConstraintViolationException 추출 도중 에러 발생"));

        return handleExceptionInternalConstraint(
                e, GlobalErrorCode.valueOf(errorMessage), HttpHeaders.EMPTY, request);
    }

    @Override
    @NotNull
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().stream()
                .forEach(
                        fieldError -> {
                            String fieldName = fieldError.getField();
                            String errorMessage =
                                    Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                            errors.merge(
                                    fieldName,
                                    errorMessage,
                                    (existingErrorMessage, newErrorMessage) ->
                                            existingErrorMessage + ", " + newErrorMessage);
                        });

        return handleExceptionInternalArgs(
                ex, HttpHeaders.EMPTY, GlobalErrorCode.valueOf("BAD_ARGS_ERROR"), request, errors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        e.printStackTrace();

        return handleExceptionInternalFalse(
                e,
                GlobalErrorCode.SERVER_ERROR,
                HttpHeaders.EMPTY,
                GlobalErrorCode.SERVER_ERROR.getHttpStatus(),
                request,
                e.getMessage());
    }


    private ResponseEntity<Object> handleExceptionInternal(
            Exception e, ErrorResponseDTO reason, HttpHeaders headers, HttpServletRequest request) {

        CommonResponse<Object> body =
                CommonResponse.onFailure(reason.getCode(), reason.getMessage(), null);
        e.printStackTrace();

        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                e, body, headers, reason.getHttpStatus(), webRequest);
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(
            Exception e,
            GlobalErrorCode errorCode,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request,
            String errorPoint) {
        CommonResponse<Object> body =
                CommonResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errorPoint);
        return super.handleExceptionInternal(e, body, headers, status, request);
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(
            Exception e,
            HttpHeaders headers,
            GlobalErrorCode errorCode,
            WebRequest request,
            Map<String, String> errorArgs) {
        CommonResponse<Object> body =
                CommonResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errorArgs);
        return super.handleExceptionInternal(e, body, headers, errorCode.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(
            Exception e, GlobalErrorCode errorCode, HttpHeaders headers, WebRequest request) {
        CommonResponse<Object> body =
                CommonResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null);
        return super.handleExceptionInternal(e, body, headers, errorCode.getHttpStatus(), request);
    }

}

