package com.veon.moveon.app.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.veon.moveon.core.reservation.ReservationService
import com.veon.moveon.app.rest.Models.{RegisterMovie, ReserveSeat}

import scala.concurrent.ExecutionContext

class ReservationRoute(service: ReservationService)
                      (implicit system: ActorSystem, _mat: Materializer, _ec: ExecutionContext)
    extends ReservationErrorHandler {

  import com.veon.common.akkahttp.CirceCodec._
  import io.circe.generic.auto._

  lazy val route: Route = pathPrefix("movies") {
    handleExceptions(errorHandler) {
      post {
        (path("register") & entity(as[RegisterMovie])) { req =>
          complete {
            service.startSession(req.screenId, req.imdbId, req.availableSeats)
              .map(_.imdbId)
          }
        } ~
        (path("reserve") & entity(as[ReserveSeat])) { req =>
          complete {
            service.reserveSession(req.screenId, req.imdbId)
              .map(_.imdbId)
          }
        }
      } ~
      get {
        (parameter('screenId) & parameter('imdbId)) { (scrId, imdbId) =>
          complete {
            service.find(scrId, imdbId).map(Models.fromSession)
          }
        }
      }
    }
  }
}
