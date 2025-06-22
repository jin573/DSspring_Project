package com.example.backend.service;

import com.example.backend.model.BookEntity;
import com.example.backend.persistence.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;


    public List<BookEntity> getAllBookList(final String temporaryUserId) {
        return bookRepository.findByUserId(temporaryUserId);
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

    public List<BookEntity> createBook(final BookEntity bookEntity) {
        validate(bookEntity); //bookEntity가 null을 가지고 있지 않게 유효성 검사를 한다.

        bookRepository.save(bookEntity);
        return bookRepository.findByUserId(bookEntity.getUserId());
    }

    public List<BookEntity> getBookToList(final String temporaryUserId, final String bookTitle) {
        //userid가 가지고 있는  bookList중 bookTitle에 해당하는 객체가 있는지 조회해야 한다.
        /* case
        * 1. book title에 해당하는 bookEntity가 없는 경우 (0인 경우) -> [] 빈 객체 반환
        * 2. 1개 이상 있는 경우 -> 개수 만큼 출력*/
        return bookRepository.findByUserIdAndTitle(temporaryUserId, bookTitle);
    }

    public List<BookEntity> updateBook(final BookEntity bookEntity, boolean isAdmin) {
        //entity의 값을 변경했으므로 유효성 검사를 한다.
        validate(bookEntity);

        //id 검색으로 기존 entity를 가져온다.
        final Optional<BookEntity> optionalBookEntity = bookRepository.findById(bookEntity.getId());

        //title을 변경한다.
        optionalBookEntity.ifPresent(book ->{
            if (!isAdmin && !book.getUserId().equals(bookEntity.getUserId())) {
                throw new RuntimeException("다른 사용자의 책은 수정할 수 없습니다.");
            }
            checkBookState(book, bookEntity);
            bookRepository.save(book);
        });
        /* 변경된 book entity를 반환한다. id는 데이터마다 고유한 값을 가지므로
         * getBookToList를 사용하여 하나의 값만 반환하게 한다.*/
        if (isAdmin) {
            return bookRepository.findByTitle(bookEntity.getTitle());
        } else {
            return getBookToList(bookEntity.getUserId(), bookEntity.getTitle());
        }
    }

    private void checkBookState(BookEntity originalBook, BookEntity updateBook){
        if(updateBook.getTitle() != null) originalBook.setTitle(updateBook.getTitle());
        if(updateBook.getAuthor() != null) originalBook.setAuthor(updateBook.getAuthor());
        if(updateBook.getPublisher() != null) originalBook.setPublisher(updateBook.getPublisher());
        if(updateBook.getCategory() != null) originalBook.setCategory(updateBook.getCategory());
    }

    public List<BookEntity> deleteBook(BookEntity bookEntity, boolean isAdmin) {
        //해당 id로 하는 entity가 존재하는지 확인한다.
        validate(bookEntity);

        try {
            Optional<BookEntity> optionalBook = bookRepository.findById(bookEntity.getId());

            optionalBook.ifPresent(book -> {
                if (!isAdmin && !book.getUserId().equals(bookEntity.getUserId())) {
                    throw new RuntimeException("다른 사용자의 책은 삭제할 수 없습니다.");
                }
                bookRepository.delete(book);
            });
        } catch (Exception e) {
            log.error("error deleting entity", bookEntity.getId(), e);
            throw new RuntimeException("error deleting entity" + bookEntity.getId());
        }

        // 삭제 후 리스트 반환
        return isAdmin ? getAllBooks() : getAllBookList(bookEntity.getUserId());
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<BookEntity> getAllBooksByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
}
