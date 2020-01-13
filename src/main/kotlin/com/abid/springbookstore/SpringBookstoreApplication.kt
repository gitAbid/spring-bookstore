package com.abid.springbookstore

import com.abid.springbookstore.model.Author
import com.abid.springbookstore.model.Book
import com.abid.springbookstore.repository.AuthorRepository
import com.abid.springbookstore.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SpringBookstoreApplication {

    @Bean
    fun run(authorRepository: AuthorRepository, bookRepository: BookRepository) = CommandLineRunner {
        val book = Book(title = "New BOok1")
        val book1 = Book(title = "New BOok12")
        val book2 = Book(title = "New BOok13")
        val book3 = Book(title = "New BOok4")

        val author = Author(name = "Abid", books = listOf(book))
        val author1 = Author(name = "Abid1")
        val author2 = Author(name = "Abid2")
        val author3 = Author(name = "Abid3")



        authorRepository.save(author)
        authorRepository.save(author1)
        authorRepository.save(author2)
        authorRepository.save(author3)


        bookRepository.save(book)
        bookRepository.save(book1)
        bookRepository.save(book2)
        bookRepository.save(book3)
    }

}


fun main(args: Array<String>) {
    runApplication<SpringBookstoreApplication>(*args)
}
