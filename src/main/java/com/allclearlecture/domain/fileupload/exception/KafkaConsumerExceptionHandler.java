package com.allclearlecture.domain.fileupload.exception;


import com.allclearlecture.domain.fileupload.enums.FileUploadErrorCode;
import com.allclearlecture.global.apiPayload.exception.GeneralException;

public class KafkaConsumerExceptionHandler extends GeneralException {


    public KafkaConsumerExceptionHandler(FileUploadErrorCode errorCode) {
        super(errorCode);
    }
}
