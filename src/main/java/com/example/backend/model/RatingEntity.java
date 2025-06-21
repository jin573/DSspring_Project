package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*유저는 하나의 책에 대해 한 번만 평점을 남길 수 있다.
* 따라서 복합 키 (userId, bookId)를 유일한 식별자로 설정해야 한다 -> IdClass*/
@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RatingId.class)
public class RatingEntity {

    @Id
    private String userId;

    @Id
    private String bookId;

    private int rating; //범위 1~5

    @ManyToOne
    @JoinColumn(name="bookId", insertable = false, updatable = false)
    private BookEntity book;




}
