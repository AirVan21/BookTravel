package models

import play.api._
import play.api.mvc._
import play.api.libs.json._

import reactivemongo.bson._
import reactivemongo.api._
import reactivemongo.api.indexes._
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global


case class Author(firstName: String, lastName: String) 

object Author {

  implicit val reader: BSONDocumentReader[Author] = Macros.reader[Author]

  implicit val authorWrites = new Writes[Author] {
    def writes(author: Author) = Json.obj(
      "firstName" -> author.firstName,
      "lastName" -> author.lastName
    )
  }
}

