package com.veon.moveon.app.rest

import com.veon.moveon.app.rest.injection.Injection

object Boot extends App
  with Injection {

  val binding = restServer.start()

  scala.sys.addShutdownHook {
    restServer.stop(binding)
    println("Server stopped...")
  }
}
