package com.veon.moveon.infra.repo.slick

import com.veon.common.repo.slick.SlickRepository
import com.veon.moveon.core.movie.{Movie, MovieRepository}
import scala.concurrent.Future
import slick.jdbc.H2Profile.api._

class MovieSlickRepo(val db: Database) extends SlickRepository[Movie, String, MovieTable]
    with MovieRepository {

  override val query = TableQuery[MovieTable]
  override def filterById(id: String) = query.filter(_.imdbId === id)

  override def update(movie: Movie): Future[Movie] = db.run {
    query.filter(_.imdbId === movie.imdbId)
      .map(m => (m.title, m.director))
      .update((movie.title, movie.director))
  }.map(_ => movie)
}

class MovieTable(tag: Tag) extends Table[Movie](tag, "movies") {
  def imdbId   = column[String]("imdbId", O.PrimaryKey)
  def title    = column[String]("title")
  def director = column[String]("director")

  val allColumns = (imdbId, title, director)

  def * = allColumns.mapTo[Movie]
}
