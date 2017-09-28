package com.veon.moveon.core

import scala.concurrent.Future

trait ReservationAlg[Allocation, Movie, Session, Reservation] {
  def initSession(allocation: Allocation, movie: Movie, seats: Int): Future[Session]
  def reserve(session: Session, seats: Int = 1): Future[Reservation]
  def find(allocation: Allocation, movie: Movie): Future[Session]
}
