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


case class Quote(_id: BSONObjectID, source: String, sentiment: String)

object Quote {

  implicit val reader: BSONDocumentReader[Quote] = Macros.reader[Quote]

  implicit val quoteWrites = new Writes[Quote] {
    def writes(quote: Quote) = Json.obj(
      "text" -> quote.source,
      "sentiment" -> quote.sentiment
    )
  }



  def findByIds(collection: BSONCollection)(ids: List[BSONObjectID]) = {
    val query = BSONDocument(
        "$or" -> (ids map { id => BSONDocument("_id" -> id) })
        // "isNotable" -> true
    )

    collection.
    find(query).
    // sort(BSONDocument("raiting" -> -1)).
    cursor[Quote].
    collect[List](10000)

  }

  DB.quotes.indexesManager.ensure(
    Index(List("_id" -> IndexType.Ascending))
  )
}
