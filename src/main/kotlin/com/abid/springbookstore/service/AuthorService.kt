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

    //TODO fixme issue misleading {Why sending books too?}
    //TODO fixme send paging object from controller
    fun getAllAuthors(pageable: PageRequest): ResponseDTO<List<AuthorDTO>> {
        val authors = authorRepository.findAll(pageable)
        return if (authors.isEmpty) ResponseDTO(error = "No authors found!")
        else ResponseDTO(data = authors.map { it ->
            AuthorDTO(id = it.id, name = it.name, books = it.books.map { it.id })
        }.toList())

    }

    fun getAuthor(id: Long): ResponseDTO<AuthorDTO> {
        val author = authorRepository.findById(id)
        return if (author.isPresent) ResponseDTO(data = author.get().let { it ->
            AuthorDTO(id = it.id, name = it.name, books = it.books.map { it.id })
        })
        else ResponseDTO(error = "No authors found with id of $id")
    }

    fun deleteAuthor(id: Long): ResponseDTO<String> {
        val author = authorRepository.findById(id)
        return if (author.isPresent) {
            try {
                authorRepository.delete(author.get())
                ResponseDTO(data = "Author deleted successfully with id of $id.")
            } catch (e: Exception) {
                return ResponseDTO(error = e.message)
            }
        } else {
            ResponseDTO(error = "No author found with id $id")
        }
    }

    //TODO Next Session Thursday
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
                    responseDTO.data = Author(id = authorDTO.id, name = authorDTO.name, books = books).let { it ->
                        authorRepository.save(it)
                        AuthorDTO(
                                id = it.id,
                                name = it.name,
                                books = it.books
                                        .stream()
                                        .map { book -> book.id }
                                        .collect(Collectors.toList()))
                    }
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