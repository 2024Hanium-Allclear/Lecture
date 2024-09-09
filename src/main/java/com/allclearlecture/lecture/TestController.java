package com.allclearlecture.lecture;

import com.allclearlecture.lecture.global.apiPayload.ApiResponse;
import com.allclearlecture.lecture.global.apiPayload.code.GeneralErrorCode;
import com.allclearlecture.lecture.global.apiPayload.exception.GeneralException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("lecture-health");
    }

    @GetMapping("/test1")
    public ApiResponse<String> test1() {
        return ApiResponse.onSuccess("handlerTest");
    }

    @GetMapping("/test2")
    public ApiResponse<String> test2() {
        throw new GeneralException(GeneralErrorCode.INTERNAL_SERVER_ERROR_500);
    }
}
