package models

import scala.reflect.BeanProperty
import play.api.data.Forms._
import play.api.data._
import play.api.data.format.Formats._
import play.api.libs.json._

case class Guestbook(id: Long, name: String, message: String)

case class GuestbookDisplay(name: String, message: String, date: String)