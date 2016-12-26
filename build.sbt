name := """akkademy-db"""

version := "1.0"

scalaVersion := "2.11.7"


// Change this to another test framework if you prefer
libraryDependencies ++= {
  val AkkaVersion = "2.3.11"
  Seq(

    "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % "test",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
}
