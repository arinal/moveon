package com.veon.moveon.infra.repo.slick

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import com.veon.moveon.core.session.{ Session => MovieSession, SessionRepository }
import org.h2.jdbc.JdbcSQLException
import scala.concurrent.Future
import com.veon.common.repo.slick.SlickProfile

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

  override def insert(session: MovieSession) =
    db.run(sessionQuery += session)
      .map(_ => session)
      .recoverWith {
        case ex: JdbcSQLException
            if ex.getMessage.contains("Unique index or primary key") =>
          Future.failed(alreadyExistsError(session))
        case _: MySQLIntegrityConstraintViolationException =>
          Future.failed(alreadyExistsError(session))
      }

  override def update(session: MovieSession) = db.run {
    sessionQuery.updateAct(session)
  }.map(_ => session)
}
