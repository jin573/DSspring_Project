package com.example.backend.dto;

import com.example.backend.model.RatingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RatingDTO {
    private String userId;
    private String bookId;
    private int rating;

    public RatingDTO(final RatingEntity entity) {
        this.userId = entity.getUserId();
        this.bookId = entity.getBookId();
        this.rating = entity.getRating();
    }

    public static RatingEntity toEntity(final RatingDTO ratingDTO) {
        return RatingEntity.builder()
                .userId(ratingDTO.getUserId())
                .bookId(ratingDTO.getBookId())
                .rating(ratingDTO.getRating())
                .build();
    }
}
