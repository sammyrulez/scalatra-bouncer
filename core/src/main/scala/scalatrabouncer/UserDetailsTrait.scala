package scalatrabouncer

trait UserDetailsTrait {
  
  def loadUser(username:String):Either[String,User]

}