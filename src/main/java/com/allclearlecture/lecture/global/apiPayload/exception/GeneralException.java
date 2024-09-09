package com.allclearlecture.lecture.global.apiPayload.exception;

import com.allclearlecture.lecture.global.apiPayload.code.status.BaseErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public GeneralException(BaseErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
