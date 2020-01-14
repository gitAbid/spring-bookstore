package com.abid.springbookstore.controller.v1

import com.abid.springbookstore.dto.AuthorDTO
import com.abid.springbookstore.dto.ResponseDTO
import com.abid.springbookstore.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/authors")
class AuthorController(val authorService: AuthorService) {

    @GetMapping
    fun getAuthors(@RequestParam limit: Optional<Int>, @RequestParam sort: Optional<String>):
            ResponseEntity<ResponseDTO<List<AuthorDTO>>> {
        return ResponseEntity(authorService.getAllAuthors(limit.orElse(Int.MAX_VALUE), sort.orElse("id")),
                HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getAuthor(@PathVariable id: Long): ResponseEntity<ResponseDTO<AuthorDTO>> {
        return ResponseEntity(authorService.getAuthor(id), HttpStatus.OK)

    }

    @PostMapping
    fun addAuthor(@RequestBody authorDTO: AuthorDTO): ResponseEntity<ResponseDTO<AuthorDTO>> {
        return ResponseEntity(authorService.addAuthor(authorDTO), HttpStatus.OK)
    }

    @PutMapping
    fun updateAuthor(@RequestBody authorDTO: AuthorDTO): ResponseEntity<ResponseDTO<AuthorDTO>> {
        return ResponseEntity(authorService.updateAuthor(authorDTO), HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteAuthor(@PathVariable id: Long): ResponseEntity<ResponseDTO<String>> {
        return ResponseEntity(authorService.deleteAuthor(id), HttpStatus.OK)
    }
}