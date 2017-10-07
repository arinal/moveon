package com.veon.moveon.infra.repo.slick

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException
import com.veon.moveon.core.movie.{Movie, MovieRepository}
import org.h2.jdbc.JdbcSQLException
import scala.concurrent.Future

trait MovieSlickRepo extends MovieRepository
    with SlickProfile
    with Queries {

  import profile.api._

  override def count = db.run(movieQuery.countAct)
  override def all   = db.run(movieQuery.allAct)

  def mkTable() = db.run(movieQuery.schema.create)

  override def find(imdbId: String) =
    db.run(movieQuery.findAct(imdbId))
      .map(Option.apply)
      .recover {
        case _: NoSuchElementException => None
      }

  override def store(movie: Movie) =
    db.run(movieQuery += movie)
      .map(_ => movie)
      .recoverWith {
        case ex: JdbcSQLException
            if ex.getMessage.contains("Unique index or primary key") =>
          Future.failed(alreadyExistsError(movie))
      }

  override def update(movie: Movie) = db.run {
    movieQuery.updateAct(movie)
  }.map(_ => movie)
}
