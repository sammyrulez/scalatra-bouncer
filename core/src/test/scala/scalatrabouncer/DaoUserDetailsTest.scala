package scalatrabouncer

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification

class DummyProfiledUser( username:String,  roles: List[String]) extends SimpleUser(username,roles)  with ProfiledUser[Map[String,String]] {
  
  def profile():Map[String,String] ={
    Map("name" -> "Mark")
  }
}

class DummyProfiledUserDao extends DummyUserDao with ProfiledUserDao[Map[String,String]] {
  
   def loadUserDetails(id:String):ProfiledUser[Map[String,String]] = {
      new DummyProfiledUser("mrk",List("dummy"))
   }
}

class DummyDaoUserDetails extends DaoUserDetails[Map[String,String]] {
  def dao():ProfiledUserDao[Map[String,String]] = {
    new DummyProfiledUserDao
  }
}


@RunWith(classOf[JUnitRunner])
class DaoUserDetailsTest extends Specification {

  
   "A  DaoUserDetails" should {
      " load user data" in { 
        val userDetails = new DummyDaoUserDetails
        val user = userDetails.loadUser("dummy")
        user.isRight must beTrue
      }
   }
}