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
    private String id;
    private String title;
    private String author;
    private String publisher;

    public BookDTO(final BookEntity entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.publisher = entity.getPublisher();
    }

    public static BookEntity toEntity(final BookDTO bookDTO){
        return BookEntity.builder()
                .userId(bookDTO.getId())
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .publisher(bookDTO.getPublisher())
                .build();
    }
}
