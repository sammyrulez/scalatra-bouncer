package scalatrabouncer.db

import scalatrabouncer.UserDao
import scalatrabouncer.SaltedUser
import scalikejdbc._


class DbUser(val username:String,val password:String,val salt:String)

object DbUser extends SQLSyntaxSupport[DbUser] {
  override val tableName = "USERS_DATA"
  def apply(rs: WrappedResultSet) = new DbUser(
    rs.string("USERNAME"), rs.string("PASSWORD_DATA"), rs.string("SALT"))
}

object UserDaoDb {

  def createDb(implicit session:DBSession)={
    sql"""
create table USERS_DATA (
  USERNAME varchar(64) not null primary key,
  PASSWORD_DATA varchar(128),
  SALT varchar(128) not null
)
      """.execute.apply()

    sql"""
create table PERMISSIONS (
  USERNAME varchar(64) not null primary key,
  ROLE varchar(128) not null
)
      """.execute.apply()
}
}

class UserDaoDb()(implicit session:DBSession) extends UserDao {

  def loadUser(id:String):Option[SaltedUser] = {
    val m = DbUser.syntax("m")
    val user: Option[DbUser] = withSQL {
      select.from(DbUser as m).where.eq(m.username, id)
    }.map(rs => DbUser(rs)).single.apply()

   val userData = user.getOrElse(return None)
   return Option(new SaltedUser(userData.username,userData.password,userData.salt) )

  }

  def userRoles(id:String):List[String] = {
    val roles: List[String] = sql"select * from PERMISSIONS where USERNAME = ${id}".map(_.string("ROLE")).list.apply()
    return roles
  }

  def createUser(saltedUser: SaltedUser, roles:List[String]) = {
    sql"insert into USERS_DATA (USERNAME, PASSWORD_DATA,SALT) values (${saltedUser.username}, ${saltedUser.password},${saltedUser.salt})".update.apply()
    //TODO check that the role exeists
    roles.foreach(role =>
    {
      sql"insert into PERMISSIONS (USERNAME,ROLE) values (${saltedUser.username},${role})".update.apply()
    }
    )

  }



}


  

  



