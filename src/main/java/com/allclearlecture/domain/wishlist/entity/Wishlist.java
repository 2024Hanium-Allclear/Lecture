package com.allclearlecture.domain.wishlist.entity;


import com.allclearlecture.domain.lecture.entity.Lecture;
import com.allclearlecture.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Wishlist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    private Long studentId;  // 학생 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")  // FK 칼럼
    private Lecture lecture;  // 강의 엔티티

    private int priority;  // 우선순위

    // 필요한 경우, 학생 ID와 강의 엔티티를 설정하는 생성자 추가
    public Wishlist(Long studentId, Lecture lecture, int priority) {
        this.studentId = studentId;
        this.lecture = lecture;
        this.priority = priority;
    }
    @Override
    public String toString() {
        return "Wishlist{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", lecture=" + lecture +
                ", priority=" + priority +
                '}';
    }
}
