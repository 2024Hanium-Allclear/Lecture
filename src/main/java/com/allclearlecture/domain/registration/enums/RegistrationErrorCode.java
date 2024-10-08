package com.allclearlecture.domain.registration.enums;

import com.allclearlecture.global.apiPayload.ApiResponse;
import com.allclearlecture.global.apiPayload.code.status.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RegistrationErrorCode implements BaseErrorCode {
    /* 400 BAD_REQUEST : 이 응답은 잘못된 문법으로 인해 서버가 요청을 이해할 수 없다는 의미입니다. */
    CREDIT_EXCEEDED(HttpStatus.BAD_REQUEST, "REGISTRATION400", "이수 가능 학점이 초과되었습니다."),

    /* 403 FORBIDDEN : 클라이언트가 콘텐츠에 접근할 권리를 가지고 있지 않다는 의미입니다.*/
    NO_AUTHORITY_TO_REGISTRATION(HttpStatus.FORBIDDEN, "REGISTRATION403", "해당 수강신청을 한 사용자가 아닙니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    SUBJECT_ALREADY_REGISTERED(HttpStatus.CONFLICT, "REGISTRATION409", "이미 신청한 과목입니다.\n(동일한 과목의 다른 분반을 다수 신청할 수 없습니다.)"),
    COURSE_ALREADY_FULLED(HttpStatus.CONFLICT, "REGISTRATION409", "수강 정원이 다 찼습니다."),
    COURSE_TIME_CONFLICT(HttpStatus.CONFLICT, "REGISTRATION409", "이미 신청한 강의와 시간이 겹칩니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ApiResponse<Void> getErrorResponse() {
        return ApiResponse.onFailure(code, message);
    }
}
