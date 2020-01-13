package com.abid.springbookstore.model

import javax.persistence.*

@Entity
class Author(@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
             var id: Long? = null,
             var name: String = "",
             @ManyToMany(cascade = arrayOf(
                     CascadeType.DETACH,
                     CascadeType.MERGE,
                     CascadeType.REFRESH,
                     CascadeType.PERSIST), fetch = FetchType.LAZY)
             var books: List<Book> = listOf()) {

}

@Entity
class Book(@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
           var id: Long? = null,
           var title: String = "",
           var description: String = "",
           @ManyToMany(mappedBy = "books", cascade = arrayOf(
                   CascadeType.DETACH,
                   CascadeType.MERGE,
                   CascadeType.REFRESH,
                   CascadeType.PERSIST), fetch = FetchType.LAZY)
           var authors: List<Author> = listOf()) {

}