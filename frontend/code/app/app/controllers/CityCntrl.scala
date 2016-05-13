package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models._

import reactivemongo.bson._
import scala.concurrent._
import scala.concurrent.duration._

class CityCntrl extends Controller {

  def search(name: String) = Action.async {
    City.findByPrefix(DB.cities)(name) map { result => 
      Ok(Json.toJson(result))
    }
  }

  def page(name: String) = Action {
    Ok(views.html.base(views.html.city(name)))
  }

}
