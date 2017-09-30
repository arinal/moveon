package com.veon.moveon.ui.rest

import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.veon.common.core._

import akka.http.scaladsl.server.Directives._

trait ReservationErrorHandler {

  lazy val errorHandler = ExceptionHandler {
    case token@ ErrorToken(message, errorType) =>
      extractUri { uri =>
        val status = deductStatus(token)
        println(s"Request to $uri could not be handled normally.\nError: $message")
        complete(HttpResponse(status, entity = message))
      }
  }

  private def deductStatus(token: ErrorToken) = token.errorType match {
    case NotFoundError => StatusCodes.NotFound
    case InputError    => StatusCodes.BadRequest
    case ProcessError | UnknownError => StatusCodes.InternalServerError
  }
}
