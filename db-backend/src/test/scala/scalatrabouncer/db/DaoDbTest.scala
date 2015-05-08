package scalatrabouncer.db
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scalikejdbc.{ConnectionPool, AutoSession}

import org.specs2.mutable.Before

import scalatrabouncer.{ProfiledUser, SaltedUser}

@RunWith(classOf[JUnitRunner])
class DaoDbTest extends Specification with Before {

  implicit val session = AutoSession
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")
  UserDaoDb.createDb

  val dao = new UserDaoDb()
  dao.createRole("ADMIN")
  dao.createRole("DUMMY")
  dao.createUser(new SaltedUser("admin","a","b"),List("ADMIN"))

  ProfiledUserMetaDaoDb.createDb
  val metaDao = new ProfiledUserMetaDaoDb()



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

    " Change password in db" in {
      val userIn = new SaltedUser("admin","x","x")
      dao.changePassword(userIn)
      val user = dao.loadUser("admin")
      val testUser  = user.getOrElse(new SaltedUser("","",""))
      testUser.password mustEqual "x"

    }


  }

  "A profile dao" should {
    "load metadata from db" in {
      val profile:ProfiledUser[Map[String, String]] = metaDao.loadUserDetails("admin")
      profile.profile().size mustEqual 0



    }

  }

}