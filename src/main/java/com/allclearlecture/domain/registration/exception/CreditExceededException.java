package com.allclearlecture.domain.registration.exception;

import com.allclearlecture.domain.registration.enums.RegistrationErrorCode;
import com.allclearlecture.global.apiPayload.exception.GeneralException;

public class CreditExceededException extends GeneralException {
    public CreditExceededException(RegistrationErrorCode errorCode) {
        super(errorCode);
    }
}
