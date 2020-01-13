package com.abid.springbookstore.controller.v1

import com.abid.springbookstore.dto.AuthorDTO
import com.abid.springbookstore.dto.ResponseDTO
import com.abid.springbookstore.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/authors")
class AuthorController(val authorService: AuthorService) {

    @GetMapping
    fun getAuthors(): ResponseEntity<ResponseDTO<List<AuthorDTO>>> {
        return ResponseEntity(authorService.getAllAuthors(), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getAuthor(@PathVariable id: Long): ResponseEntity<ResponseDTO<AuthorDTO>> {
        return ResponseEntity(authorService.getAuthor(id), HttpStatus.OK)

    }

    @PostMapping
    fun addAuthors(@RequestBody authorDTO: AuthorDTO): ResponseEntity<ResponseDTO<AuthorDTO>> {
        return ResponseEntity(authorService.addAuthor(authorDTO), HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun updateAuthor(@PathVariable id: Long) {

    }

    @DeleteMapping("/{id}")
    fun deleteAuthor(@PathVariable id: Long) {

    }
}