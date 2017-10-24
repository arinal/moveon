package com.lamedh.moveon.core

import scala.concurrent.Future

/**
  * The algebra of reservation domain. Note that `Session`, `Movie`, and `Allocation`,
  * are all implemented as typed parameters, e.g. they don't refer to case classes.
  * Most of the implementation is inside `ReservationService`.
  * @tparam Allocation allocated theater room at specified time.
  * @tparam Movie      movie description, has title, director, etc
  * @tparam Session    movie + allocation, a movie session
  */
trait ReservationAlg[Allocation, Movie, Session] {
  def initSession(allocation: Allocation, movie: Movie, seats: Int): Future[Session]
  def reserve(session: Session, seats: Int = 1): Future[Session]
  def find(allocation: Allocation, movie: Movie): Future[(Session, Movie)]
}
