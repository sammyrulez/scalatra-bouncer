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
  USERNAME varchar(64) not null,
  ROLE varchar(128) not null
)
      """.execute.apply()

    sql"""
create table ROLES (
  ROLE varchar(128) not null primary key
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
    roles.foreach(role =>
    {
      addRoleToUser(saltedUser, role)
    }
    )

  }

  private def addRoleToUser(saltedUser: SaltedUser, role: String): AnyVal = {
    if (checkRole(role))
      sql"insert into PERMISSIONS (USERNAME,ROLE) values (${saltedUser.username},${role})".update.apply()
  }
  private def removeRoleToUser(saltedUser: SaltedUser, role: String): AnyVal = {
    if (checkRole(role))
      sql"delete from PERMISSIONS  where USERNAME = ${saltedUser.username} and ROLE = ${role}".update.apply()
  }

  def addPermission(saltedUser: SaltedUser,role:String) = {
    if(!userRoles(saltedUser.username).contains(role)){
      addRoleToUser(saltedUser,role)
    }
  }
  def removePermission(saltedUser: SaltedUser,role:String) = {
    if(userRoles(saltedUser.username).contains(role)){
      removeRoleToUser(saltedUser,role)
    }
  }

  def createRole(role:String) = {
    sql"insert into ROLES (ROLE) values (${role})".update.apply()
  }

  def checkRole(role:String):Boolean = {
    sql"select * from ROLES where ROLE = ${role}".map(_.string("ROLE")).single().apply().getOrElse(return false)
    return true
  }




}


  

  



