package models

import scala.reflect.BeanProperty
import play.api.data.Forms._
import play.api.data._
import play.api.data.format.Formats._
import play.api.libs.json._

case class Invitee(id: Long, gid: Long, leader: Int, name: String, ceremony: Int, reception: Int, tnum: Int, meal: Int)
