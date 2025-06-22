package com.example.backend.controller;

import com.example.backend.dto.BookDTO;
import com.example.backend.dto.RatingDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.RatingEntity;
import com.example.backend.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rating")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    //평점 남기는 메서드
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> rateBook(Authentication authentication, @RequestBody RatingDTO ratingDTO) {
        try {
            //유저가 평점을 남길 때 사용
            //유저의 id, 책이름, rating을 Entity에 저장한다.
            String userId = authentication.getName();
            ratingDTO.setUserId(userId);

            RatingEntity ratingEntity = RatingDTO.toEntity(ratingDTO);
            ratingEntity.setUserId(userId);
            RatingEntity saved = ratingService.saveRating(ratingEntity);
            //dto 반환
            RatingDTO response = new RatingDTO(saved);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<RatingDTO> responseDTO = ResponseDTO.<RatingDTO>builder()
                    .error(error).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/book")
    public ResponseEntity<?> getRatingsByBook(@RequestBody BookDTO bookDTO) {
        //특정 책에 대한 모든 평점 -> 관리자만 확인
        //도서 id와 상관없이 책 제목, 작가, 출판사가 동일하면 평점을 모두 보여줌
        List<RatingEntity> ratingEntityList = ratingService.getRatingsByBook(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPublisher());
        List<RatingDTO> result = ratingEntityList.stream().map(RatingDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<?> getRatingsByUser(@AuthenticationPrincipal String userId) {
        //본인이 남긴 평점들 확인
        List<RatingEntity> ratingEntityList = ratingService.getRatingsByUser(userId);
        List<RatingDTO> result = ratingEntityList.stream().map(RatingDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/average")
    public Double getAvgRatingByBook(@RequestBody BookDTO bookDTO){
        /*책 이름에 해당하는 List를 1차적으로 불러온다.
        * 그 후 작가, 출판사가 동일한 책들만 남긴 후
        * 평균을 구한다.
        * */
        Double avgRating = ratingService.getAvgRatingsByBook(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPublisher());
        return ResponseEntity.ok().body(avgRating != null ? avgRating : 0.0).getBody();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRatings() {
        try {
            List<RatingEntity> ratings = ratingService.getAllRating();
            return ResponseEntity.ok().body(ratings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("전체 평점 조회 실패: " + e.getMessage());
        }
    }

}