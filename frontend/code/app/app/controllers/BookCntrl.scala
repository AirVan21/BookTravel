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

  def searchCity(cityName: String) = Action.async {
    Book.findByCityName(DB.books)(cityName) map { books =>
      implicit val writes = Book.getSimpleBookWrites(cityName)
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
    Ok(views.html.base(views.html.book(titleId)))
  }

  def pageListMain = Action {
    Redirect(routes.BookCntrl.pageList(1))
  }

  def pageList(pageNum: Int) = Action {
    Ok(views.html.base(views.html.bookList(pageNum)))
  }

  def pageSearch = Action {
    Ok(views.html.base(views.html.findBook()))
  }

  def searchBook(words: String) = Action.async {
    Book.findByTitleOrAuthor(DB.books)(words.split(" ").toList) map {books =>
      implicit val writes = Book.getSimpleBookWrites("")
      Ok(Json.toJson(books))
    }
  }

  def getList(pageNum: Int) = Action.async {
    Book.getInRange(DB.books)((pageNum - 1) * 50, 50) map { books =>
      implicit val writes = Book.getSimpleBookWrites("")
      Ok(Json.toJson(books))
    }
  }

}
