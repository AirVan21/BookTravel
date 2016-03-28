package models

import play.api._
import play.api.mvc._
import play.api.libs.json._

import reactivemongo.bson._
import reactivemongo.api.indexes._
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global


case class Book(_id: BSONObjectID, title: String, cities: List[Quotes]) {

  val id: String = title

  def getCityNames = cities map { _.cityName }

}

object Book {

  val TITLE = "title"
  val ID = "id"
  val CITIES = "cities"

  implicit val reader: BSONDocumentReader[Book] = Macros.reader[Book]

  def getWrites(cityMap: Map[String, City]) = new Writes[Book] {
    implicit val quotesWrite = Quotes.getWrites(cityMap)
    def writes(book: Book) = Json.obj(
      TITLE -> book.title,
      ID -> book._id.stringify,
      CITIES -> Json.toJson(book.cities)
    )
  }

  implicit val bookWrites = new Writes[Book] {
    def writes(book: Book) = Json.obj(
      TITLE -> book.title,
      ID -> book._id.stringify,
      CITIES -> Json.toJson(book.cities)
    )
  }

  val getSimpleBookWrites = new Writes[Book]{
    def writes(book: Book) = Json.obj(
      TITLE -> book.title,
      ID -> book._id.stringify
    )
  }

  def findByCityName(collection: BSONCollection)(name: String): Future[List[Book]] = {
    val query = BSONDocument(
      "cities.cityName" -> BSONDocument(
        "$regex" -> ("^" + name + "$"),
        "$options" -> "i"
      )
    )

    collection.
    find(query).
    cursor[Book]().
    collect[List](100)
  }

  def findById(collection: BSONCollection)(titleId: String): Future[Option[Book]] = {
    val query = BSONDocument("_id" -> BSONObjectID(titleId))
    
    collection.
    find(query).
    one[Book]
  }


  // On start
  DB.books.indexesManager.ensure(
    Index(List("cities.cityName" -> IndexType.Ascending))
  )

}

