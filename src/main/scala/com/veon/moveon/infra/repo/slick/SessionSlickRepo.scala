package com.veon.moveon.infra.repo.slick

import com.veon.common.repo.slick.SlickRepository
import com.veon.moveon.core.session.{Session, SessionRepository}
import slick.jdbc.H2Profile.api._

class SessionSlickRepo(val db: Database) extends SlickRepository[Session, String, SessionTable]
    with SessionRepository {

  override val query = TableQuery[SessionTable]
  override def filterById(id: String) = query.filter(_.screenId === id)
}

class SessionTable(tag: Tag) extends Table[Session](tag, "sessions") {
  def screenId      = column[String]("screenId", O.PrimaryKey)
  def imdbId        = column[String]("imdbId")
  def initialSeats  = column[Int]("initialSeats")
  def reservedSeats = column[Int]("reservedSeats")

  def * = (screenId, imdbId, initialSeats, reservedSeats).mapTo[Session]
}