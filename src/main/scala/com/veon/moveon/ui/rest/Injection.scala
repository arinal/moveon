package com.veon.moveon.ui.rest

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.softwaremill.macwire.wire
import com.veon.common.codec.RestServer
import com.veon.moveon.core.reservation.ReservationService
import com.veon.moveon.infra.allocationfinder.DummyAllocationFinder
import com.veon.moveon.infra.repo.inmemory.{MovieInMemoryRepo, SessionInMemoryRepo}

trait Injection {

  implicit val system = ActorSystem("moveon-rest")
  implicit val _mat = ActorMaterializer()
  implicit val _ec = system.dispatcher

  lazy val sessionRepo = wire[SessionInMemoryRepo]
  lazy val movieRepo   = wire[MovieInMemoryRepo]
  lazy val allocFinder = wire[DummyAllocationFinder]
  lazy val service     = wire[ReservationService]

  lazy val resvRoute: ReservationRoute = wire[ReservationRoute]
  lazy val restServer = new RestServer(resvRoute.route)
}
