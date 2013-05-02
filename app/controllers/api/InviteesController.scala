package controllers.api

import play.api._
import play.api.mvc._
import play.api.libs.json._
import base._

object InviteeController extends Controller {

  val invitee = registry inviteeService

  def add = Action { implicit request =>
    {
      val map = request.queryString.map { case (k, v) => k -> v.mkString }
      invitee.add(map)
      Status(200)
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

  def update = Action { implicit request =>
    {
      val map = request.queryString.map { case (k, v) => k -> v.mkString }
      invitee.update(map)
      Status(200)
    }
  }

}