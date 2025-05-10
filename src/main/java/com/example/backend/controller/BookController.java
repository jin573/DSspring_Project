package com.example.backend.controller;

import com.example.backend.dto.BookDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.BookEntity;
import com.example.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("book") //localhost:8080/book
public class BookController {

    @Autowired
    private BookService bookService;


    /*
    * bookDTO 객체를 입력하고
    * 추가 시 마다, 전체 리스트를 반환해야 한다.*/
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDTO bookDTO){
        try{
            //userID는 본인의 영문명으로 해야 하므로 코드 안에서 임의로 저장한다.
            String temporaryUserId = "KimJinSeon";
            BookEntity bookEntity = BookDTO.toEntity(bookDTO);
            bookEntity.setId(null);
            bookEntity.setUserId(temporaryUserId);

            //entity 리스트에 추가한다.
            List<BookEntity> bookEntityList = bookService.createBook(bookEntity);

            //response body에 담기 위해 dto로 변경한다.
            List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList());
            ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build();
            return  ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    /*userID 가 가지고 있는 모든 도서 데이터를 반환한다. */
    @GetMapping
    public ResponseEntity<?> getAllBookList(){
        //userid 로 저장된 book entity 검색한다.
        String temporaryUserId = "KimJinSeon";
        List<BookEntity> bookEntityList = bookService.getAllBookList(temporaryUserId);

        //response body에 담기 위해 dto로 변경한다.
        List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList());
        ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    /*제품의 title이 JSON 형태로 입력되면
    * 해당하는 bookEntity를 반환해야 한다.*/
    @GetMapping("/search")
    public ResponseEntity<?> getBookToList(@RequestParam String title){
        /* 클라이언트가 book title을 검색 시
        * 동일한 책 제목의 다른 출반사가 있을 수 있으므로, list 형태로 반환한다.
        * 모든 titel이 아닌, 클라이언트의 bookList 중에서 반환해야 하므로 userID와 함께 검색한다.*/
        String temporaryUserId = "KimJinSeon";
        List<BookEntity> bookEntityList = bookService.getBookToList(temporaryUserId, title); //service에서 검색하여 entity에 반환
        System.out.println(bookEntityList.stream().toList());

        //response body에 담기 위해 dto로 변경한다.
        List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList());
        ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    /*클라이언트는 검색 된 책 데이터들 중 하나의 제품 정보를 복사하여
    * title만 수정한다.
    * 해당 id의 제품 정보를 검색하고,
    * title 값을 수정해야 한다.
    * 수정된 제품의 정보만 반환한다.*/
    @PutMapping
    public ResponseEntity<?> updateBook(@RequestBody BookDTO bookDTO){
        //입력된 dto에 userID를 추가해준다.
        String temporaryUserId = "KimJinSeon";
        BookEntity bookEntity = BookDTO.toEntity(bookDTO);
        bookEntity.setUserId(temporaryUserId);

        //해당 dto가 존재하는지 확인 후 있으면 새로운 title로 변경한다.
        List<BookEntity> bookEntityList = bookService.updateBook(bookEntity);

        //response body에 담기 위해 dto로 변경한다.
        List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList());
        ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    /*클라이언트는 검색 된 책 데이터들 중 하나의 제품 id만 복사하여 입력한다.
    * 해당 id를 가진 책 데이터를 삭제한 후
    * 전체 리스트를 반환한다.*/
    @DeleteMapping
    public ResponseEntity<?> deleteBook(@RequestBody BookDTO bookDTO){
        try{
            //userID는 임의로 저장되어있으므로 다시 저장한 후 검색한다.
            String temporaryUserId = "KimJinSeon";
            BookEntity bookEntity = BookDTO.toEntity(bookDTO);
            bookEntity.setUserId(temporaryUserId);

            //해당 dto가 존재하는지 확인 후 삭제한다.
            List<BookEntity> bookEntityList = bookService.deleteBook(bookEntity);

            List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList());
            ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
