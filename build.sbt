import Dependencies._

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Seq(
      organization := "com.lamedh",
      scalaVersion := "2.12.3",
      version      := "0.1.0",
      name := "moveon",
      ),
    Defaults.itSettings,
    libraryDependencies ++= Dependencies.dependencies
  )

enablePlugins(DockerPlugin)

dockerfile in docker := {
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("java")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}

imageNames in docker := Seq(
  ImageName(s"lamedh/${name.value}:latest"),
  ImageName(
    namespace = Some("lamedh"),
    repository = name.value,
    tag = Some("v" + version.value)
  )
)

buildOptions in docker := BuildOptions(cache = false)
