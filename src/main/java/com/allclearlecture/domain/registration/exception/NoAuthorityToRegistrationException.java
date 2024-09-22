package com.allclearlecture.domain.registration.exception;

import com.allclearlecture.domain.registration.enums.RegistrationErrorCode;
import com.allclearlecture.global.apiPayload.exception.GeneralException;

public class NoAuthorityToRegistrationException extends GeneralException {
    public NoAuthorityToRegistrationException(RegistrationErrorCode errorCode) {
        super(errorCode);
    }
}
