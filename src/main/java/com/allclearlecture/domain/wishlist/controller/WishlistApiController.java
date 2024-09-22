package com.allclearlecture.domain.wishlist.controller;


import com.allclearlecture.domain.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistApiController {

    private final WishlistService wishlistService;

    @PostMapping("/savePriorities")
    public ResponseEntity<Void> savePriorities(@RequestParam Map<String, String> priorities) {
        Long studentId = getStudentIdFromAuthentication(); // 공통 사용자 정보
        wishlistService.updatePriorities(studentId, priorities);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 사용자 정보
    private Long getStudentIdFromAuthentication() {
        // Spring Security
        // Authentication... /*로그인된 사용자의 ID를 추출*/
        return 1L;
    }
}
