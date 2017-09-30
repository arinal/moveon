package com.veon.common.repo.slick

import scala.concurrent.Future
import slick.jdbc.H2Profile.api._

trait SlickRepository[E, Id, S <: Table[E]] {

  protected implicit val _ec = db.executor.executionContext

  protected val query: TableQuery[S]

  protected def db: Database
  protected def filterById(id: Id): Query[S, E, Seq]

  def mkTable() = db.run(query.schema.create)

  def count: Future[Int]  = db.run(query.size.result)
  def all: Future[Seq[E]] = db.run(query.result)

  def find(id: Id): Future[Option[E]] =
    db.run(filterById(id).result.head)
      .map(Option.apply)
      .recover {
        case _: NoSuchElementException => None
      }

  def store(entity: E): Future[E] =
    db.run(query += entity)
      .map(_ => entity)
}
