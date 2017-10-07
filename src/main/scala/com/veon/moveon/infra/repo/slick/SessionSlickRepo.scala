package com.veon.moveon.infra.repo.slick

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import com.veon.moveon.core.session.{ Session => MovieSession, SessionRepository }
import scala.concurrent.Future

trait SessionSlickRepo extends SessionRepository
    with SlickProfile
    with Queries {

  import profile.api._

  override def count = db.run(sessionQuery.countAct)
  override def all   = db.run(sessionQuery.allAct)

  def mkTable() = db.run(sessionQuery.schema.create)

  override def find(imdbId: String) =
    db.run(sessionQuery.findAct(imdbId))
      .map(Option.apply)
      .recover {
        case _: NoSuchElementException => None
      }

  override def store(session: MovieSession) =
    db.run(sessionQuery += session)
      .map(_ => session)
      .recoverWith {
        case _: MySQLIntegrityConstraintViolationException =>
          Future.failed(alreadyExistsError(session))
      }

  override def update(session: MovieSession) = db.run {
    sessionQuery.updateAct(session)
  }.map(_ => session)
}
