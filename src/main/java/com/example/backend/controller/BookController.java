package com.example.backend.controller;

import com.example.backend.dto.BookDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.BookEntity;
import com.example.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("book") //localhost:8080/book
public class BookController {

    @Autowired
    private BookService bookService;


    //bookDTO 객체를 데이터베이스에 입력하고 기존의 리스트에 추가하여 전체 데이터를 반환한다.
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDTO bookDTO){
        try{
            String temporaryUserId = "KimJinSeon";
            //entity 객체를 생성
            BookEntity bookEntity = BookDTO.toEntity(bookDTO);
            bookEntity.setId(null);
            bookEntity.setUserId(temporaryUserId);
            //entity 리스트에 추가
            List<BookEntity> bookEntityList = bookService.createBook(bookEntity);

            //response body에 담기 위해 dto로 변경
            List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList());
            ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build();

            return  ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //userID 가 가지고 있는 모든 도서 데이터를 반환
    @GetMapping
    public ResponseEntity<?> getBookList(){
        String temporaryUserId = "KimJinSeon";
        //userid 로 저장된 book entity 검색
        List<BookEntity> bookEntityList = bookService.getBookList(temporaryUserId);
        //response body에 담기 위해 dto로 변경
        List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList());
        ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
