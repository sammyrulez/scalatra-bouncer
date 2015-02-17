package scalatrabouncer

class SaltedUser(val username:String,val password:String,val salt:String)

trait UserDao {
  
  def loadUser(id:String):SaltedUser

}