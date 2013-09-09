package services

import play.api.Play.current
import play.api.db._
import anorm._
import models._
import org.joda.time.DateTime

trait GuestbookServiceComponent {

  val guestbookService: GuestbookService

  trait GuestbookService {

    def add(guestbook: Guestbook): Boolean

    def list(): List[GuestbookDisplay]

  }
}

trait GuestbookServiceComponentImpl extends GuestbookServiceComponent {

  this: GuestbookServiceComponent =>

  override val guestbookService = new GuestbookService {

    override def add(guestbook: Guestbook): Boolean = {
      try {
        DB.withConnection("kk")(implicit conn => {
          SQL("""
	            insert into guestbook
	            (id, name, message)
	            values ({id}, {name}, {message});
	        	""")
            .on("id" -> guestbook.id,
              "name" -> guestbook.name,
              "message" -> guestbook.message)
            .execute()
        })
      } catch {
        case e: Exception => {
          play.Logger.error("Exception in add", e)
          false
        }
      }
    }

    override def list(): List[GuestbookDisplay] = {
      DB.withConnection("kk")(implicit conn => {
        val sql = SQL("select * from guestbook order by id desc")
        sql().map { row =>
	      val id = row[Long]("id")
	      val dt = (new DateTime(id)).toString("MMM d, yyyy")
	      val name = row[String]("name")
	      val message = row[String]("message")
	      
	      new GuestbookDisplay(name, message, dt)
        } toList
      })
    }    
  }
}