package com.abid.springbookstore.service

import com.abid.springbookstore.dto.BookDTO
import com.abid.springbookstore.dto.ResponseDTO
import com.abid.springbookstore.model.Author
import com.abid.springbookstore.model.Book
import com.abid.springbookstore.repository.AuthorRepository
import com.abid.springbookstore.repository.BookRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.awt.print.Pageable
import java.util.stream.Collectors

@Service
class BookService(val authorRepository: AuthorRepository, val bookRepository: BookRepository) {

    fun getAllBooks(limit: Int, sort: String): ResponseDTO<List<BookDTO>> {
        val pageable=PageRequest.of(0,limit, Sort.by(sort))
        val responseDTO = ResponseDTO<List<BookDTO>>()
        val books = bookRepository.findAll(pageable)
        if (books.isEmpty) {
            responseDTO.error = "No books found!"
        } else {
            val bookDTOs = arrayListOf<BookDTO>()
            books.forEach {
                val authors = it.authors.stream().map { it.id }.collect(Collectors.toList())
                val bookDTO = BookDTO(id = it.id, title = it.title, authors = authors, description = it.description,
                        published_date = it.publishedDate, publisher = it.publisher)
                bookDTOs.add(bookDTO)

            }
            responseDTO.data = bookDTOs
        }

        return responseDTO
    }

    fun getBook(id: Long): ResponseDTO<BookDTO> {
        val responseDTO = ResponseDTO<BookDTO>()
        val bookOptional = bookRepository.findById(id)

        if (bookOptional.isPresent) {
            val book = bookOptional.get()
            val authors = book.authors.stream().map { it.id }.collect(Collectors.toList())
            val bookDTO = BookDTO(id = book.id, title = book.title, authors = authors, description = book.description,
                    published_date = book.publishedDate, publisher = book.publisher)
            responseDTO.data = bookDTO
        } else {
            responseDTO.error = "No book found with id of $id"
        }

        return responseDTO
    }

    fun deleteBook(id: Long): ResponseDTO<String> {
        val book = bookRepository.findById(id)
        val responseDTO = ResponseDTO<String>()

        if (book.isPresent) {
            try {
                bookRepository.delete(book.get())
                responseDTO.data = "Book deleted successfully with id of $id."
            } catch (e: Exception) {
                responseDTO.error = e.message
            }

        } else {
            responseDTO.error = "No book found with id $id"
        }

        return responseDTO
    }

    fun addBook(bookDTO: BookDTO): ResponseDTO<BookDTO> {
        var isValid = true
        val responseDTO = ResponseDTO<BookDTO>(error = "")
        val authors = arrayListOf<Author>()
        bookDTO.authors.forEach {
            it?.let {
                val author = authorRepository.findById(it)
                if (author.isPresent) authors.add(author.get()) else {
                    responseDTO.error += "No author found with id of $it."
                    isValid = false;
                }
            } ?: run {
                isValid = false;
                responseDTO.error += "Invalid author id $it. Please provide a valid author id."
            }
        }

        if (isValid) {
            var book = Book(title = bookDTO.title, authors = authors, description = bookDTO.description,
                    publishedDate = bookDTO.published_date, publisher = bookDTO.publisher)
            book = bookRepository.save(book)
            responseDTO.data = BookDTO(
                    id = book.id,
                    title = book.title,
                    description = book.description,
                    published_date = book.publishedDate,
                    publisher = book.publisher,
                    authors = book.authors
                            .stream()
                            .map { it.id }
                            .collect(Collectors.toList()))
        }

        return responseDTO
    }

    fun updateAuthor(bookDTO: BookDTO): ResponseDTO<BookDTO> {
        val responseDTO = ResponseDTO<BookDTO>(error = "")
        var isValid = true
        val authors = arrayListOf<Author>()

        bookDTO.id?.let {
            val authorOptional = authorRepository.findById(it)
            if (authorOptional.isPresent) {
                bookDTO.authors.forEach {
                    it?.let {
                        val author = authorRepository.findById(it)
                        if (author.isPresent) authors.add(author.get()) else {
                            responseDTO.error += "No author found with id of $it."
                            isValid = false;
                        }
                    } ?: run {
                        isValid = false;
                        responseDTO.error += "Invalid author id $it. Please provide a valid author id."
                    }
                }

                if (isValid) {
                    var book = Book(id = bookDTO.id, title = bookDTO.title,
                            authors = authors, description = bookDTO.description,
                            publishedDate = bookDTO.published_date,
                            publisher = bookDTO.publisher)
                    book = bookRepository.saveAndFlush(book)
                    responseDTO.data = BookDTO(
                            id = book.id,
                            title = book.title,
                            description = book.description,
                            published_date = book.publishedDate,
                            publisher = book.publisher,
                            authors = book.authors
                                    .stream()
                                    .map { it.id }
                                    .collect(Collectors.toList()))
                }
            } else {
                responseDTO.error += "No book found with id of $it."
            }

        } ?: run {
            responseDTO.error = "Invalid book id ${bookDTO.id}. Please provide a valid id"
        }

        return responseDTO;
    }
}