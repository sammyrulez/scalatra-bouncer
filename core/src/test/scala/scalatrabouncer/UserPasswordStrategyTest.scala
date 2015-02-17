package scalatrabouncer
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import javax.servlet.http.HttpServletRequest
import org.scalatra.ScalatraBase
import javax.servlet.http.HttpServletResponse
import org.specs2.mock.Mockito
import scala.PartialFunction.OrElse


class DummyAuthenticator extends Authenticator{
  
  def authenticateRequest(request:HttpServletRequest):Either[String,Any] = {
     Right("OK")
  }
  
}

class DummyUserDetailsTrait extends UserDetailsTrait {
  
   def loadUser(username:String):Either[String,User] = {
     Right(new UserWithPassword("foo","foo", List[String]()))
   }
  
}

@RunWith(classOf[JUnitRunner])
class UserPasswordStrategyTest extends Specification with Mockito {
    
  

  "A UserPasswordStrategy" should {
      " authenticate a request" in {
           implicit val request:HttpServletRequest = mock[HttpServletRequest]
           implicit val response: HttpServletResponse = mock[HttpServletResponse]
           val app:ScalatraBase =  mock[ScalatraBase]
           val strategy = new UserPasswordStrategy(app,new DummyAuthenticator,new DummyUserDetailsTrait )
           val user = strategy.authenticate()
           val check:User = user getOrElse new UserWithPassword("","", List[String]())
           check.username() mustEqual "foo"
           
      }
      
  }
  
  
}