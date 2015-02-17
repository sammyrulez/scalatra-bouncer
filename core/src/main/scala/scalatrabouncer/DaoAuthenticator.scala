package scalatrabouncer

import javax.servlet.http.HttpServletRequest
import com.github.t3hnar.bcrypt._


trait DaoAuthenticator extends Authenticator{
  
  def dao():UserDao
  
  def authenticateRequest(request:HttpServletRequest):Either[String,Any] = {
    val user = dao.loadUser(request.getParameter("login"))
    checkPassword(user,request.getParameter("password"))
    
    
  }
  
  def checkPassword(user:SaltedUser,psw:String ):Either[String,Any] = {
    val matchingPsw = psw.bcrypt(user.salt)
    matchingPsw match {
      case user.password => Right(user.username)
      case _ => Left("wrong password")
    }
      
    
  }

}