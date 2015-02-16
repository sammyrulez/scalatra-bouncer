package example

import org.scalatra.ScalatraServlet

class HomeController extends ScalatraServlet with DummyAuthenticationSupport{
  
  before() {
    requireLogin()
  }
  
   get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
      </body>
    </html>
  }

}