package scalatrabouncer

trait DaoUserDetails extends UserDetailsTrait{
  
  
   def dao():UserDao
  
   def loadUser(username:String):Either[String,User] = {
      Right(new SimpleUser(dao().loadUser(username).username,dao().userRoles(username))) //TODO
   }

}