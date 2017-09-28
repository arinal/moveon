package com.veon.common.core

import scala.concurrent.Future
import scala.language.higherKinds

case class ErrorToken(message: String) extends Throwable

object ErrorToken {
  def left(message: String) = Left(ErrorToken(message))
  def future(message: String) = Future.failed(ErrorToken(message))
}

trait Entity[Id] {
  def id: Id
}

trait Repository[E <: Entity[_], Id] {
  def find(id: Id): Future[Option[E]]
  def store(entity: E): Future[E]
}
