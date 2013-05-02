package services

import play.api.Play.current
import play.api.db._
import anorm._

trait InviteeServiceComponent {

  val inviteeService: InviteeService

  trait InviteeService {

    def add(map: Map[String, String]): Boolean

    def get(id: String): Map[String, String]

    def list(): List[Map[String, String]]

    def update(map: Map[String, String]): Boolean
  }
}

trait InviteeServiceComponentImpl extends InviteeServiceComponent {

  this: InviteeServiceComponent =>

  override val inviteeService = new InviteeService {

    override def add(map: Map[String, String]): Boolean = {
      try {
        DB.withConnection("kk")(implicit conn => {
          SQL("""
	            insert into invitees
	            (englishName, chineseName, relationship, gender, invitedBy, invitedNum, status, goingNum, tableNum, giftItem, giftValue)
	            values ({englishName}, {chineseName}, {relationship}, {gender}, {invitedBy}, {invitedNum}, {status}, {goingNum}, {tableNum}, {giftItem}, {giftValue});
	        	""")
            .on("englishName" -> map.getOrElse("englishName", null),
              "chineseName" -> map.getOrElse("chineseName", null),
              "relationship" -> map.getOrElse("relationship", null),
              "gender" -> map.getOrElse("gender", null),
              "invitedBy" -> map.getOrElse("invitedBy", "both"),
              "invitedNum" -> map.getOrElse("invitedNum", "1"),
              "status" -> map.getOrElse("status", "invited"),
              "goingNum" -> map.getOrElse("goingNum", "0").toInt,
              "tableNum" -> map.getOrElse("tableNum", "0").toInt,
              "giftItem" -> map.getOrElse("giftItem", null),
              "giftValue" -> map.getOrElse("tableNum", "0").toDouble)
            .execute()
        })
      } catch {
        case e: Exception => {
          play.Logger.error("Exception in add", e)
          false
        }
      }
    }

    override def get(id: String): Map[String, String] = {
      DB.withConnection("kk")(implicit conn => {
        val sql = SQL("select * from invitees where id = {id}").on("id" -> id)
        val row = sql().iterator.next

        rowToMap(row)

      })
    }

    override def list(): List[Map[String, String]] = {
      DB.withConnection("kk")(implicit conn => {
        val sql = SQL("select * from invitees")
        sql().map { row =>
          rowToMap(row)
        } toList
      })
    }

    override def update(map: Map[String, String]): Boolean = {
      try {
        DB.withConnection("kk")(implicit conn => {
          SQL("""
	            update invitees set
                englishName = {englishName}, chineseName = {chineseName}, relationship = {relationship}, gender = {gender},
                invitedBy = {invitedBy}, invitedNum = {invitedNum}, status = {status}, goingNum = {goingNum}, tableNum = {tableNum},
                giftItem = {giftItem}, giftValue = {giftValue} where id = {id};
              """)
            .on("englishName" -> map.getOrElse("englishName", null),
              "chineseName" -> map.getOrElse("chineseName", null),
              "relationship" -> map.getOrElse("relationship", null),
              "gender" -> map.getOrElse("gender", null),
              "invitedBy" -> map.getOrElse("invitedBy", "both"),
              "invitedNum" -> map.getOrElse("invitedNum", "1"),
              "status" -> map.getOrElse("status", "invited"),
              "goingNum" -> map.getOrElse("goingNum", "0").toInt,
              "tableNum" -> map.getOrElse("tableNum", "0").toInt,
              "giftItem" -> map.getOrElse("giftItem", null),
              "giftValue" -> map.getOrElse("tableNum", "0").toDouble,
              "id" -> map.getOrElse("id", "0").toInt)
            .execute()
        })
      } catch {
        case e: Exception => {
          play.Logger.error("Exception in update", e)
          false
        }
      }
    }

    private def rowToMap(row: SqlRow): Map[String, String] = {
      Map("id" -> row[Int]("id").toString,
        "englishName" -> row[Option[String]]("englishName").getOrElse(""),
        "chineseName" -> row[Option[String]]("chineseName").getOrElse(""),
        "relationship" -> row[Option[String]]("relationship").getOrElse(""),
        "gender" -> row[Option[String]]("gender").getOrElse(""),
        "invitedBy" -> row[Option[String]]("invitedBy").getOrElse("both"),
        "invitedNum" -> row[Int]("invitedNum").toString,
        "status" -> row[Option[String]]("status").getOrElse(""),
        "goingNum" -> row[Int]("goingNum").toString,
        "tableNum" -> row[Int]("tableNum").toString,
        "giftItem" -> row[Option[String]]("giftItem").getOrElse(""),
        "giftValue" -> row[Double]("giftValue").toString
      )
    }
  }
}