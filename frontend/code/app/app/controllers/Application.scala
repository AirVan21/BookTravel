package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import models._

class Application extends Controller {

  def index = Action {
    Ok(views.html.base(views.html.index()))
  }

}
