package com.veon.common.core

import scala.concurrent.Future
import scala.language.higherKinds

trait ErrorType
case object InputError    extends ErrorType
case object ProcessError  extends ErrorType
case object NotFoundError extends ErrorType
case object UnknownError  extends ErrorType
case object NoError       extends ErrorType

case class ErrorToken(message: String, errorType: ErrorType = UnknownError) extends Throwable

object ErrorToken {

  val empty = ErrorToken("", NoError)

  def left(message: String, errorType: ErrorType = UnknownError)
    = Left(ErrorToken(message, errorType))

  def future(message: String, errorType: ErrorType = UnknownError)
    = Future.failed(ErrorToken(message, errorType))
}

trait Entity[Id] {
  def id: Id
}

trait Repository[E <: Entity[_], Id] {
  def find(id: Id): Future[Option[E]]
  def store(entity: E): Future[E]
}
