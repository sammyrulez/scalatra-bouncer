package scalatrabouncer

trait User {

  def username():String
  
}

class UserWithPassword(val username:String,val password:String) extends User{
  
}