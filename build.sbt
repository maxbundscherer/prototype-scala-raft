name := "prototype-scala-raft"
version := "0.1"
scalaVersion := "2.13.1"

//Akka Actors
val akkaVersion = "2.6.0"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test

//ScalaTest
val scalaTestVersion = "3.0.8"
libraryDependencies += "org.scalactic" %% "scalactic" % scalaTestVersion
libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % "test"

//Logger
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"