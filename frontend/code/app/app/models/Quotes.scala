package models

import play.api._
import play.api.mvc._
import play.api.libs.json._

import reactivemongo.bson._


case class Quotes(cityName: String, quotes: List[String])

object Quotes {

  implicit val reader: BSONDocumentReader[Quotes] = Macros.reader[Quotes]

  implicit val quoteWrites = new Writes[Quotes] {
    def writes(quotes: Quotes) = Json.obj(
      "cityName" -> quotes.cityName,
      "quotes" -> Json.toJson(quotes.quotes)
    )
  }

  def getWrites(names: Map[String, City]) = new Writes[Quotes] {
    def writes(quotes: Quotes) = Json.obj(
      "city" -> (names.get(quotes.cityName) match {
        case Some(city) => Json.toJson(city)
        case _ => Json.obj("name" -> quotes.cityName)
      }),
      "quotes" -> Json.toJson(quotes.quotes)
    )
  }
}
