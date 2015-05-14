package scalatrabouncer.db

import scalikejdbc.DBSession

import scalatrabouncer.DaoAuthenticator
import com.typesafe.config.ConfigFactory

class DbAuthenticator()(implicit session:DBSession) extends DaoAuthenticator{

  private val config  = ConfigFactory.load()
  private val daoDb:UserDaoDb = new UserDaoDb()
  override def dao() = {
    daoDb
  }

}
