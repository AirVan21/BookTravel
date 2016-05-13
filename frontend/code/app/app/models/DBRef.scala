package models

import play.api._
import play.api.mvc._
import play.api.libs.json._

import reactivemongo.bson._

case class DBRef(collection: String, id: BSONObjectID) 

object DBRef { 

  implicit object DBRefReader extends BSONDocumentReader[DBRef] { 
    def read(bson: BSONDocument) = 
      DBRef( 
        bson.getAs[String]("$ref").get, 
        bson.getAs[BSONObjectID]("$id").get) 
  } 

  implicit object DBRefWriter extends BSONDocumentWriter[DBRef] { 
    def write(ref: DBRef) = 
      BSONDocument( 
        "$ref" -> ref.collection, 
        "$id" -> ref.id) 
  }
}