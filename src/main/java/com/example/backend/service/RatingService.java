package com.example.backend.service;

import com.example.backend.model.RatingEntity;
import com.example.backend.persistence.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public RatingEntity saveRating(RatingEntity rating) {
        if(rating.getRating() >= 1&& rating.getRating() <= 5){
            return ratingRepository.save(rating);
        }else {
            throw new RuntimeException("평점은 1점과 5점 사이에서 선택할 수 있습니다.");
        }
    }

    public List<RatingEntity> getRatingsByBook(String title, String author, String publisher) {
        return ratingRepository.findByBook(title, author, publisher);
    }

    public List<RatingEntity> getRatingsByUser(String userId) {
        return ratingRepository.findByUserId(userId);
    }

    public Double getAvgRatingsByBook(String title, String author, String publisher){
        return ratingRepository.findAverageRatingByBookId(title, author, publisher);
    }

    public void deleteByUserIdAndBookId(String userId, String bookId){
        ratingRepository.deleteByUserIdAndBookId(userId, bookId);
    }

    public void deleteByBookId(String bookId) {
        ratingRepository.deleteByBookId(bookId);
    }

    public List<RatingEntity> getAllRating(){
        return ratingRepository.findAll();
    }
}
