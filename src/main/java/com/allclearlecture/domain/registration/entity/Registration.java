package com.allclearlecture.domain.registration.entity;

import com.allclearlecture.domain.lecture.entity.Lecture;
import com.allclearlecture.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Registration extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Long id;

    private Long studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private int rank;

    @Builder.Default
    private boolean cancelStatus = false;

    public void delete() {
        this.cancelStatus = true;
    }
}
