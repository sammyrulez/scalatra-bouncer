package scalatrabouncer

import org.scalatra.ScalatraBase
import org.scalatra.auth.ScentryStrategy
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.slf4j.LoggerFactory

class UserPasswordStrategy(protected val app: ScalatraBase,protected val authenticator: Authenticator,protected val userDetails:UserDetailsTrait)(implicit request: HttpServletRequest, response: HttpServletResponse) extends ScentryStrategy[User] {

  val logger = LoggerFactory.getLogger(getClass)

  override def name: String = "UserPassword"

  private def login = app.params.getOrElse("login", "")
  private def password = app.params.getOrElse("password", "")


  /***
    * Determine whether the strategy should be run for the current request.
    */
  override def isValid(implicit request: HttpServletRequest) = {
    logger.info("UserPasswordStrategy: " + login + " " + password )
    logger.info("UserPasswordStrategy: determining isValid: " + (login != "" && password != "").toString())
    login != "" && password != ""
  }

  
  def authenticate()(implicit request: HttpServletRequest, response: HttpServletResponse): Option[User] = {
    logger.info("UserPasswordStrategy: attempting authentication")
    
    authenticator.authenticateRequest(request) match {
      case Right(x) => userDetails.loadUser(x.asInstanceOf[String]) match {
                        case Right(u) => Option(u)
                        case Left(e) => None
                      }
      case Left(x) => None
      }
    }
    
  

  /**
   * What should happen if the user is currently not authenticated?
   */
  override def unauthenticated()(implicit request: HttpServletRequest, response: HttpServletResponse) {
    app match {
      case authAup:AuthenticationSupport => app.redirect(authAup.loginUrl)
      case _ =>  app.redirect("/login.html")
    }
   
  }
  
}
