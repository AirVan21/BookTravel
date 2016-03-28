package models

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.iteratee.Iteratee

import reactivemongo.bson.BSONDocument
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection

import models._

object DB {

  val driver = new MongoDriver
  val connection = driver.connection(List("db"))
  val db = connection("geobook")

  val books = db("books")
  val cities = db("cities")

}