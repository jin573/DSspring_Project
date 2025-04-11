package com.example.backend.controller;

import com.example.backend.dto.BookDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.BookEntity;
import com.example.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Book;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("book") //localhost:8080/book
public class BookController {

    @Autowired
    private BookService bookService;

    //userID 가 가지고 있는 모든 도서 데이터를 반환
    @GetMapping()
    public ResponseEntity<?> getBookList(){
        String temporaryUserId = "KimJinSeon";

        List<BookEntity> bookEntityList = bookService.getBookList(temporaryUserId);
        List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList());
        ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
