name := "prototype-scala-raft"
version := "0.1"
scalaVersion := "2.13.1"

//Akka Actors
val akkaVersion = "2.6.0"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test

//Logger
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"