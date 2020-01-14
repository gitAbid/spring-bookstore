package com.abid.springbookstore.controller.v1

import com.abid.springbookstore.dto.BookDTO
import com.abid.springbookstore.dto.ResponseDTO
import com.abid.springbookstore.service.BookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.swing.text.html.Option

@RestController
@RequestMapping("/v1/books")
class BookController(val bookService: BookService) {

    @GetMapping
    fun getBooks(@RequestParam limit: Optional<Int>, @RequestParam sort: Optional<String>):
            ResponseEntity<ResponseDTO<List<BookDTO>>> {
        return ResponseEntity(bookService.getAllBooks(limit.orElse(Int.MAX_VALUE), sort.orElse("id")), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getBook(@PathVariable id: Long): ResponseEntity<ResponseDTO<BookDTO>> {
        return ResponseEntity(bookService.getBook(id), HttpStatus.OK)

    }

    @PostMapping
    fun addBook(@RequestBody bookDTO: BookDTO): ResponseEntity<ResponseDTO<BookDTO>> {
        return ResponseEntity(bookService.addBook(bookDTO), HttpStatus.OK)
    }

    @PutMapping
    fun updateBook(@RequestBody bookDTO: BookDTO): ResponseEntity<ResponseDTO<BookDTO>> {
        return ResponseEntity(bookService.updateAuthor(bookDTO), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable id: Long): ResponseEntity<ResponseDTO<String>> {
        return ResponseEntity(bookService.deleteBook(id), HttpStatus.OK)
    }
}