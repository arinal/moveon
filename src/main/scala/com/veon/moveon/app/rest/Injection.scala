package com.veon.moveon.app.rest

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.softwaremill.macwire.wire
import com.veon.common.akkahttp.RestServer
import com.veon.moveon.core.reservation.ReservationService
import com.veon.moveon.infra.allocationfinder.DummyAllocationFinder
import com.veon.moveon.infra.repo.slick.{MovieSlickRepo, SessionSlickRepo, SlickProfile}
import com.veon.moveon.infra.repo.inmemory._

trait Injection {

  implicit val system = ActorSystem("moveon-rest")
  implicit val _mat = ActorMaterializer()
  implicit val _ec = system.dispatcher

  lazy val sessionRepo = wire[SessionInMemoryRepo]
  lazy val movieRepo   = wire[MovieInMemoryRepo]

  // trait Slick extends SlickProfile {
  //   override lazy val profile = slick.jdbc.MySQLProfile
  //   import slick.jdbc.MySQLProfile.api._
  //   override lazy val db = Database.forConfig("db-mysql-docker")
  // }

  // lazy val sessionRepo = new SessionSlickRepo with Slick
  // lazy val movieRepo   = new MovieSlickRepo with Slick

  lazy val allocFinder = wire[DummyAllocationFinder]
  lazy val service     = wire[ReservationService]

  lazy val resvRoute: ReservationRoute = wire[ReservationRoute]
  lazy val route = resvRoute.route
  lazy val restServer = wire[RestServer]
}
