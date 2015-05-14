package scalatrabouncer

trait UserDetailsProvider {
  
  def loadUser(username:String):Either[String,User]

}