package com.veon.common.core

import scala.concurrent.Future
import scala.language.higherKinds

case class Error(message: String) extends Throwable

object Error {
  def left(message: String) = Left(Error(message))
  def future(message: String) = Future.failed(Error(message))
}

trait Entity[Id] {
  def id: Id
}

trait Repository[E <: Entity[_], Id] {
  def find(id: Id): Future[Option[E]]
  def store(entity: E): Future[E]
}
