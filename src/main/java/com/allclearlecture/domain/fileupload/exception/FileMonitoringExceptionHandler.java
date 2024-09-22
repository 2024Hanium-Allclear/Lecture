package com.allclearlecture.domain.fileupload.exception;

import com.allclearlecture.domain.fileupload.enums.FileUploadErrorCode;
import com.allclearlecture.global.apiPayload.exception.GeneralException;

public class FileMonitoringExceptionHandler extends GeneralException {

    public FileMonitoringExceptionHandler(FileUploadErrorCode errorCode) {
        super(errorCode);
    }
}
