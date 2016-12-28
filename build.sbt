name := """akkademy-db"""

version := "1.0"

scalaVersion := "2.11.7"

name := "akkademy-db"
organization := "com.akkademy-db"
version := "0.0.1-SNAPSHOT"


libraryDependencies ++= {
  val AkkaVersion = "2.3.11"
  Seq(

    "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    "com.typesafe.akka" %% "akka-remote" % AkkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % "test",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
}

// todo move messages into a sub project and remove this
mappings in(Compile, packageBin) ~= {
  _.filterNot { case (_, name) => Seq("application.conf").contains(name) }
}
