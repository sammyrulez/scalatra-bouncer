package example

import scalatrabouncer.AuthenticationSupport
import scalatrabouncer.Authenticator
import javax.servlet.http.HttpServletRequest
import scalatrabouncer.UserDetailsTrait
import scalatrabouncer.{User,UserWithPassword}


class DummyAuthenticator extends Authenticator{
  
  def authenticateRequest(request:HttpServletRequest):Either[String,Any] = {
     Right("OK")
  }
  
}

class DummyUserDetailsTrait extends UserDetailsTrait {
  
   def loadUser(username:String):Either[String,User] = {
     Right(new UserWithPassword("foo","foo",List[String]("bar")))
   }
  
}

trait DummyAuthenticationSupport extends AuthenticationSupport {
  
  override protected def authenticator = new DummyAuthenticator()
  override protected def userDetails:UserDetailsTrait = new DummyUserDetailsTrait()
  
    
}