package scalatrabouncer.db
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scalikejdbc.{ConnectionPool, AutoSession}

import org.specs2.mutable.Before

import scalatrabouncer.SaltedUser

@RunWith(classOf[JUnitRunner])
class DaoDbTest extends Specification with Before {

  implicit val session = AutoSession
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")

  def before() = {


  }

  "A dao db" should {
    " load user from db " in {
      UserDaoDb.createDb(session)

      val dao = new UserDaoDb()

      dao.createUser(new SaltedUser("admin","a","b"))
      val user = dao.loadUser("admin")
      val testUser  = user.getOrElse(new SaltedUser("","",""))
      testUser.username mustEqual "admin"


    }
   /* " load roles from db " in {

    }*/


  }

}