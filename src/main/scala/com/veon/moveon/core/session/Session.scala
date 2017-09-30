package com.veon.moveon.core.session

import com.veon.common.core.{Entity, ErrorToken, InputError}

case class Session (
  allocationId : String,
  imdbId       : String,
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

  def make(allocationId: String, movieId: String, initialSeats: Int): Either[ErrorToken, Session] = {
    val newSession = Session(allocationId, movieId, initialSeats, 0)
    validate(newSession)
  }

  def reserve(session: Session, seats: Int): Either[ErrorToken, Session] = {
    val reservedSess = session.copy(reservedSeats = session.reservedSeats + seats)
    validate(reservedSess)
  }

  def tupled(tuple: (String, String, Int, Int)) = {
    val (id, movId, max, res) = tuple
    Session(id, movId, max, res)
  }
}
