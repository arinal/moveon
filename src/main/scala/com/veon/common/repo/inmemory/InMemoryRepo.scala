package com.veon.common.repo.inmemory

import com.veon.common.core.{Entity, Repository}

import scala.collection.mutable
import scala.concurrent.Future

abstract class InMemoryRepo[E <: Entity[Id], Id] extends Repository[E, Id] {

  private lazy val entityMap = mutable.Map[Id, E]()

  override def count        = Future.successful(entityMap.size)
  override def all          = Future.successful(entityMap.values.toSeq)
  override def find(id: Id) = Future.successful(entityMap.get(id))

  override def update(entity: E) = insert(entity)
  override def insert(entity: E) = {
    entityMap += entity.id -> entity
    Future.successful(entity)
  }
}
