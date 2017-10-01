package com.veon.moveon.infra.repo.slick

import com.veon.moveon.core.movie.{Movie, MovieRepository}

trait MovieSlickRepo extends MovieRepository
    with SlickProfile {

  import profile.api._

  override def count = db.run(Query.countAct)
  override def all   = db.run(Query.allAct)

  def mkTable() = db.run(Query.schema.create)

  override def find(imdbId: String) =
    db.run(Query.findAct(imdbId))
      .map(Option.apply)
      .recover {
        case _: NoSuchElementException => None
      }

  override def store(movie: Movie) = db.run {
    Query += movie
  }.map(_ => movie)

  override def update(movie: Movie) = db.run {
    Query.updateAct(movie)
  }.map(_ => movie)

  class MovieTable(tag: Tag) extends Table[Movie](tag, "Movies") {
    def imdbId   = column[String]("imdb_id", O.PrimaryKey)
    def title    = column[String]("title")
    def director = column[String]("director")

    val allColumns = (imdbId, title, director)

    def * = allColumns.mapTo[Movie]
  }

  object Query extends TableQuery(new MovieTable(_)) {

    def countAct = size.result
    def allAct   = this.result

    def find(imdbId: String)    = filter(_.imdbId === imdbId)
    def findAct(imdbId: String) = filter(_.imdbId === imdbId).result.head
    def updateAct(movie: Movie) = find(movie.imdbId)
      .map(m => (m.title, m.director))
      .update((movie.title, movie.director))
  }
}
