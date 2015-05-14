package scalatrabouncer

import org.scalatra.auth.{ScentryConfig, ScentrySupport}
import org.scalatra.{ScalatraBase}
import org.slf4j.LoggerFactory


trait AuthenticationSupport extends ScalatraBase with ScentrySupport[User] {
  self: ScalatraBase =>
  
  def loginUrl = "/login.html"
  
  protected def userDetails:UserDetailsProvider
  protected def authenticator:Authenticator

  protected def fromSession = { case id: String =>  userDetails.loadUser(id).right.asInstanceOf[User] }
  protected def toSession   = { case usr: User => usr.username() }

  protected val scentryConfig = (new ScentryConfig {
    override val login = loginUrl
  }).asInstanceOf[ScentryConfiguration]

  val logger = LoggerFactory.getLogger(getClass)

  protected def requireLogin() = {
    scentry.authenticate()
    if(!isAuthenticated) {
      redirect(scentryConfig.login)
    }
  }
  
  protected def checkRole(role:String) = {
     if(!isAuthenticated) {       
      redirect(scentryConfig.login)
     }
    
     if(!scentry.userOption.get.roles().contains(role)){
       redirect(scentryConfig.login)
     }
     
  }
  
  protected def checkAnyRole(roles:List[String]) = {
     if(!isAuthenticated) {       
      redirect(scentryConfig.login)
     }
     val inters = scentry.userOption.get.roles().intersect(roles)
     if(inters.length == 0){
       redirect(scentryConfig.login)
     }
     
  }
  
  protected def checkAllRoles(roles:List[String]) = {
     if(!isAuthenticated) {       
      redirect(scentryConfig.login)
     }
     val roleSet = collection.SortedSet.empty[String] ++ roles
     val userRoleSet = collection.SortedSet.empty[String] ++ scentry.userOption.get.roles()
     val isSubset = roleSet.subsetOf(userRoleSet)
     if(!isSubset){
       redirect(scentryConfig.login)
     }
     
  }


  override protected def configureScentry = {
    scentry.unauthenticated {
      scentry.strategies("UserPassword").unauthenticated()
    }
  }

  /**
   * TODO: delegate to registar
   *    */
  override protected def registerAuthStrategies = {
    scentry.register("UserPassword", app => new UserPasswordStrategy(app,authenticator,userDetails))
  //  scentry.register("RememberMe", app => new RememberMeStrategy(app))
  }

}