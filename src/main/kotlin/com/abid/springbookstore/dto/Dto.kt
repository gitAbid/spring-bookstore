package com.abid.springbookstore.dto

class ResponseDTO<T>(var data: T? = null, var error: String?=null)
class AuthorDTO(var id: Long?, var name: String, var books: List<Long?> = listOf())
class BookDTO(var id: Long?, var title: String, var description: String, var authors: List<Long?> = listOf())