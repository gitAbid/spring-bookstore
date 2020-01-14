package com.abid.springbookstore.service

import com.abid.springbookstore.dto.AuthorDTO
import com.abid.springbookstore.dto.ResponseDTO
import com.abid.springbookstore.model.Author
import com.abid.springbookstore.model.Book
import com.abid.springbookstore.repository.AuthorRepository
import com.abid.springbookstore.repository.BookRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class AuthorService(val authorRepository: AuthorRepository, val bookRepository: BookRepository) {

    fun getAllAuthors(limit: Int, sort: String): ResponseDTO<List<AuthorDTO>> {
        val pageable= PageRequest.of(0,limit, Sort.by(sort))
        val responseDTO = ResponseDTO<List<AuthorDTO>>()
        val authors = authorRepository.findAll(pageable)
        if (authors.isEmpty) {
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

    fun deleteAuthor(id: Long): ResponseDTO<String> {
        val author = authorRepository.findById(id)
        val responseDTO = ResponseDTO<String>()

        if (author.isPresent) {
            try {
                authorRepository.delete(author.get())
                responseDTO.data = "Author deleted successfully with id of $id."
            } catch (e: Exception) {
                responseDTO.error = e.message
            }

        } else {
            responseDTO.error = "No author found with id $id"
        }

        return responseDTO
    }

    fun addAuthor(authorDTO: AuthorDTO): ResponseDTO<AuthorDTO> {
        var isValid = true
        val responseDTO = ResponseDTO<AuthorDTO>(error = "")
        val books = arrayListOf<Book>()
        authorDTO.books.forEach {
            it?.let {
                val book = bookRepository.findById(it)
                if (book.isPresent) books.add(book.get()) else {
                    responseDTO.error += "No book found with id of $it."
                    isValid = false;
                }
            } ?: run {
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

    fun updateAuthor(authorDTO: AuthorDTO): ResponseDTO<AuthorDTO> {
        val responseDTO = ResponseDTO<AuthorDTO>(error = "")
        var isValid = true
        val books = arrayListOf<Book>()

        authorDTO.id?.let {
            val authorOptional = authorRepository.findById(it)
            if (authorOptional.isPresent) {
                authorDTO.books.forEach {
                    it?.let {
                        val book = bookRepository.findById(it)
                        if (book.isPresent) books.add(book.get()) else {
                            responseDTO.error += "No book found with id of $it."
                            isValid = false;
                        }
                    } ?: run {
                        isValid = false;
                        responseDTO.error += "Invalid book id $it. Please provide a valid book id."
                    }
                }

                if (isValid) {
                    var author = Author(id = authorDTO.id, name = authorDTO.name, books = books)
                    author = authorRepository.save(author)
                    responseDTO.data = AuthorDTO(
                            id = author.id,
                            name = author.name,
                            books = author.books
                                    .stream()
                                    .map { book -> book.id }
                                    .collect(Collectors.toList()))
                }

            } else {
                responseDTO.error += "No author found with id of $it."
            }

        } ?: run {
            responseDTO.error = "Invalid author id ${authorDTO.id}. Please provide a valid id"
        }

        return responseDTO;
    }
}