package com.veon.moveon.infra.repo.slick

import com.veon.moveon.core.session.{ Session => MovieSession }
import com.veon.moveon.core.movie.{ Movie }

trait Queries extends SlickProfile {

  import profile.api._

  val sessionQuery = SessionQuery
  val movieQuery = MovieQuery

  class SessionTable(tag: Tag) extends Table[MovieSession](tag, "Sessions") {
    def screenId      = column[String]("screen_id", O.PrimaryKey)
    def imdbId        = column[String]("imdb_id")
    def initialSeats  = column[Int]("initial_seats")
    def reservedSeats = column[Int]("reserved_seats")

    def * = (screenId, imdbId, initialSeats, reservedSeats).mapTo[MovieSession]
  }

  object SessionQuery extends TableQuery(new SessionTable(_)) {

    def countAct = size.result
    def allAct   = this.result
    def findAct(screenId: String) = find(screenId).result.head
    def updateAct(session: MovieSession) = find(session.allocationId)
      .map(m => (m.imdbId, m.initialSeats, m.reservedSeats))
      .update((session.imdbId, session.initialSeats, session.reservedSeats))

    def find(screenId: String)    = filter(_.screenId === screenId)
  }

  class MovieTable(tag: Tag) extends Table[Movie](tag, "Movies") {
    def imdbId   = column[String]("imdb_id", O.PrimaryKey)
    def title    = column[String]("title")
    def director = column[String]("director")

    val allColumns = (imdbId, title, director)

    def * = allColumns.mapTo[Movie]
  }

  object MovieQuery extends TableQuery(new MovieTable(_)) {

    def countAct = size.result
    def allAct   = this.result

    def find(imdbId: String)    = filter(_.imdbId === imdbId)
    def findAct(imdbId: String) = filter(_.imdbId === imdbId).result.head
    def updateAct(movie: Movie) = find(movie.imdbId)
      .map(m => (m.title, m.director))
      .update((movie.title, movie.director))
  }
}
