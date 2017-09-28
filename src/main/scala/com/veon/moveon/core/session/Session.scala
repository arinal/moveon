package com.veon.moveon.core.session

import com.veon.common.core.{Entity, Error}
import com.veon.moveon.core.movie.Movie

case class Session private (
  screenId: String,
  movie: Movie,
  initialSeats: Int,
  reservedSeats: Int) extends Entity[String] {

  def availableSeats: Int = initialSeats - reservedSeats
  override val id: String = screenId
}

object Session {

  def validate(session: Session): Either[Error, Session] = {
    import session._
    import Error._
    if      (initialSeats   < 0) left("initialSeats must not be negative")
    else if (reservedSeats  < 0) left("reservedSeats must not be negative")
    else if (availableSeats < 0) left("availableSeats must not be negative")
    else Right(session)
  }

  def make(screenId: String, movie: Movie, initialSeats: Int): Either[Error, Session] = {
    val newSession = Session(screenId, movie, initialSeats, 0)
    validate(newSession)
  }

  def reserve(session: Session, seats: Int): Either[Error, Session] = {
    val reservedSess = session.copy(reservedSeats = session.reservedSeats + seats)
    validate(reservedSess)
  }
}
