package services

import play.api.Play.current
import play.api.db._
import anorm._
import models._

trait InviteeServiceComponent {

  val inviteeService: InviteeService

  trait InviteeService {

    def add(invitee: Invitee): Boolean

    def update(invitee: Invitee): Boolean

    def get(id: String): Invitee

    def list(): List[InviteeDisplay]

    def leaders(): List[Leader]

    def total(): Map[String, String]
  }
}

trait InviteeServiceComponentImpl extends InviteeServiceComponent {

  this: InviteeServiceComponent =>

  override val inviteeService = new InviteeService {

    val rsvp : List[String] = List("Not Invited", "Invited", "Not Going", "Going")
    val meals : List[String] = List("Not Selected", "Filet Mignon", "Salmon", "Vegetarian", "Kids Meal")
    
    override def add(invitee: Invitee): Boolean = {
      try {
        DB.withConnection("kk")(implicit conn => {
          SQL("insert into invitee (id, gid, name, ceremony, reception, tnum, meal) values ({id}, {gid}, {name}, {ceremony}, {reception}, {tnum}, {meal});")
            .on("id" -> invitee.id,
              "gid" -> invitee.gid,
              "name" -> invitee.name,
              "ceremony" -> invitee.ceremony,
              "reception" -> invitee.reception,
              "tnum" -> invitee.tnum,
              "meal" -> invitee.meal)
            .execute()
        })
      } catch {
        case e: Exception => {
          play.Logger.error("Exception in add", e)
          false
        }
      }
    }

    override def update(invitee: Invitee): Boolean = {
      try {
        DB.withConnection("kk")(implicit conn => {
          SQL("update invitee set gid = {gid}, name = {name}, ceremony = {ceremony}, reception = {reception}, tnum = {tnum}, meal = {meal} where id = {id};")
            .on("id" -> invitee.id,
              "gid" -> invitee.gid,
              "name" -> invitee.name,
              "ceremony" -> invitee.ceremony,
              "reception" -> invitee.reception,
              "tnum" -> invitee.tnum,
              "meal" -> invitee.meal)
            .execute()
        })
      } catch {
        case e: Exception => {
          play.Logger.error("Exception in update", e)
          false
        }
      }
    }

    override def get(id: String): Invitee = {
      DB.withConnection("kk")(implicit conn => {
        val sql = SQL("select * from invitee where id = {id}").on("id" -> id.toLong)
        val row = sql().iterator.next

        _build(row)

      })
    }

    override def list(): List[InviteeDisplay] = {
      DB.withConnection("kk")(implicit conn => {
        val sql = SQL("select * from invitee order by tnum asc")
        sql().map { row =>
          _buildDisplay(row)
        } toList
      })
    }

    override def leaders(): List[Leader] = {
      DB.withConnection("kk")(implicit conn => {
        val sql = SQL("select * from invitee where id = gid order by id asc")
        sql().map { row =>
          val id = row[Long]("id")
          val name = row[String]("name")

          new Leader(id, name)
        } toList
      })
    }

    override def total(): Map[String, String] = {
      DB.withConnection("kk")(implicit conn => {
        val sql = SQL("""
            select invitedBy, sum(invitedNum) as t from invitees group by invitedBy
        	union all
        	select 'Total', sum(invitedNum) as t from invitees;
        	""")

        var map: Map[String, String] = Map.empty[String, String]
        sql().foreach { row =>
          map ++= Map(row[String]("invitedBy") -> row[java.math.BigDecimal]("t").intValue.toString)
          //map ++= Map(row[String]("invitedBy") -> row[Int]("t").toString)
        }

        map
      })

    }

    private def _build(row: SqlRow): Invitee = {
      val id = row[Long]("id")
      val gid = row[Long]("gid")
      val name = row[String]("name")
      val ceremony = row[Int]("ceremony")
      val reception = row[Int]("reception")
      val tnum = row[Int]("tnum")
      val meal = row[Int]("meal")

      new Invitee(id, gid, name, ceremony, reception, tnum, meal)
    }

    private def _buildDisplay(row: SqlRow): InviteeDisplay = {
      val id = row[Long]("id")
      val gid = row[Long]("gid")
      val isLeader = id == gid
      val name = row[String]("name")
      val ceremony = rsvp(row[Int]("ceremony"))
      val reception = rsvp(row[Int]("reception"))
      val tnum = row[Int]("tnum")
      val meal = meals(row[Int]("meal"))

      new InviteeDisplay(id, name, isLeader, ceremony, reception, tnum, meal)
    }

  }
}