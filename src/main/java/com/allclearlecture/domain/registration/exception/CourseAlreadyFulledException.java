package com.allclearlecture.domain.registration.exception;

import com.allclearlecture.domain.registration.enums.RegistrationErrorCode;
import com.allclearlecture.global.apiPayload.exception.GeneralException;

public class CourseAlreadyFulledException extends GeneralException {
    public CourseAlreadyFulledException(RegistrationErrorCode errorCode) {
        super(errorCode);
    }
}
