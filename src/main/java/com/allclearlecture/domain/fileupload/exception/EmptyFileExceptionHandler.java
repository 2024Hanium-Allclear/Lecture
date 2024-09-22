package com.allclearlecture.domain.fileupload.exception;

import com.allclearlecture.domain.fileupload.enums.FileUploadErrorCode;
import com.allclearlecture.global.apiPayload.exception.GeneralException;

public class EmptyFileExceptionHandler extends GeneralException {
    public EmptyFileExceptionHandler(FileUploadErrorCode errorCode) {
        super(errorCode);
    }
}
