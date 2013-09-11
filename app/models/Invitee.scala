package models

import scala.reflect.BeanProperty
import play.api.data.Forms._
import play.api.data._
import play.api.data.format.Formats._
import play.api.libs.json._

case class Invitee(id: Long, gid: Long, name: String, ceremony: Int, reception: Int, tnum: Int, meal: Int)

case class Leader(id: Long, name: String)