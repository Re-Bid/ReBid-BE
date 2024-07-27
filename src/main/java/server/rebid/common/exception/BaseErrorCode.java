package server.rebid.common.exception;


import server.rebid.common.exception.dto.ErrorResponseDTO;

public interface BaseErrorCode {

    public ErrorResponseDTO getReason();

    public ErrorResponseDTO getReasonHttpStatus();
}
