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
    public ResponseEntity<?> getAllBookList(){
        String temporaryUserId = "KimJinSeon";
        //userid 로 저장된 book entity 검색
        List<BookEntity> bookEntityList = bookService.getAllBookList(temporaryUserId);
        //response body에 담기 위해 dto로 변경
        List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList());
        ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    //전체 데이터가 아닌 특정 book dto를 검색했을 때 반환
    @GetMapping("/search/{bookTitle}")
    public ResponseEntity<?> getBookToList(@PathVariable String bookTitle){
        /* 클라이언트가 book title을 검색 시
        * 해당하는 dto 객체들 responseEntity 에 담아 반환한다.
        * 동일한 책 제목의 다른 출반사가 있을 수 있으므로, list 형태로 반환한다.
        * userid 로 저장된 book entity 검색*/
        String temporaryUserId = "KimJinSeon";
        List<BookEntity> bookEntityList = bookService.getBookToList(temporaryUserId, bookTitle); //service에서 검색하여 entity에 반환

        /*responseEntity에 담아 반환하기 위해
        * 1. bookEntity를 bookDTO로 변환
        * 2. bookDTO를 responseDTO에 담음
        * 3. responseEntity에 responseDTO를 담아 반환한다.*/
        List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList()); //1
        ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build(); //2
        return ResponseEntity.ok().body(responseDTO); //3
    }
}
