package com.veon.moveon.infra.repo.slick

import com.veon.moveon.core.session.{ Session => MovieSession, SessionRepository }

trait SessionSlickRepo extends SessionRepository
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

  override def store(session: MovieSession) = db.run {
    Query += session
  }.map(_ => session)

  override def update(session: MovieSession) = db.run {
    Query.updateAct(session)
  }.map(_ => session)

  class SessionTable(tag: Tag) extends Table[MovieSession](tag, "Sessions") {
    def screenId      = column[String]("screen_id", O.PrimaryKey)
    def imdbId        = column[String]("imdb_id")
    def initialSeats  = column[Int]("initial_seats")
    def reservedSeats = column[Int]("reserved_seats")

    def * = (screenId, imdbId, initialSeats, reservedSeats).mapTo[MovieSession]
  }

  object Query extends TableQuery(new SessionTable(_)) {

    def countAct = size.result
    def allAct   = this.result

    def find(screenId: String)    = filter(_.screenId === screenId)
    def findAct(screenId: String) = find(screenId).result.head
    def updateAct(session: MovieSession) = find(session.imdbId)
      .map(m => (m.imdbId,m.initialSeats, m.reservedSeats))
      .update((session.imdbId, session.initialSeats, session.reservedSeats))
  }
}
