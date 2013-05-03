import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "kake"
  val appVersion      = "0.1"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "mysql" % "mysql-connector-java" % "5.1.24"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
