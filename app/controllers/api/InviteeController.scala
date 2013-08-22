package controllers.api

import play.api._
import play.api.mvc._
import play.api.libs.json._
import base._

object InviteeController extends Controller {

  val invitee = registry inviteeService

  def action = Action { implicit request =>
    {
      val map = request.body.asFormUrlEncoded.getOrElse(Map.empty[String, Seq[String]]).map { case (k, v) => k -> v.mkString }
      
      play.Logger.info(map.toString)
      
      map("oper") match {
        case "add" => 
          invitee.add(map)
          Status(200)
        case "edit" => 
          invitee.update(map)
          Status(200)
        case _ =>
          Status(500)
      }
    }
  }

  def get(id: String) = Action {
    val result = invitee.get(id)

    Ok(Json.toJson(result))
  }

  def list() = Action {
    val result = invitee.list()

    Ok(Json.toJson(result))
  }
  
  def total() = Action {
    val result = invitee.total()
    
    Ok(Json.toJson(result))
  }

}