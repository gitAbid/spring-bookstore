package com.abid.springbookstore.model

import java.sql.Date
import javax.persistence.*

@Entity
class Author(@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
             var id: Long? = null,
             var name: String = "") {

}

@Entity
class Book(@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
           var id: Long? = null,
           var title: String = "",
           var description: String = "",
           var publisher: String = "",
           var publishedDate: Date? = null) {

}