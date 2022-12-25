package com.example.dao

import com.example.models.Article

interface DAOFacade {

    suspend fun getAllArticles() : List<Article>
    suspend fun getArticle(id : Int) : Article?
    suspend fun addNewArticle(title : String, body : String) : Article?
    suspend fun editArticle(id : Int, title : String, body : String) : Boolean
    suspend fun deleteArticle(id : Int) : Boolean

}