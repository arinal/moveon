package com.veon.moveon.app.rest.injection

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.softwaremill.macwire.wire
import com.veon.common.akkahttp.RestServer
import com.veon.moveon.app.rest.ReservationRoute
import com.veon.moveon.core.reservation.ReservationService
import com.veon.moveon.infra.allocationfinder.AkkaHttpAllocationFinder

trait Injection {

  implicit val system = ActorSystem("moveon-rest")
  implicit val _ec = system.dispatcher
  implicit val _mat = ActorMaterializer()

  lazy val repoModule =
    new MySqlRepoModule
    // new H2RepoModule
    // new InMemoryRepoModule
  import repoModule._

  // lazy val allocFinder = wire[DummyAllocationFinder]
  lazy val allocFinder = new AkkaHttpAllocationFinder(Config.allocationHost, Config.allocationPort)
  lazy val service     = wire[ReservationService]

  lazy val resvRoute: ReservationRoute = wire[ReservationRoute]
  lazy val route = resvRoute.route
  lazy val restServer = wire[RestServer]
}
