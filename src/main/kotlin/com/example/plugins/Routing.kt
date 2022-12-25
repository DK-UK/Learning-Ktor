package com.example.plugins

import com.example.models.Article
import com.example.models.Article.Companion.articles
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import sun.rmi.runtime.Log
import java.util.logging.Logger

fun Application.configureRouting() {
    

    routing {

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("files")
        }
        get("/") {
            call.respondRedirect("articles")
        }
        route("/articles"){
            get{
                // for all articles
                call.respond(FreeMarkerContent("index.ftl", mapOf("articles" to articles)))
            }
            get("new"){
                // Show a page with fields for creating a new article
                call.respond(FreeMarkerContent("new.ftl", null))
            }
            post {
                // to save article
                val formParameters = call.receiveParameters()
                val title = formParameters.getOrFail("title")
                val body = formParameters.getOrFail("body")
                val article = Article.newEntry(title, body)
                articles.add(article)
                call.respondRedirect("/articles/${article.id}")
            }
            get("{id}") {
                // Show an article with a specific id
                val id = call.parameters.getOrFail("id").toInt()
                val article = articles.find { it.id == id }
                call.respond(FreeMarkerContent("show.ftl", mapOf("article" to articles.find { it.id == id})))
            }
            get("{id}/edit") {
                // Show a page with fields for editing an article
                val id = call.parameters.getOrFail("id").toInt()
                val article = articles.find {it.id == id}
                call.respond(FreeMarkerContent("edit.ftl", mapOf("article" to article)))
            }
            post("{id}") {
                // Update or delete an article
                val formParameters = call.receiveParameters()
                val id = call.parameters.getOrFail("id").toInt()
                when(formParameters.getOrFail("_action")){
                    "update" ->{
                        val index = articles.indexOf(articles.find { it.id == id })
                        val title = formParameters.getOrFail("title")
                        val body = formParameters.getOrFail("body")
                        articles[index].title = title
                        articles[index].body = body
                        call.respondRedirect("/articles/${id}")
                    }
                    "delete" -> {
                        articles.removeIf { it.id == id }
                        call.respondRedirect("/articles")
                    }
                }
            }

        }
    }
}
