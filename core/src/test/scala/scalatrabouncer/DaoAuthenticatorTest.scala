package scalatrabouncer

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import com.github.t3hnar.bcrypt._
import org.specs2.mock.Mockito
import javax.servlet.http.HttpServletRequest

class DummyUserDao extends UserDao {
  def loadUser(id:String):SaltedUser = {
    val salt = generateSalt
    val encryptedPsw:String = "bar".bcrypt(salt)
    new SaltedUser(id, encryptedPsw,salt)
  }
  
  def userRoles(id:String):List[String] = List[String]()
}

class DummyDaoAuthenticator extends DaoAuthenticator {
  
  def dao():UserDao = {
    new DummyUserDao
  }
  
}


@RunWith(classOf[JUnitRunner])
class DaoAuthenticatorTest extends Specification with Mockito{
  
  "A  DaoAuthenticator" should {
      " check for encrypted and salted passwords" in {       
      
        val request:HttpServletRequest = mock[HttpServletRequest]
        request.getParameter("login") returns "foo"
        request.getParameter("password") returns "bar"
        val authenticator = new DummyDaoAuthenticator
        val result = authenticator.authenticateRequest(request)
        result must beRight("foo")        
        
      }
      " reject wrong passwords" in {
        
        val request:HttpServletRequest = mock[HttpServletRequest]
        request.getParameter("login") returns "foo"
        request.getParameter("password") returns "xyz"
        val authenticator = new DummyDaoAuthenticator
        val result = authenticator.authenticateRequest(request)
        result must beLeft("wrong password")        
      }
      
  }
  
  "A SaltedUser "  should {
    "encrypt passwords" in {
    
      val user = SaltedUser.apply("foo", "bar")
      user.password  mustNotEqual "bar"    
    }
  }

}