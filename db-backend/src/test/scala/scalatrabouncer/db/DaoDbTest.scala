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
  UserDaoDb.createDb(session)

  val dao = new UserDaoDb()
  dao.createRole("ADMIN")
  dao.createRole("DUMMY")
  dao.createUser(new SaltedUser("admin","a","b"),List("ADMIN"))


  def before() = {


  }

  "A dao db" should {
    " load user from db " in {

      val user = dao.loadUser("admin")
      val testUser  = user.getOrElse(new SaltedUser("","",""))
      testUser.username mustEqual "admin"


    }
    " load roles from db " in {
        val roles = dao.userRoles("admin")
        roles.size mustEqual 1
        roles.contains("ADMIN")
    }
    " Add roles to db " in {
      val user = dao.loadUser("admin")
      dao.addPermission(user.get,"DUMMY")
      val extraRoles = dao.userRoles("admin")
      extraRoles.size mustEqual 2
      dao.removePermission(user.get,"DUMMY")
      val stdRoles = dao.userRoles("admin")
      stdRoles.size mustEqual 1

    }


  }

}