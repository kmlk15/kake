package models

import scala.reflect.BeanProperty
import play.api.data.Forms._
import play.api.data._
import play.api.data.format.Formats._
import play.api.libs.json._

case class Invitee(id: Long, gid: Long, name: String, ceremony: Int, reception: Int, tnum: Int, meal: Int)

case class InviteeDisplay(id: Long, name: String, isLeader: Boolean, ceremony: String, reception: String, tnum: Int, meal: String)

case class Leader(id: Long, name: String)

case class Total(total: Long, 
    cTotal: Long, cNotReplied: Long, cDeclined: Long, cGoing: Long, 
    rTotal: Long, rNotReplied: Long, rDeclined: Long, rGoing: Long, 
    mNotSelected: Long, mBeef: Long, mFish: Long, mVeg: Long, mKids: Long)