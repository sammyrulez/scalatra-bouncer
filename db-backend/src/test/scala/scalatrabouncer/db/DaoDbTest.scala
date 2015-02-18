package scalatrabouncer.db
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scala.slick.driver.H2Driver
import scala.slick.jdbc.JdbcBackend.Database
import org.specs2.mutable.Before

@RunWith(classOf[JUnitRunner])
class DaoDbTest extends Specification with Before {

  def before() = {

  }

  "A dao db" should {
    " load user from db " in {
      val db = Database.forURL("jdbc:h2:mem:hello;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
      val dao: UserDaoDb = new UserDaoDb(H2Driver, db)
      dao.createDb()
      val adminUsr = dao.loadUser("admin")
      adminUsr.username mustEqual "admin"
    }

  }

}