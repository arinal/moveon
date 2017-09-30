package com.veon.moveon.ui.rest

import scala.io.StdIn

object Boot extends App
  with Injection {

  val binding = restServer.start()
  println("Press enter to stop..")
  StdIn.readLine()
  restServer.stop(binding)
}
