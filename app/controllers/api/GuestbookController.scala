package controllers.api

import play.api._
import play.api.mvc._
import play.api.libs.json._
import base._
import models._

object GuestbookController extends Controller {

  val guestbook = registry guestbookService

  def add = Action { implicit request =>
    form.GuestbookForm.bindFromRequest.fold(
      errors => Status(404),
      entry => {
        guestbook.add(entry)
        Status(200)
      })
  }

  def list() = Action {
    val result = guestbook.list()

    Ok(views.html.guestbook(result))
  }

}