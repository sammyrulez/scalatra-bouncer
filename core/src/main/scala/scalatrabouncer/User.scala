package scalatrabouncer

trait User {

  def username():String
  def roles(): List[String]
  
}

trait ProfiledUser[A] extends User {
  
  def profile():A
  
}

class SimpleUser(val username:String,val roles: List[String]) extends User{
  
}