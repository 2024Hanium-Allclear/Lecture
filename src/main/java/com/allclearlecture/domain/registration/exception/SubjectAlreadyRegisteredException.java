package com.allclearlecture.domain.registration.exception;

import com.allclearlecture.domain.registration.enums.RegistrationErrorCode;
import com.allclearlecture.global.apiPayload.exception.GeneralException;

public class SubjectAlreadyRegisteredException extends GeneralException {
    public SubjectAlreadyRegisteredException(RegistrationErrorCode errorCode) {
        super(errorCode);
    }
}
