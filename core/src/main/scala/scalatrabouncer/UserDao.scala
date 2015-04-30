package scalatrabouncer

import com.github.t3hnar.bcrypt._
object SaltedUser {
  
  def apply( username:String, password:String) = {
    val salt = generateSalt
    val encryptedPsw:String = password.bcrypt(salt)
    new SaltedUser(username,encryptedPsw,salt)
  }
}

class SaltedUser(val username:String,val password:String,val salt:String)

trait UserDao {
  
  def loadUser(id:String):Option[SaltedUser]
  
  def userRoles(id:String):List[String]  

}

trait ProfiledUserDao[A] extends UserDao {
  
  def loadUserDetails(id:String):ProfiledUser[A]
}