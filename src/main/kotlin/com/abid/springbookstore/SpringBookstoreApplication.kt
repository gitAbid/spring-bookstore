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
        val book = Book(title = "Adolescent Health Screening: an Update in the Age of Big Data")
        val book1 = Book(title = "Atlas of Wound Healing")
        val book2 = Book(title = "Anatomy, Imaging and Surgery of the Intracranial Dural Venous Sinuses")
        val book3 = Book(title = "Complications in Male Circumcision")

        val author = Author(name = "William Shakespeare", books = listOf(book))
        val author1 = Author(name = "Emily Dickinson")
        val author2 = Author(name = "H. P. Lovecraft")
        val author3 = Author(name = "Arthur Conan Doyle")



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
