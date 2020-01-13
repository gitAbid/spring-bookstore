package com.abid.springbookstore.model

import javax.persistence.*

@Entity
class Author(@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
             var id: Long? = null, var name: String = "",
             @ManyToMany
             var books: List<Book> = listOf()) {

}

@Entity
class Book(@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
           var id: Long? = null, var title: String = "",
           @ManyToMany(mappedBy = "books")
           var authors: List<Author> = listOf()) {

}