import Dependencies._

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.3",
      version      := "0.1.0"
    )),
    name := "moveon",
    libraryDependencies ++= Dependencies.dependencies)
  .configs(ItTesting.it: _*)
  .settings(ItTesting.settings: _*)
