package scalatrabouncer.db

import scalatrabouncer.UserDao
import scalatrabouncer.SaltedUser
import scala.slick.driver.JdbcProfile
import scala.slick.jdbc.JdbcBackend.Database
import scala.slick.lifted.{ ProvenShape, ForeignKeyQuery }

class UserDaoDb(val driver: JdbcProfile, val db: Database) extends UserDao {

  import driver.simple._

  class Users(tag: Tag)
    extends Table[(String, String, String)](tag, "USERS") {
    def username: Column[String] = column[String]("USER_NAME", O.PrimaryKey)
    def password: Column[String] = column[String]("PASSWORD_DATA")
    def salt: Column[String] = column[String]("SALT")
    def * : ProvenShape[(String, String, String)] =
      (username, password, salt)

  }

  class Roles(tag: Tag)
    extends Table[(String, String)](tag, "ROLES") {
    def name: Column[String] = column[String]("ROLE_NAME", O.PrimaryKey)
    def description: Column[String] = column[String]("ROLE_DESCR")

    def * : ProvenShape[(String, String)] =
      (name, description)
  }

  class Permissions(tag: Tag)
    extends Table[(String, String)](tag, "PERMISSIONS") {
    def roleName: Column[String] = column[String]("ROLE_NAME")
    def username: Column[String] = column[String]("USER_NAME")
    def roleFK = foreignKey("role_fk", roleName, TableQuery[Roles])(a => a.name)
    def userFK = foreignKey("user_fk", username, TableQuery[Users])(a => a.username)
    def * : ProvenShape[(String, String)] =
      (roleName, username)
  }

  val users: TableQuery[Users] = TableQuery[Users]
  val permissions: TableQuery[Permissions] = TableQuery[Permissions]
  val roles: TableQuery[Roles] = TableQuery[Roles]

  def loadUser(id: String) = {
    db withSession { implicit session =>
      loadUserFromDb(id)
    }
  }
  def userRoles(id: String): List[String] = {
    db withSession { implicit session =>
      userRolesFromDb(id)
    }
  }

  def loadUserFromDb(id: String)(implicit session: Session): SaltedUser = {

    val filterQuery: Query[Users, (String, String, String), Seq] = users.filter(_.username === id)
    val record = filterQuery.list.head
    new SaltedUser(record._1, record._2, record._3)

  }

  def userRolesFromDb(id: String)(implicit session: Session): List[String] = {

    val filterQuery: Query[Permissions, (String, String), Seq] = permissions.filter(_.username === id)
    filterQuery.list.map(_._1)

  }

  def createDb() = {
    db withSession { implicit session =>
      (users.ddl ++ permissions.ddl ++ roles.ddl).create
      val user = SaltedUser.apply("admin", "admin")
      val usersInsertResult: Option[Int] = users ++= Seq(
        (user.username, user.password, user.salt))
      val rolesInsertResult: Option[Int] = roles ++= Seq(
        ("USER_ADMIN", "Admin for users"))
      val permissionsInsertResult: Option[Int] = permissions ++= Seq(
        ("USER_ADMIN", user.username))
    }

  }

}


  

  



