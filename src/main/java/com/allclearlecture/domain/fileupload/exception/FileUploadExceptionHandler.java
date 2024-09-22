package com.allclearlecture.domain.fileupload.exception;

import com.allclearlecture.domain.fileupload.enums.FileUploadErrorCode;
import com.allclearlecture.global.apiPayload.exception.GeneralException;

public class FileUploadExceptionHandler extends GeneralException {


    public FileUploadExceptionHandler(FileUploadErrorCode errorCode) {
        super(errorCode);
    }
}