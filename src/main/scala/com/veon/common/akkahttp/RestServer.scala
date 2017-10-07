package com.veon.common.akkahttp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import akka.stream.Materializer

import scala.concurrent.{ExecutionContext, Future}

class RestServer(route: Route)
                (implicit system: ActorSystem, _mat: Materializer, _ec: ExecutionContext) {

  def start(port: Int = 8080): Future[ServerBinding] = {
    println(s"Server is started on port $port")
    Http().bindAndHandle(route, "0.0.0.0", port)
  }

  def stop(bindingFut: Future[ServerBinding]): Unit = {
    bindingFut.flatMap(_.unbind()).onComplete { _ =>
      println("Shutting down..")
      system.terminate()
    }
  }
}
