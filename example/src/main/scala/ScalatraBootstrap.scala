

import org.scalatra._
import javax.servlet.ServletContext
import example.HomeController

class ScalatraBootstrap extends LifeCycle {


  override def init(context: ServletContext) {
    //implicit val bindingModule = ProjectConfiguration
    
    context.mount(new HomeController(), "/api/*")
  }

  override def destroy(context: ServletContext) {
  
  }

}