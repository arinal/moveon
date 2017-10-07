import sbt._

object Dependencies {

  val circeVersion = "0.8.0"

  lazy val dependencies = Seq(
    "com.softwaremill.macwire" %% "macros"          % "2.3.0"       % "provided",
    "io.circe"                 %% "circe-core"      % circeVersion,
    "io.circe"                 %% "circe-generic"   % circeVersion,
    "io.circe"                 %% "circe-parser"    % circeVersion,
    "ch.qos.logback"           %  "logback-classic" % "1.1.2",

    "com.typesafe.slick" %% "slick"                % "3.2.1",
    "com.typesafe.slick" %% "slick-hikaricp"       % "3.2.1",
    "mysql"              %  "mysql-connector-java" % "5.1.34",
    "com.typesafe.akka"  %% "akka-http"            % "10.0.10",

    "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
    "com.h2database"    %  "h2"                % "1.4.185",
    // "com.h2database"    %  "h2"                % "1.4.185" % Test,
    "org.scalatest"     %% "scalatest"         % "3.0.1"   % Test
  )
}
