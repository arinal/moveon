package com.veon.moveon.core.session

import com.veon.common.core.{Entity, ErrorToken}
import com.veon.moveon.core.movie.Movie

case class Session private (
  allocationId : String,
  movie        : Movie,
  initialSeats : Int,
  reservedSeats: Int) extends Entity[String] {

  def availableSeats: Int = initialSeats - reservedSeats
  override val id: String = allocationId
}

object Session {

  def validate(session: Session): Either[ErrorToken, Session] = {
    import session._
    import ErrorToken._
    if      (initialSeats   < 0) left("initialSeats must not be negative")
    else if (reservedSeats  < 0) left("reservedSeats must not be negative")
    else if (availableSeats < 0) left("availableSeats must not be negative")
    else Right(session)
  }

  def make(allocationId: String, movie: Movie, initialSeats: Int): Either[ErrorToken, Session] = {
    val newSession = Session(allocationId, movie, initialSeats, 0)
    validate(newSession)
  }

  def reserve(session: Session, seats: Int): Either[ErrorToken, Session] = {
    val reservedSess = session.copy(reservedSeats = session.reservedSeats + seats)
    validate(reservedSess)
  }
}
