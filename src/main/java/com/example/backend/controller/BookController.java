package com.example.backend.controller;

import com.example.backend.dto.BookDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.model.BookEntity;
import com.example.backend.service.BookService;
import com.example.backend.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("book") //localhost:8080/book
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private RatingService ratingService;

    /*
    * bookDTO 객체를 입력하고
    * 추가 시 마다, 전체 리스트를 반환해야 한다.*/
    @PostMapping
    public ResponseEntity<?> createBook(Authentication authentication, @RequestBody BookDTO bookDTO){
        try{
            //userID는 본인의 영문명으로 해야 하므로 코드 안에서 임의로 저장한다.
            //String temporaryUserId = "KimJinSeon";
            String userId = authentication.getName(); // 로그인된 사용자 ID
            BookEntity bookEntity = BookDTO.toEntity(bookDTO);
            bookEntity.setId(null);
            bookEntity.setUserId(userId);

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
    public ResponseEntity<?> getAllBookList(Authentication authentication){
        //userid 로 저장된 book entity 검색한다.
        //String temporaryUserId = "KimJinSeon";
        String userId = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        List<BookEntity> bookEntityList;
        if(isAdmin){
            bookEntityList = bookService.getAllBooks();
        }else{
            bookEntityList = bookService.getAllBookList(userId);
        }


        //response body에 담기 위해 dto로 변경한다.
        List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList());
        ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    /*제품의 title이 JSON 형태로 입력되면
    * 해당하는 bookEntity를 반환해야 한다.*/
    @GetMapping("/search")
    public ResponseEntity<?> getBookToList(Authentication authentication, @RequestParam String title){
        /* 클라이언트가 book title을 검색 시
        * 동일한 책 제목의 다른 출반사가 있을 수 있으므로, list 형태로 반환한다.
        * 모든 titel이 아닌, 클라이언트의 bookList 중에서 반환해야 하므로 userID와 함께 검색한다.*/
        //String temporaryUserId = "KimJinSeon";
        String userId = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        List<BookEntity> bookEntityList;
        if(isAdmin){
            bookEntityList = bookService.getAllBooksByTitle(title); // 전체 도서에서 title 검색
        } else {
            bookEntityList = bookService.getBookToList(userId, title); // 본인 도서에서 title 검색
        }
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
    public ResponseEntity<?> updateBook(Authentication authentication, @RequestBody BookDTO bookDTO){
        //입력된 dto에 userID를 추가해준다.
        //String temporaryUserId = "KimJinSeon";
        String userId = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        BookEntity bookEntity = BookDTO.toEntity(bookDTO);

        if(!isAdmin){
            // 관리자 권한이면 userId 무시하고 그냥 수정 가능
            bookEntity.setUserId(userId);
        }
        //해당 dto가 존재하는지 확인 후 있으면 새로운 title로 변경한다.
        List<BookEntity> bookEntityList = bookService.updateBook(bookEntity, isAdmin);

        //response body에 담기 위해 dto로 변경한다.
        List<BookDTO> bookDTOList = bookEntityList.stream().map(BookDTO::new).collect(Collectors.toList());
        ResponseDTO<BookDTO> responseDTO = ResponseDTO.<BookDTO>builder().data(bookDTOList).build();
        return ResponseEntity.ok().body(responseDTO);
    }

    /*클라이언트는 검색 된 책 데이터들 중 하나의 제품 id만 복사하여 입력한다.
    * 해당 id를 가진 책 데이터를 삭제한 후
    * 전체 리스트를 반환한다.*/

    @DeleteMapping
    public ResponseEntity<?> deleteBook(Authentication authentication, @RequestBody BookDTO bookDTO){
        try{
            //userID는 임의로 저장되어있으므로 다시 저장한 후 검색한다.
            //String temporaryUserId = "KimJinSeon";
            String userId = authentication.getName();
            boolean isAdmin = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN"));

            BookEntity bookEntity = BookDTO.toEntity(bookDTO);

            if(!isAdmin){
                bookEntity.setUserId(userId);  // 일반 유저는 본인 도서만 삭제 가능
                ratingService.deleteByUserIdAndBookId(userId, bookEntity.getId());

            }else{
                ratingService.deleteByBookId(bookEntity.getId());
            }

            List<BookEntity> bookEntityList = bookService.deleteBook(bookEntity, isAdmin);

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
