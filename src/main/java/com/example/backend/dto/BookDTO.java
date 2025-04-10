package com.example.backend.dto;

import com.example.backend.model.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookDTO {
    private String userId;
    private String title;
    private String author;
    private String publisher;

    public BookDTO(final BookEntity entity){
        this.userId = entity.getUserId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.publisher = entity.getPublisher();
    }
}
