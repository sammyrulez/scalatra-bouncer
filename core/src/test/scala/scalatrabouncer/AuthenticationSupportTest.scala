package scalatrabouncer

import org.scalatra.test.specs2._
import scalatrabouncer.mocks.DummyServlet
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AuthenticationSupportTest extends  MutableScalatraSpec {

   addServlet(classOf[DummyServlet], "/*")

  "GET / on an Authenticated controller" should {
    "return status 302 (redirect) for unauthenticated requests" in {
      get("/") {
        status must_== 302
      }
    }
     "return status 302 (redirect) for missing credential requests" in {
      get("/?login=foo") {
        status must_== 302
      }
       get("/?password=foo") {
        status must_== 302
      }
    }
    "return status 200 (redirect) for requests with credentials" in {
      get("/?login=foo&password=foo") {
        status must_== 200
      }
    }
    
  }
  "GET /admins on an Authenticated controller" should {
    "return status 302 (redirect) for requests with insufficient privileges" in {
      get("/admins?login=foo&password=foo") {
        status must_== 302
      }
      get("/at-least-admins?login=foo&password=foo") {
        status must_== 302
      }
      get("/only-admins?login=foo&password=foo") {
        status must_== 302
      }
    }
  }
  
}