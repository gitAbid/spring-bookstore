package com.abid.springbookstore.service

import com.abid.springbookstore.dto.BookDTO
import com.abid.springbookstore.dto.ResponseDTO
import com.abid.springbookstore.model.Book
import com.abid.springbookstore.repository.AuthorRepository
import com.abid.springbookstore.repository.BookRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class BookService(val bookRepository: BookRepository) {

    fun getAllBooks(pageable: PageRequest): ResponseDTO<List<BookDTO>> {
        val books = bookRepository.findAll(pageable)
        return if (books.isEmpty) {
            ResponseDTO(error = "No books found!")
        } else {
            ResponseDTO(data = books.map {
                BookDTO(id = it.id, title = it.title, description = it.description,
                        published_date = it.publishedDate, publisher = it.publisher)
            }.toList())
        }

    }

    fun getBook(id: Long): ResponseDTO<BookDTO> {
        val bookOptional = bookRepository.findById(id)
        return if (bookOptional.isPresent) {
            val book = bookOptional.get()
            ResponseDTO(data = BookDTO(id = book.id, title = book.title, description = book.description,
                    published_date = book.publishedDate, publisher = book.publisher))
        } else {
            ResponseDTO(error = "No book found with id of $id")
        }
    }

    fun deleteBook(id: Long): ResponseDTO<String> {
        val book = bookRepository.findById(id)
        if (book.isPresent) {
            return try {
                bookRepository.delete(book.get())
                ResponseDTO(data = "Book deleted successfully with id of $id.")
            } catch (e: Exception) {
                ResponseDTO(error = e.message)
            }

        } else {
            return ResponseDTO(error = "No book found with id $id")
        }
    }

    fun addBook(bookDTO: BookDTO): ResponseDTO<BookDTO> {
        return bookRepository.save(Book(title = bookDTO.title, description = bookDTO.description,
                publishedDate = bookDTO.published_date, publisher = bookDTO.publisher)).let {
            ResponseDTO(data = BookDTO(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    published_date = it.publishedDate,
                    publisher = it.publisher
            ))
        }

    }


    fun updateBook(bookDTO: BookDTO): ResponseDTO<BookDTO> {
        val responseDTO = ResponseDTO<BookDTO>(error = "")
        bookDTO.id?.let {
            val bookOptional = bookRepository.findById(it)
            if (bookOptional.isPresent) {
                    bookRepository.save(Book(id = bookDTO.id, title = bookDTO.title,description = bookDTO.description,
                            publishedDate = bookDTO.published_date,
                            publisher = bookDTO.publisher)).let {
                        responseDTO.data = BookDTO(
                                id = it.id,
                                title = it.title,
                                description = it.description,
                                published_date = it.publishedDate,
                                publisher = it.publisher)

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