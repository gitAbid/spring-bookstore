package com.abid.springbookstore.dto

import java.sql.Date

class ResponseDTO<T>(var data: T? = null, var error: String? = null)
class AuthorDTO(var id: Long?, var name: String)
class BookDTO(var id: Long?, var title: String, var description: String, var publisher: String, var published_date: Date?)