package com.lamedh.moveon.core.reservation

import scala.concurrent.Future

trait AllocationFinder {
  def find(id: String): Future[(String, Int)]
}
