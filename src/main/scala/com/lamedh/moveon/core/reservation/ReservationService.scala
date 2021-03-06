package com.lamedh.moveon.core.reservation

import com.lamedh.common.core.{ErrorToken, NotFoundError}

import scala.concurrent.{ExecutionContext, Future}
import com.lamedh.moveon.core.ReservationAlg
import com.lamedh.moveon.core.movie.{Movie, MovieRepository}
import com.lamedh.moveon.core.session.{Session, SessionRepository}

class ReservationService(sessionRepo: SessionRepository,
                         movieRepo: MovieRepository,
                         allocFinder: AllocationFinder)
                        (implicit val _ec: ExecutionContext)
    extends ReservationAlg[String, Movie, Session] {

  import com.lamedh.common.core.Syntax._

  /**
   * There is an external microservice to schedule allocation of theater rooms at
   * specified times. The main functionality of this operation is to start a movie
   * session by associating that externally generated allocation with a provided movie.
   * side-effect storing the created session
   * @param allocationId an id of externally generated theater room allocation
   * @param imdbId IMDb movie id to associate with the session
   * @param seats number of seats (tickets sold), by default is provided by external system.
   *           A non-zero seats will override the provided seat number
   * @return the new session
   */
  def startSession(allocationId: String, imdbId: String, seats: Int = 0): Future[Session] = for {
    maybeMov <- movieRepo.find(imdbId)
    mov      <- maybeMov.toFuture(ifFail = movieNotFound(imdbId))
    sess     <- initSession(allocationId, mov, seats)
  } yield sess

  /**
   * Reserve seats for a session found by provided allocationId and imbdIb
   * side-effect updating the reserved session
   * @param allocationId an id of externally generated theater room allocation
   * @param imdbId IMDb movie
   * @param seats number of seats to reserve
   * @return the reserved session
   */
  def reserveSession(allocationId: String, imdbId: String, seats: Int = 1): Future[Session] = for {
    maybeSess <- sessionRepo.find(allocationId)
    sess      <- maybeSess.toFuture(ifFail = sessionNotFound(allocationId))
    if sess.imdbId == imdbId
    reservedSess <- reserve(sess, seats)
  } yield reservedSess

  /**
   * Find the session. This function expects a consistency between allocation and movie.
   * @param allocationId an id of externally generated theater room allocation
   * @param imdbId IMDb movie
   * @return the found session
   */
  def find(allocationId: String, imdbId: String): Future[(Session, Movie)] = {
    val sessFut = sessionRepo.find(allocationId)
    val movFut  = movieRepo.find(imdbId)
    for {
      maybeSess <- sessFut
      session   <- maybeSess.toFuture(ifFail = sessionNotFound(allocationId))
      maybeMov  <- movFut
      mov       <- maybeMov.toFuture(ifFail = movieNotFound(imdbId))
      if session.imdbId == mov.imdbId
    } yield (session, mov)
  }

  override def initSession(allocationId: String, movie: Movie, seats: Int = 0): Future[Session] = for {
    (id, count)  <- allocFinder.find(allocationId)
    initialSeats =  if (seats == 0) count else seats
    session      <- Session.make(id, movie.imdbId, initialSeats).toFuture
    storedSess   <- sessionRepo.insert(session)
  } yield storedSess

  override def reserve(session: Session, seats: Int = 1): Future[Session] = for {
    sess    <- Session.reserve(session, seats).toFuture
    updated <- sessionRepo.update(sess)
  } yield updated

  override def find(allocationId: String, movie: Movie): Future[(Session, Movie)] =
    find(allocationId, movie.imdbId)

  private def sessionNotFound(id: String) =
    ErrorToken(s"Session $id not found", NotFoundError)

  private def movieNotFound(id: String) =
    ErrorToken(s"Movie $id not found", NotFoundError)
}
