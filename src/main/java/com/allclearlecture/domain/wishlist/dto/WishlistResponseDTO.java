package com.allclearlecture.domain.wishlist.dto;


import com.allclearlecture.domain.wishlist.entity.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishlistResponseDTO {
    private final Long wishlistId;
    private final String lectureCode;
    private final String division;
    private final String lectureName;
    private final int credit;
    private final String professor;
    private final int priority;

    public static WishlistResponseDTO from(final Wishlist wishlist) {
        return new WishlistResponseDTO(
                wishlist.getId(),
                wishlist.getLecture().getLectureCode(),
                wishlist.getLecture().getDivision(),
                wishlist.getLecture().getLectureName(),
                wishlist.getLecture().getCredit(),
                wishlist.getLecture().getProfessor().getName(),
                wishlist.getPriority()
        );
    }
}
