package services

import play.api.Play.current
import play.api.db._
import anorm._
import models._

trait InviteeServiceComponent {

  val inviteeService: InviteeService

  trait InviteeService {

    def add(invitee: Invitee): Boolean

    def edit(invitee: Invitee): Boolean

    def get(id: String): Invitee

    def list(): List[InviteeDisplay]

    def leaders(): List[Leader]

    def total(): Total
  }
}

trait InviteeServiceComponentImpl extends InviteeServiceComponent {

  this: InviteeServiceComponent =>

  override val inviteeService = new InviteeService {

    val rsvp : List[String] = List("Not Invited", "Not Replied", "Declined", "Going")
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

    override def edit(invitee: Invitee): Boolean = {
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
        val sql = SQL("select * from invitee order by gid asc, id asc, tnum asc")
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

    override def total(): Total = {
      DB.withConnection("kk")(implicit conn => {
        val totalRow = SQL("select count(*) as t from invitee;").apply().head
        val totalCount = totalRow[Long]("t")

        val csql = SQL("select ceremony, count(ceremony) as s from invitee group by ceremony;")
        var cmap: Map[Int, Long] = Map.empty[Int, Long]
        csql().foreach { row =>
          cmap ++= Map(row[Int]("ceremony") -> row[Long]("s"))
        }
        
        val rsql = SQL("select reception, count(reception) as s from invitee group by reception;")
        var rmap: Map[Int, Long] = Map.empty[Int, Long]
        rsql().foreach { row =>
          rmap ++= Map(row[Int]("reception") -> row[Long]("s"))
        }

        val msql = SQL("select meal, count(meal) as s from invitee group by meal;")
        var mmap: Map[Int, Long] = Map.empty[Int, Long]
        msql().foreach { row =>
          mmap ++= Map(row[Int]("meal") -> row[Long]("s"))
        }
        
        val cTotal = _getMap(cmap, 1) + _getMap(cmap, 2) + _getMap(cmap, 3)
        val rTotal = _getMap(rmap, 1) + _getMap(rmap, 2) + _getMap(rmap, 3)
        
        Total(totalCount, 
            cTotal, _getMap(cmap, 1), _getMap(cmap, 2), _getMap(cmap, 3), 
            rTotal, _getMap(rmap, 1), _getMap(rmap, 2), _getMap(rmap, 3), 
            _getMap(mmap, 0), _getMap(mmap, 1), _getMap(mmap, 2), _getMap(mmap, 3), _getMap(mmap, 4))
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

    private def _getMap(map: Map[Int, Long], index: Int): Long = {
      try {
        map(index)
      } catch {
        case e: Exception => 0L
      }
    }
    
  }
}