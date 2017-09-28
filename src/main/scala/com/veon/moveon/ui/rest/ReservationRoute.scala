package com.veon.moveon.ui.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.stream.Materializer
import com.veon.common.core._
import com.veon.moveon.core.reservation.ReservationService
import com.veon.moveon.ui.rest.Models.{MovieSession, RegisterMovie, ReserveSeat}

import scala.concurrent.{ExecutionContext, Future}

class ReservationRoute(service: ReservationService)
                      (implicit system: ActorSystem, _mat: Materializer, _ec: ExecutionContext) {

  import com.veon.common.akkahttp.CirceCodec._
  import io.circe.generic.auto._

  lazy val route: Route =
    handleExceptions(errorHandler) {
      pathPrefix("movies") {
        post {
          (path("register") & entity(as[RegisterMovie])) { register =>
            complete {
              service.startSession(register.screenId, register.imdbId, register.availableSeats)
                .map(Models.fromSession)
            }
          } ~
            (path("reserve") & entity(as[ReserveSeat])) { reserve =>
              complete {
                service.reserveSession(reserve.screenId, reserve.imdbId)
                  .map(Models.fromSession)
              }
            }
        }
      } ~
        (get & parameter('screenId) & parameter('imdbId)) { (screenId, imdbId) =>
          complete {
            service.find(screenId, imdbId).map(Models.fromSession)
          }
        }
    }

  private lazy val errorHandler = ExceptionHandler {
    case ErrorToken(message, errorType) =>
      extractUri { uri =>
        val status = deductStatus(errorType)
        println(s"Request to $uri could not be handled normally.\nError is: $message")
        complete(HttpResponse(status, entity = message))
      }
  }

  private def deductStatus(errorType: ErrorType) = errorType match {
    case NotFoundError => StatusCodes.NotFound
    case InputError    => StatusCodes.BadRequest
    case ProcessError | UnknownError => StatusCodes.InternalServerError
  }
}
