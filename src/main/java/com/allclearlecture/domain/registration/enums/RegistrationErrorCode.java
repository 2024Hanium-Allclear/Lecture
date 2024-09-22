package com.allclearlecture.domain.registration.enums;

import com.allclearlecture.global.apiPayload.ApiResponse;
import com.allclearlecture.global.apiPayload.code.status.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RegistrationErrorCode implements BaseErrorCode {
    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    SUBJECT_ALREADY_REGISTERED(HttpStatus.CONFLICT, "REGISTRATION409", "이미 신청한 과목입니다.\n(동일한 과목의 다른 분반을 다수 신청할 수 없습니다.)"),
    COURSE_ALREADY_FULLED(HttpStatus.CONFLICT, "REGISTRATION409", "수강 정원이 다 찼습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }
}
