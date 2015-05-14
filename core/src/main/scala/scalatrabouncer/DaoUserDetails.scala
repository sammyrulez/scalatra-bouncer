package scalatrabouncer

trait DaoUserDetails[A] extends UserDetailsProvider{
  
  
   def dao():ProfiledUserDao[A]
  
   def loadUser(username:String):Either[String,ProfiledUser[A]] = {
     try{
       val pUser = dao().loadUserDetails(username)
       Right(pUser)
     }catch{
       case e: Exception =>  Left(e.getMessage)
       case _: Throwable  => Left("Catastrophic Error loading user details")
     }
     
     
     
   }

}