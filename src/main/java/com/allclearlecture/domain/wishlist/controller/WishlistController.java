package com.allclearlecture.domain.wishlist.controller;


import com.allclearlecture.domain.wishlist.dto.WishlistRequestDTO;
import com.allclearlecture.domain.wishlist.entity.Wishlist;
import com.allclearlecture.domain.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WishlistController {

    private static final Logger logger = LoggerFactory.getLogger(WishlistController.class);
    private final WishlistService wishlistService;

    @GetMapping("/wishlist")
    public String showWishlist(Model model) {
        /*Long studentId = getStudentIdFromAuthentication();*/

        List<Wishlist> wishlistItems = wishlistService.getLecturesSortedByPriority();
        double totalCredits = wishlistService.getTotalCredits();
        double totalOcuCredits = wishlistService.getTotalOcuCredits();

        model.addAttribute("lectures", wishlistItems);
        model.addAttribute("totalCredits", totalCredits);
        model.addAttribute("totalOcuCredits", totalOcuCredits);
        return "wishlist/wishlist"; // Thymeleaf 템플릿 이름
    }

    @GetMapping("/addwishlist")
    public String showAddWishlistPage(Model model) {
        model.addAttribute("wishlistRequest", new WishlistRequestDTO());
        return "wishlist/addwishlist"; // 뷰 템플릿 이름
    }

    @PostMapping("/wishlist/savePriorities")
    public String savePriorities(@RequestParam Map<String, String> priorities) {
        /*Long studentId = getStudentIdFromAuthentication();*/

        /*wishlistService.updatePriorities(studentId, priorities);*/
        return "redirect:/wishlist";
    }

    @GetMapping("/wishlist/delete/{id}")
    public String deleteWishlist(@PathVariable("id") Long id) {
        try {
            wishlistService.deleteWishlist(id);
            return "redirect:/wishlist";
        } catch (Exception e) {
            e.printStackTrace();
            return "error/errorPage";
        }
    }

    @PostMapping("/wishlist/add")
    public String addLectureToWishlist(@ModelAttribute("request") WishlistRequestDTO request, RedirectAttributes redirectAttributes) {
        try {
            /*Long studentId = getStudentIdFromAuthentication();*/

            /*wishlistService.addLectureToWishlist(request.getLectureCode(), request.getDivision(), studentId);*/
            redirectAttributes.addFlashAttribute("successMessage", "위시리스트에 성공적으로 추가되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "해당 강좌를 찾을 수 없거나 추가할 수 없습니다.");
        }
        return "redirect:/wishlist";
    }

    /*private Long getStudentIdFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 사용자 이름으로 학생 ID 조회
            return fetchStudentIdFromUsername(userDetails.getUsername()); // 사용자 이름으로 학생 ID를 조회
        }
        return null;
    }*/

    private Long fetchStudentIdFromUsername(String username) {
        // 사용자 이름으로 학생 ID를 조회
        // username으로 ID를 반환
        return 202345L;
    }
}
