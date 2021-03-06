package example

import scalatrabouncer.AuthenticationSupport
import scalatrabouncer.Authenticator
import javax.servlet.http.HttpServletRequest
import scalatrabouncer.UserDetailsProvider
import scalatrabouncer.{User,SimpleUser}


class DummyAuthenticator extends Authenticator{
  
  def authenticateRequest(request:HttpServletRequest):Either[String,Any] = {
     Right("OK")
  }
  
}

class DummyUserDetailsProvider extends UserDetailsProvider {
  
   def loadUser(username:String):Either[String,User] = {
     Right(new SimpleUser("foo",List[String]()))
   }
  
}

trait DummyAuthenticationSupport extends AuthenticationSupport {
  
  override protected def authenticator = new DummyAuthenticator()
  override protected def userDetails:UserDetailsProvider = new DummyUserDetailsProvider()
  
    
}