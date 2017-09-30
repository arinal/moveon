package com.veon.common.core

import scala.concurrent.Future
import scala.language.higherKinds

trait Entity[Id] {
  def id: Id
}

trait Repository[E <: Entity[_], Id] {
  def count: Future[Int]
  def all: Future[Seq[E]]
  def find(id: Id): Future[Option[E]]
  def store(entity: E): Future[E]
}
