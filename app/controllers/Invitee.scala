package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import base._
import models._

object Invitee extends Controller {

  val invitee = registry inviteeService

  def index = Action {
    val invitees = invitee.list
    val leaders = invitee.leaders
    val total = invitee.total

    Ok(views.html.admin.index(invitees, leaders, total))
  }

  def add = Action { implicit request =>
    form.InviteeForm.bindFromRequest.fold(
      errors => {
        play.Logger.error(errors.toString)
        true
      },
      entry => {
        invitee.add(entry)
        true
      })

    val leaders = invitee.leaders

    Ok(views.html.admin.add(leaders))
  }

  def edit = Action { implicit request =>
    form.InviteeForm.bindFromRequest.fold(
      errors => {
        play.Logger.error(errors.toString)
        true
      },
      entry => {
        invitee.edit(entry)
        true
      })

    val leaders = invitee.leaders

    Ok(views.html.admin.add(leaders))
  }

  def get(id: String) = Action {
    val current = invitee.get(id)
    val leaders = invitee.leaders

    Ok(views.html.admin.edit(current, leaders))
  }

  def list() = Action {
    val invitees = invitee.list

    Ok(views.html.admin.list(invitees))
  }

  def total() = Action {
    val result = invitee.total()

    Ok(views.html.admin.summary(result))
  }

}