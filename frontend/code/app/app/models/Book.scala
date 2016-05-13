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


case class Book(
    _id: BSONObjectID
  , title: String
  , authors: Option[List[Author]]
  , cities: List[Quotes]
  , description: String) {

  def getCityNames = cities map { _.cityName }
  def getQuoteIds = cities.map(quotes => quotes.getQuoteIds).flatten
  def getQuoteByCityName(cityName: String) = cities.find(quotes => quotes.cityName == cityName)
    .map(q => q.quotes.take(3).map(_.id))
}

object Book {

  val TITLE = "title"
  val ID = "id"
  val CITIES = "cities"
  val AUTHORS = "authors"
  val QUOTES = "quotes"
  val DESCRIPTION = "description"

  implicit val reader: BSONDocumentReader[Book] = Macros.reader[Book]

  def getWrites(cityMap: Map[String, City])(quoteMap: Map[BSONObjectID, Quote]) = new Writes[Book] {
    implicit val quotesWrite = Quotes.getWrites(cityMap)(quoteMap)
    def writes(book: Book) = Json.obj(
      TITLE -> book.title,
      ID -> book._id.stringify,
      CITIES -> Json.toJson(book.cities),
      AUTHORS -> Json.toJson(book.authors),
      DESCRIPTION -> Json.toJson(book.description)
    )
  }

  def getSimpleBookWrites(cityName: String)(quoteMap: Map[BSONObjectID, Quote]) = new Writes[Book]{
    def writes(book: Book) = Json.obj(
      TITLE -> book.title,
      ID -> book._id.stringify,
      AUTHORS -> Json.toJson(book.authors),
      QUOTES -> Json.toJson {
        val quotesOption = book.cities find { _.cityName == cityName}
        quotesOption match {
          case Some(quotes) => quotes.quotes.take(3).map(x => quoteMap.get(x.id)).flatten
          case _ => List()
        }
        
      }
    )
  }

  def findByCityName(collection: BSONCollection)(name: String): Future[List[Book]] = {
    val query = BSONDocument(
      "cities.cityName" -> BSONDocument(
        "$regex" -> ("^" + name + "$"),
        "$options" -> "i"
      )
    )

    // println("here")

    collection
    .find(query)
    .cursor[Book]()
    .collect[List](100)
  }

  def findById(collection: BSONCollection)(titleId: String): Future[Option[Book]] = {
    val query = BSONDocument("_id" -> BSONObjectID(titleId))
    
    collection
    .find(query)
    .one[Book]
  }

  def getInRange(collection: BSONCollection)(skip: Int, count: Int): Future[List[Book]] = {
    collection
    .find(BSONDocument())
    .sort(BSONDocument("title" -> 1))
    .options(QueryOpts(skip))
    .cursor[Book]()
    .collect[List](count)
  }

  def findByTitleOrAuthor(collection: BSONCollection)(words: List[String]): Future[List[Book]] = {
    val query = BSONDocument(
      "$and" -> (words map { word =>
        BSONDocument(
          "$or" -> BSONArray(
            BSONDocument(
              "title" -> BSONDocument(
                "$regex" -> (/*"^" + */word),
                "$options" -> "i"
              )
            ),
            BSONDocument(
              "authors.firstName" -> BSONDocument(
                "$regex" -> ("^" + word),
                "$options" -> "i"
              )
            ),
            BSONDocument(
              "authors.lastName" -> BSONDocument(
                "$regex" -> ("^" + word),
                "$options" -> "i"
              )
            )
          )
        )
      })
    )

    collection
    .find(query)
    .cursor[Book]()
    .collect[List](100)
  }


  // On start
  DB.books.indexesManager.ensure(
    Index(List("cities.cityName" -> IndexType.Ascending))
  )

  DB.books.indexesManager.ensure(
    Index(List("title" -> IndexType.Ascending))
  )

  DB.books.indexesManager.ensure(
    Index(List("authors.lastName" -> IndexType.Ascending))
  )

  DB.books.indexesManager.ensure(
    Index(List("authors.firstName" -> IndexType.Ascending))
  )

}

