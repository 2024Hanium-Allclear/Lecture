package com.allclearlecture.domain.registration.exception;

import com.allclearlecture.domain.registration.enums.RegistrationErrorCode;
import com.allclearlecture.global.apiPayload.exception.GeneralException;

public class CourseTimeConflictException extends GeneralException {
    public CourseTimeConflictException(RegistrationErrorCode errorCode) {
        super(errorCode);
    }
}
