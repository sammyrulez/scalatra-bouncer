package scalatrabouncer.db

import scala.slick.driver.H2Driver
import scala.slick.jdbc.JdbcBackend._
import scalatrabouncer.SaltedUser

/**
 * Created by sam on 29/04/15.
 */
class Registration(val db:Database) {

  val userDao:UserDaoDb = new UserDaoDb(H2Driver,db)

  def createUser(user:SaltedUser):Option[Int] = {



    val out:Option[Int] = userDao.users ++= Seq(
      (user.username, user.password, user.salt))
  }

}
