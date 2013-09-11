package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import base._
import models._

object Guestbook extends Controller {

  val guestbook = registry guestbookService

  def add = Action { implicit request =>
    form.GuestbookForm.bindFromRequest.fold(
      errors => true,
      entry => {
        guestbook.add(entry)
        true
      })
    
    val result = guestbook.list()
    Ok(views.html.guestbook(result))
  }

  def list = Action {
    val result = guestbook.list()

    Ok(views.html.guestbook(result))
  }

}