package scalatrabouncer

trait User {

  def username():String
  def roles(): List[String]
  
}

class UserWithPassword(val username:String,val password:String,val roles: List[String]) extends User{
  
}