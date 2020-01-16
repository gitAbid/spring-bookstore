package com.abid.springbookstore.service

import com.abid.springbookstore.dto.AuthorDTO
import com.abid.springbookstore.dto.ResponseDTO
import com.abid.springbookstore.model.Author
import com.abid.springbookstore.repository.AuthorRepository
import com.abid.springbookstore.repository.BookRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AuthorService(val authorRepository: AuthorRepository) {

    //TODO fixme issue misleading {Why sending books too?}
    //TODO fixme send paging object from controller
    fun getAllAuthors(pageable: PageRequest): ResponseDTO<List<AuthorDTO>> {
        val authors = authorRepository.findAll(pageable)
        return if (authors.isEmpty) ResponseDTO(error = "No authors found!")
        else ResponseDTO(data = authors.map {
            AuthorDTO(id = it.id, name = it.name)
        }.toList())

    }

    fun getAuthor(id: Long): ResponseDTO<AuthorDTO> {
        val author = authorRepository.findById(id)
        return if (author.isPresent) ResponseDTO(data = author.get().let {
            AuthorDTO(id = it.id, name = it.name)
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
        return ResponseDTO(data = authorRepository.save(Author(name = authorDTO.name)).let {
            AuthorDTO(id = it.id, name = it.name)
        })
    }

    fun updateAuthor(authorDTO: AuthorDTO): ResponseDTO<AuthorDTO> {
        val responseDTO = ResponseDTO<AuthorDTO>(error = "")
        authorDTO.id?.let {
            val authorOptional = authorRepository.findById(it)
            if (authorOptional.isPresent) {
                responseDTO.data = Author(id = authorDTO.id, name = authorDTO.name).let {
                    authorRepository.save(it)
                    AuthorDTO(id = it.id, name = it.name)
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