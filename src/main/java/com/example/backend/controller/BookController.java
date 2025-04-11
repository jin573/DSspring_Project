package com.example.backend.controller;

import com.example.backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("book") //localhost:8080/book
public class BookController {

    @Autowired
    private BookService bookService;
}
