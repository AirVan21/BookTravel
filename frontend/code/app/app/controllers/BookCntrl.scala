package controllers

import models._

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import reactivemongo.bson._
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent._
import scala.concurrent.duration._

class BookCntrl extends Controller {

  def search(cityName: String) = Action.async {
    Book.findByCityName(DB.books)(cityName) map { books =>
      implicit val writes = Book.getSimpleBookWrites
      Ok(Json.toJson(books))
    }
  }

  def get(titleId: String) = Action.async {

    (for {
      book <- Book.findById(DB.books)(titleId) map { case Some(book) => book }
      citiesOption <- City.findMultiple(DB.cities)(book.getCityNames)
    } yield {
      val cityMap = (citiesOption match {
        case cities: List[City] => cities.groupBy(_.cityName)
      }) mapValues { _.head }
      implicit val bookWrites = Book.getWrites(cityMap)
      Ok(Json.toJson(book))
    }) recover { case _ => NotFound("No such book in DB.") }
  }

  def page(titleId: String) = Action {
    Ok(views.html.book(titleId))
  }

}
