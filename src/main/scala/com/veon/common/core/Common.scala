package com.veon.common.core

import scala.concurrent.Future
import scala.language.higherKinds

trait Entity[Id] {
  def id: Id
}

trait Repository[E <: Entity[_], Id] {
  def count: Future[Int]
  def find(id: Id): Future[Option[E]]
  def all: Future[Seq[E]]

  def store(entity: E): Future[E]
  def update(entity: E): Future[E]
}
