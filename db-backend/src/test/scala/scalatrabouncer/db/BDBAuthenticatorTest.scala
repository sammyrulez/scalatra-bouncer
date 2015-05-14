package scalatrabouncer.db

import javax.servlet.http.HttpServletRequest

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import scalikejdbc.DBSession
import scalikejdbc.config.DBs

import scalikejdbc.specs2.AutoRollback
import scalikejdbc._



@RunWith(classOf[JUnitRunner])
class BDBAuthenticatorTest  extends Specification  with Mockito {




  sequential

  DBs.setupAll()

  trait AutoRollbackWithFixture extends AutoRollback {
    override def fixture(implicit session: DBSession) {
      UserDaoDb.createDb
    }
  }


  "A  DaoAuthenticator" should {
    " load from config" in new AutoRollbackWithFixture {

      val request: HttpServletRequest = mock[HttpServletRequest]
      request.getParameter("login") returns "foo"
      request.getParameter("password") returns "bar"
      val authenticator = new DbAuthenticator()
      val result = authenticator.authenticateRequest(request)
      result must beLeft("User not found")

    }

  }
}
