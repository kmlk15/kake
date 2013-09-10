package models

import scala.reflect.BeanProperty
import play.api.data.Forms._
import play.api.data._
import play.api.data.format.Formats._
import play.api.libs.json._

object form {
  val GuestbookForm = Form(
    mapping(
      "id" -> of[Long],
      "name" -> text,
      "message" -> text)(Guestbook.apply)(Guestbook.unapply))
      
  val InviteeForm = Form(
    mapping(
      "id" -> of[Long],
      "gid" -> of[Long], 
      "leader" -> number, 
      "name" -> text, 
      "ceremony" -> number, 
      "reception" -> number, 
      "tnum" -> number, 
      "meal" -> number)(Invitee.apply)(Invitee.unapply))
}
