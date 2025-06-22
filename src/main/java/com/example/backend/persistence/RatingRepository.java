package com.example.backend.persistence;

import com.example.backend.model.RatingEntity;
import com.example.backend.model.RatingId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, RatingId> {
    @Query("SELECT r FROM RatingEntity r WHERE r.book.title = ?1 AND r.book.author = ?2 AND r.book.publisher = ?3")
    List<RatingEntity> findByBook(String bookTitle, String author, String publisher);
    List<RatingEntity> findByUserId(String userId);

    @Query("SELECT AVG(r.rating) FROM RatingEntity r WHERE r.book.title = ?1 AND r.book.author = ?2 AND r.book.publisher = ?3")
    Double findAverageRatingByBookId(String bookTitle, String author, String publisher);

    @Modifying
    @Transactional
    @Query("DELETE FROM RatingEntity r WHERE r.userId = ?1 AND r.bookId = ?2")
    void deleteByUserIdAndBookId(String userId, String bookId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RatingEntity r WHERE r.bookId = ?1")
    void deleteByBookId(String bookId);
}
