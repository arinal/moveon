package com.veon.moveon.ui.rest

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.softwaremill.macwire.wire
import com.veon.common.akkahttp.RestServer
import com.veon.moveon.core.reservation.ReservationService
import com.veon.moveon.infra.allocationfinder.DummyAllocationFinder
import com.veon.moveon.infra.repo.slick.MovieSlickRepo
import com.veon.moveon.infra.repo.slick.SessionSlickRepo
import com.veon.moveon.infra.repo.inmemory._
import slick.jdbc.H2Profile.api._

trait Injection {

  implicit val system = ActorSystem("moveon-rest")
  implicit val _mat = ActorMaterializer()
  implicit val _ec = system.dispatcher

  lazy val db = Database.forConfig("damn")

  // lazy val sessionRepo = wire[SessionInMemoryRepo]
  // lazy val movieRepo   = wire[MovieInMemoryRepo]
  lazy val sessionRepo = wire[SessionSlickRepo]
  lazy val movieRepo   = wire[MovieSlickRepo]
  movieRepo.mkTable()
  sessionRepo.mkTable()

  lazy val allocFinder = wire[DummyAllocationFinder]
  lazy val service     = wire[ReservationService]

  lazy val resvRoute: ReservationRoute = wire[ReservationRoute]
  lazy val route = resvRoute.route
  lazy val restServer = wire[RestServer]
}
