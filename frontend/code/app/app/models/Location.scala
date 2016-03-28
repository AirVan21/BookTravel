package models

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._
import play.api.libs.json._

import reactivemongo.bson._
import reactivemongo.api.collections.bson.BSONCollection

case class Location(lat: Double, lng: Double)

object Location {

  implicit val reader: BSONDocumentReader[Location] = Macros.reader[Location]

  implicit val LocationWrites = new Writes[Location] {
    def writes(location: Location) = Json.obj(
      "lat" -> location.lat,
      "lng" -> location.lng
    )
  }

  
}

