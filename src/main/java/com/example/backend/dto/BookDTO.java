package com.example.backend.dto;

import com.example.backend.Enum.Category;
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
    private String id;
    private String title;
    private String author;
    private String publisher;

    private Category category;
    private String userId;

    public BookDTO(final BookEntity entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.publisher = entity.getPublisher();
        this.category = entity.getCategory();
        this.userId = entity.getUserId();
    }

    public static BookEntity toEntity(final BookDTO bookDTO){
        return BookEntity.builder()
                .id(bookDTO.getId())
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .publisher(bookDTO.getPublisher())
                .category(bookDTO.getCategory())
                .userId(bookDTO.getUserId())
                .build();
    }
}
