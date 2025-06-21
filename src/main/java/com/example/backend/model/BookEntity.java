package com.example.backend.model;

import com.example.backend.Enum.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="Book")
public class BookEntity {
    @Id
    @GeneratedValue(generator = "system--uuid")
    @GenericGenerator(name="system--uuid", strategy="uuid")
    private String id;

    private String userId;
    private String title;
    private String author;
    private String publisher;
    @Enumerated(EnumType.STRING)
    private Category category;

}
