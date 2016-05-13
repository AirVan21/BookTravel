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

class QuoteCntrl extends Controller {
  def up(id: String) = Action {
    Ok("up")
  }

  def down(id: String) = Action {
    Ok("down")
  }
}
