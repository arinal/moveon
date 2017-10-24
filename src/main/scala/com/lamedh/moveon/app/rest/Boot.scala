package com.lamedh.moveon.app.rest

import com.lamedh.moveon.app.rest.injection.Injection

object Boot extends App
  with Injection {

  val binding = restServer.start()

  scala.sys.addShutdownHook {
    restServer.stop(binding)
    println("Server stopped...")
  }
}
