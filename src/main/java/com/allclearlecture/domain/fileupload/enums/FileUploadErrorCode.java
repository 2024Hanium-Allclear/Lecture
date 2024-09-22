package com.allclearlecture.domain.fileupload.enums;

import com.allclearlecture.global.apiPayload.ApiResponse;
import com.allclearlecture.global.apiPayload.code.status.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileUploadErrorCode implements BaseErrorCode {

    FILE_EMPTY(HttpStatus.BAD_REQUEST, "FILEUPLOAD400", "파일이 비어있습니다."),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILEUPLOAD500", "파일 업로드 중 오류 발생."),
    FILE_MONITORING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILEUPLOAD501", "파일 모니터링 중 오류 발생."),
    KAFKA_CONSUMER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILEUPLOAD502", "Kafka 소비자 시작 중 오류 발생.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }
}
