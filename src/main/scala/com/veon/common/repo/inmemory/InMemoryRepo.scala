package com.veon.common.repo.inmemory

import com.veon.common.core.{Entity, Repository}

import scala.collection.mutable
import scala.concurrent.Future

trait InMemoryRepo[E <: Entity[Id], Id] extends Repository[E, Id] {

  private lazy val entityMap = mutable.Map[Id, E]()

  override def find(id: Id): Future[Option[E]] =
    Future.successful(entityMap.get(id))

  override def store(entity: E): Future[E] = {
    entityMap += entity.id -> entity
    Future.successful(entity)
  }
}
