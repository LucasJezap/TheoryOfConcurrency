name := "Lab 13"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"               % "2.6.8",
  "com.typesafe.akka" %% "akka-testkit"             % "2.6.8" % "test",
  "com.typesafe.akka" %% "akka-actor-typed"         % "2.6.8",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.8" % "test",
  "org.scalatest"     %% "scalatest"                % "3.2.0" % "test",
  "ch.qos.logback"    % "logback-classic"           % "1.2.3"
)