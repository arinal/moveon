import sbt._

object Dependencies {

  val circeVersion = "0.8.0"

  lazy val dependencies = Seq(
    "com.typesafe.akka"        %% "akka-http"     % "10.0.10",
    "io.circe"                 %% "circe-core"    % circeVersion,
    "io.circe"                 %% "circe-generic" % circeVersion,
    "io.circe"                 %% "circe-parser"  % circeVersion,
    "com.softwaremill.macwire" %% "macros"        % "2.3.0"       % "provided",

    "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
    "org.scalatest"     %% "scalatest"         % "3.0.1"   % Test
  )
}
