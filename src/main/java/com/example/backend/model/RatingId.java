package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RatingId implements Serializable {
    private String userId;
    private String bookId;

    @Override
    public boolean equals(Object obj) {
        //실제 동일한 객체를 가져오는지 확인
        if(this == obj) return true;
        if(!(obj instanceof RatingId)) return false;
        RatingId rating = (RatingId) obj;
        return Objects.equals(rating.userId, userId) && Objects.equals(rating.bookId, bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookId);
    }
}
