package models


import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._
import play.api.libs.json._

import reactivemongo.bson._
import reactivemongo.api.indexes._
import reactivemongo.api.collections.bson.BSONCollection

case class City(cityName: String, locations: List[Location])

object City {

  val NAME = "name"

  implicit val reader: BSONDocumentReader[City] = Macros.reader[City]

  implicit val cityWrites = new Writes[City] {
    def writes(city: City) = {

      val location = city.locations.head match {
        case location: Location => location
        case _ => Location(0, 0)
      }
      
      Json.obj(
        NAME -> city.cityName,
        "lat" -> location.lat,
        "lng" -> location.lng
      )
    }
  }

  def findByPrefix(collection: BSONCollection)(prefix: String): Future[List[City]] = {
    val query = BSONDocument(
      "cityName" -> BSONDocument("$regex" -> ("^" + prefix), "$options" -> "i")
    )

    collection.
    find(query).
    cursor[City]().
    collect[List](100)
  }

  def findMultiple(collection: BSONCollection)(names: List[String]): Future[List[City]] = {
    val query = BSONDocument(
      "$or" -> (names map { name => BSONDocument("cityName" -> name) })
    )

    collection.
    find(query).
    // hint(BSONDocument("cityName" -> 1)).
    cursor[City]().
    collect[List](100)
  }

  // On start
  DB.cities.indexesManager.ensure(
    Index(List("cityName" -> IndexType.Ascending))
  )
  
}

