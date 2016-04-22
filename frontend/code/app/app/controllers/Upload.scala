package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import models._

import java.io.File


class Upload extends Controller {

  def page = Action {
    Ok(views.html.base(views.html.upload()))
  }

  val dir: File = new File("uploaded")
  dir.mkdir()

  def file = Action(parse.multipartFormData) { request =>
    request.body.file("book").map { book =>
      val filename = book.filename
      val contentType = book.contentType
      book.ref.moveTo(new File(dir.getName + "/" + filename))
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Missing file")
    }
  }

}