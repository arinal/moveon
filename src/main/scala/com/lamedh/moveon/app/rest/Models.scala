package com.lamedh.moveon.app.rest

import com.lamedh.moveon.core.movie.Movie
import com.lamedh.moveon.core.session.Session

object Models {

  case class RegisterMovie(imdbId: String, availableSeats: Int, screenId: String)
  case class ReserveSeat(imdbId: String, screenId: String)
  case class MovieSession(imdbId: String, screenId: String, movieTitle: String, availableSeats: Int, reservedSeats: Int)

  def fromSession(sessionMovie: (Session, Movie)) = {
    val (session, movie) = sessionMovie
    MovieSession(session.imdbId,
                 session.allocationId,
                 movie.title,
                 session.availableSeats,
                 session.reservedSeats)
  }
}
