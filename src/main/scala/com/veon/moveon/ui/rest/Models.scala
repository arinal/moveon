package com.veon.moveon.ui.rest

import com.veon.moveon.core.session.Session

object Models {

  case class RegisterMovie(imdbId: String, availableSeats: Int, screenId: String)
  case class ReserveSeat(imdbId: String, screenId: String)
  case class MovieSession(imdbId: String, screenId: String, movieTitle: String, availableSeats: Int, reservedSeats: Int)

  def fromSession(session: Session) =
    MovieSession(session.movie.imdbId,
      session.allocationId,
      session.movie.title,
      session.availableSeats,
      session.reservedSeats)
}
