package controllers

import play.api._
import play.api.mvc._

object Health extends Controller {
  
  def index = Action {
    Status(200)
  }
  
}