package scalatrabouncer.db

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scalikejdbc.specs2.AutoRollback
import scalikejdbc.{DBSession, ConnectionPool, AutoSession}
import scalikejdbc._

import scalatrabouncer.{ProfiledUser, SaltedUser}

@RunWith(classOf[JUnitRunner])
class
DaoDbTest extends Specification  {




  sequential

  trait AutoRollbackWithFixture extends AutoRollback {


    override def fixture(implicit session: DBSession) {
     // UserDaoDb.createDb
      val dao = new UserDaoDb()

      dao.createRole("ADMIN")
      dao.createRole("DUMMY")
      dao.createUser(new SaltedUser("admin", "a", "b"), List("ADMIN"))

      ProfiledUserMetaDaoDb.createDb


    }


  }

  "A dao db" should {
    " load user from db " in  new AutoRollbackWithFixture{
      val dao = new UserDaoDb()
      val user = dao.loadUser("admin")
      val testUser = user.getOrElse(new SaltedUser("", "", ""))
      testUser.username mustEqual "admin"


    }
    " load roles from db " in new AutoRollback {
      val dao = new UserDaoDb()
      val roles = dao.userRoles("admin")
      roles.size mustEqual 1
      roles.contains("ADMIN")
    }
    " Add roles to db " in new AutoRollback {
      val dao = new UserDaoDb()
      val user = dao.loadUser("admin")
      dao.addPermission(user.get, "DUMMY")
      val extraRoles = dao.userRoles("admin")
      extraRoles.size mustEqual 2
      dao.removePermission(user.get, "DUMMY")
      val stdRoles = dao.userRoles("admin")
      stdRoles.size mustEqual 1

    }

    " Change password in db" in new AutoRollback{
      val dao = new UserDaoDb()
      val userIn = new SaltedUser("admin", "x", "x")
      dao.changePassword(userIn)
      val user = dao.loadUser("admin")
      val testUser = user.getOrElse(new SaltedUser("", "", ""))
      testUser.password mustEqual "x"

    }


  }

  "A profile dao" should {
    "load metadata from db" in  new AutoRollback{
      val metaDao = new ProfiledUserMetaDaoDb()
      val profile: ProfiledUser[Map[String, String]] = metaDao.loadUserDetails("admin")
      profile.profile().size mustEqual 0

    }

    "persist profile " in new AutoRollback {
      val metaDao = new ProfiledUserMetaDaoDb()
      val profileData = Map("email" -> "none@upsala.it")
      metaDao.persistDetails("admin", profileData)
      val profile: ProfiledUser[Map[String, String]] = metaDao.loadUserDetails("admin")
      profile.profile().size mustEqual 1


    }

  }

}