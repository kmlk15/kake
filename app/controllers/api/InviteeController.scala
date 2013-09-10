package controllers.api

import play.api._
import play.api.mvc._
import play.api.libs.json._
import base._

object InviteeController extends Controller {

  val invitee = registry inviteeService

  def add = Action { implicit request =>
    Status(200)
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