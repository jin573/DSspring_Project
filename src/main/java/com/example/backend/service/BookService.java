package com.example.backend.service;

import com.example.backend.model.BookEntity;
import com.example.backend.persistence.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;

@Slf4j
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;


    public List<BookEntity> getAllBookList(final String temporaryUserId) {
        return bookRepository.findByUserId(temporaryUserId);
    }

    public List<BookEntity> createBook(final BookEntity bookEntity) {
        validate(bookEntity);

        bookRepository.save(bookEntity);
        return bookRepository.findByUserId(bookEntity.getUserId());
    }

    private void validate(BookEntity bookEntity) {
        if(bookEntity == null){
            log.warn("Entity cannot be null");
            throw new RuntimeException();
        }

        if(bookEntity.getUserId() == null){
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    public List<BookEntity> getBookToList(final String temporaryUserId, final String bookTitle) {
        //userid가 가지고 있는  book entity 중 bookTitle에 해당하는 객체가 있는지 조회해야 한다.
        /* case
        * 1. book title에 해당하는 bookEntity가 없는 경우 (0인 경우) -> [] 빈 객체 반환
        * 2. 1개 이상 있는 경우 -> 개수 만큼 출력*/
        return bookRepository.findByUserIdAndTitle(temporaryUserId, bookTitle);
    }
}
