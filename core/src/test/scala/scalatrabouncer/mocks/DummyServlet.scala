package scalatrabouncer.mocks

import org.scalatra.ScalatraServlet
import example.DummyAuthenticationSupport

class DummyServlet extends ScalatraServlet with DummyAuthenticationSupport {
  
   before() {
    requireLogin()
    checkRole("bar")
    checkAllRoles(List("bar"))
    checkAnyRole(List("bar"))
  }
  
   get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
      </body>
    </html>
  }
   get("/admins") {
    checkRole("admin")
    <html>
      <body>
        <h1>Hello, world!</h1>
      </body>
    </html>
  }
  get("/at-least-admins") {
    checkAnyRole(List("admin"))
    <html>
      <body>
        <h1>Hello, world!</h1>
      </body>
    </html>
  }
  get("/only-admins") {
    checkAllRoles(List("admin"))
    <html>
      <body>
        <h1>Hello, world!</h1>
      </body>
    </html>
  }
  
}