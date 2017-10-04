import sbt.Keys._
import sbt._

object ItTesting {

  val testIt = TaskKey[Unit]("test-it")

  val itTestConfig = config("it") extend Test

  val it = Seq(itTestConfig)

  val testSettings = Seq(
    fork in Test := false,
    parallelExecution in Test := false
  )

  val itSettings = inConfig(itTestConfig)(Defaults.testSettings) ++ Seq(
    fork in itTestConfig := false,
    parallelExecution in itTestConfig := false,
    scalaSource in itTestConfig := baseDirectory.value / "src/it/scala"
  )

  val settings = testSettings ++ itSettings ++ Seq(
    testIt := (),
    testIt <<= testIt.dependsOn(test in itTestConfig),
    testIt <<= testIt.dependsOn(test in Test)
  )
}
