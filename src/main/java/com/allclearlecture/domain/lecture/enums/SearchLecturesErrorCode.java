package com.allclearlecture.domain.lecture.enums;

import com.allclearlecture.global.apiPayload.ApiResponse;
import com.allclearlecture.global.apiPayload.code.status.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SearchLecturesErrorCode implements BaseErrorCode {

    NO_LECTURES_FOUND(HttpStatus.NOT_FOUND, "LECTURE_NOT_FOUND", "No lectures found for the given criteria"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "Input parameters are invalid");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }

}
