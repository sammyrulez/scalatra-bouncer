package scalatrabouncer
import javax.servlet.http.HttpServletRequest

trait Authenticator {
  
  def authenticateRequest(request:HttpServletRequest):Either[String,Any]

}