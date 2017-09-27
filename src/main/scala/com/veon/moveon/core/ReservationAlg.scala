package com.veon.moveon.core

import scala.concurrent.Future

trait ReservationAlg[Allocation, Movie, Session, Reservation] {
  def initSession(allocation: Allocation, movie: Movie, seats: Int = 0): Future[Session]
  def reserve(session: Session, seats: Int = 1): Future[Reservation]
  def find(allocation: Allocation, movie: Movie): Future[Session]
}

case class Movie(imdbId: String, title: String, author: String)
case class Session(screenId: String, movie: Movie, availableSeats: Int, reservedSeats: Int)

// class ReservationService extends ReservationAlg[String, Movie, Session] {

// }


/**
  * There is an external microservice to schedule allocation of theater rooms at
  * specified times. The main functionality of this operation is to start a movie
  * session by associating that externally generated allocation with a provided movie.
  *
  * @param screenId an id of externally generated theater room allocation
  * @param imdbId IMDb movie id to associate with the session
  * @param seats number of seats, by default is provided by external system. A non-zero
  *     seats will override the provided seat number
  */
