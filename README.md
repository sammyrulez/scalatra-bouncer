Scalatra Bouncer
================

[![Build Status](https://travis-ci.org/sammyrulez/scalatra-bouncer.svg?branch=master)](https://travis-ci.org/sammyrulez/scalatra-bouncer)
[![Coverage Status](https://coveralls.io/repos/sammyrulez/scalatra-bouncer/badge.svg?branch=master)](https://coveralls.io/r/sammyrulez/scalatra-bouncer?branch=master)


Scalatra Bouncer is a authentication / authorization library for Scalatra

### Installation

Add the dependency to the core library

#### Maven

```
         <dependency>
			<groupId>scalatrabouncer</groupId>
			<artifactId>core</artifactId>
			<version>1.0-SNAPSHOT</version>
		</dependency>
```


#### SBT

TODO

### Usage

In order to have authentication / authorization in place in your scalatra service you have to implement your logic through AuthenticationSupport trait
providing and authenticator (that verifies the request) and a UserDetailsProvider (that loads user data from a storage or service)


```
trait DummyAuthenticationSupport extends AuthenticationSupport {

  override protected def authenticator = new DummyAuthenticator()
  override protected def userDetails:UserDetailsTrait = new DummyUserDetailsProvider()


}

class DummyAuthenticator extends Authenticator{

  def authenticateRequest(request:HttpServletRequest):Either[String,Any] = {
     Right("OK")
  }

}

class DummyUserDetailsProvider extends UserDetailsProvider {

   def loadUser(username:String):Either[String,User] = {
     Right(new SimpleUser("foo",List[String]()))
   }

}

```

#### Dealing with users

TODO


### Batteries included

#### Db based user storage

A Simple jdbc (scalikejdbc) based implementation of used data persistence. To use this module add the artifact to your dependancies

#### Maven

```
         <dependency>
			<groupId>scalatrabouncer</groupId>
			<artifactId>db-backend</artifactId>
			<version>1.0-SNAPSHOT</version>
		</dependency>
```


#### SBT

TODO


In ScalatraBootstap instument scalalikejdbc with a implicit session

```
  implicit val session = AutoSession
  Class.forName("your.driver.class")
  ConnectionPool.singleton("jdbc:your:url", "user", "pass")
```









