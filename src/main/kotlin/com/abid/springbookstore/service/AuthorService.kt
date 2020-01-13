package com.abid.springbookstore.service

import com.abid.springbookstore.dto.AuthorDTO
import com.abid.springbookstore.dto.ResponseDTO
import com.abid.springbookstore.model.Author
import com.abid.springbookstore.model.Book
import com.abid.springbookstore.repository.AuthorRepository
import com.abid.springbookstore.repository.BookRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class AuthorService(val authorRepository: AuthorRepository, val bookRepository: BookRepository) {

    fun getAllAuthors(): ResponseDTO<List<AuthorDTO>> {
        val responseDTO = ResponseDTO<List<AuthorDTO>>()
        val authors = authorRepository.findAll()
        if (authors.isEmpty()) {
            responseDTO.error = "No authors found!"
        } else {
            val authorDTOs = arrayListOf<AuthorDTO>()
            authors.forEach {
                val bookIds = it.books.stream().map { it.id }.collect(Collectors.toList())
                val authorDTO = AuthorDTO(id = it.id, name = it.name, books = bookIds)
                authorDTOs.add(authorDTO)

            }
            responseDTO.data = authorDTOs
        }

        return responseDTO
    }

    fun getAuthor(id: Long): ResponseDTO<AuthorDTO> {
        val responseDTO = ResponseDTO<AuthorDTO>()
        val author = authorRepository.findById(id)

        if (author.isPresent) {
            val authorData = author.get()
            val bookIds = authorData.books.stream().map { authorData.id }.collect(Collectors.toList())
            val authorDTO = AuthorDTO(id = authorData.id, name = authorData.name, books = bookIds)
            responseDTO.data = authorDTO
        } else {
            responseDTO.error = "No authors found with id of $id"
        }

        return responseDTO
    }

    fun deleteAuthor(id: Long) {

    }

    fun addAuthor(authorDTO: AuthorDTO): ResponseDTO<AuthorDTO> {
        var isValid = true
        val responseDTO = ResponseDTO<AuthorDTO>(error = "")
        val books = arrayListOf<Book>()
        authorDTO.books.forEach {
            if (it != null) {
                val book = bookRepository.findById(it)
                if (book.isPresent) {
                    books.add(book.get())
                } else {
                    responseDTO.error += "No book found with id of $it."
                    isValid = false;
                }

            } else {
                isValid = false;
                responseDTO.error += "Invalid book id $it. Please provide a valid book id."
            }
        }

        if (isValid) {
            var author = Author(name = authorDTO.name, books = books)
            author = authorRepository.save(author)
            responseDTO.data = AuthorDTO(
                    id = author.id,
                    name = author.name,
                    books = author.books
                            .stream()
                            .map { book -> book.id }
                            .collect(Collectors.toList()))
        }

        return responseDTO
    }

    fun updateAuthor() {

    }
}