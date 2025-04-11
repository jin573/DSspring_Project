package com.example.backend.service;

import com.example.backend.persistence.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;


}
