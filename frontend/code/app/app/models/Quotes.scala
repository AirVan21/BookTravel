package models

import play.api._
import play.api.mvc._
import play.api.libs.json._

import reactivemongo.bson._


case class Quotes(cityName: String, quotes: List[DBRef]) {
  def getQuoteIds = quotes.take(Quotes.MAX_QUOTES) map { ref => ref.id }
}

object Quotes {

  val MAX_QUOTES = 30

  implicit val reader: BSONDocumentReader[Quotes] = Macros.reader[Quotes]

  def getWrites(names: Map[String, City])(quoteMap: Map[BSONObjectID, Quote]) = new Writes[Quotes] {
    def writes(quotes: Quotes) = Json.obj(
      "city" -> (names.get(quotes.cityName) match {
        case Some(city) => Json.toJson(city)
        case _ => Json.obj("name" -> quotes.cityName)
      }),
      "quotes" -> Json.toJson(quotes.quotes map { quote => quoteMap.get(quote.id) match {
        case Some(real) => Json.toJson(real)
        case _ => null
      } } filter { x => x != null} )
    )
  }
}
