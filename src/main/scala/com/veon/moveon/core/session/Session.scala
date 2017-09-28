package com.veon.moveon.core.session

import com.veon.common.core.{Entity, ErrorToken, InputError}
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
    if      (initialSeats   < 0) left("initial seats must not be negative", InputError)
    else if (reservedSeats  < 0) left("reserved seats must not be negative", InputError)
    else if (availableSeats < 0) left("available seats must not be negative", InputError)
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
