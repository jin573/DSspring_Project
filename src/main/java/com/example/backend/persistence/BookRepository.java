package com.example.backend.persistence;

import com.example.backend.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {

    List<BookEntity> findByUserId(String temporaryUserId);

    List<BookEntity> findByUserIdAndTitle(String temporaryUserId, String bookTitle);
    List<BookEntity> findByTitle(String bookTitle);
}
